package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeContainsGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.worldSeedValue;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.getAirPockets;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.useModes;

@SuppressWarnings("unchecked")
public class StoneGen extends DataMode<String> implements TerrainModeContainsGenerator {

    public StoneGen() {
        setModeData("null");
    }

    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        return TerrainUtils.tabListCreateAndSetTerrainGenerator(args);
    }

    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        return TerrainUtils.tabListCreateAndSetTerrainGenerator(args);
    }

    @Override
    public void saveMode(ConfigurationSection section) {
        TerrainUtils.saveDataString(section, getModeData());
    }

    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        return TerrainUtils.getDataString(section, this);
    }

    @Override
    public void remapTerrainGenerators(String file) {
        if (!this.getModeData().contains("/")) {
            this.setModeData(file + "/" + this.getModeData());
        }
    }

    @Override
    public void demapTerrainGenerators() {
        if (this.getModeData().contains("/")) {
            this.setModeData(this.getModeData().split("/")[1]);
        }
    }

    @Override
    public void setData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() >= 1) {
                String gen = data.get(0);
                TerrainGenData d = getGen(gen);
                if (d == null) {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", gen));
                    return;
                }
                setModeData(gen);
                player.sendMessage(formatSuccess("TerrainMode %s is now set to %s", this.getModeName(), gen));
            } else {
                player.sendMessage(formatError("Missing data"));
            }
        }
    }

    @Override
    public String getModeName() {
        return "stoneGen";
    }

    @Override
    public String getInsertion() {
        return getModeData();
    }

    @Override
    public boolean isFinalMode() {
        return false;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that change the material above the given TerrainGenerator height to the given TerrainGenerator material";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        //newGenHeight is the start of this generator
        TerrainGenData newData = terrainGenData.get(getModeData());
        if (newData == null) {
            return;
        }

        String infLoopProtectionPath = getModeName() + ";infLoop;" + x + ";" + z;
        if (genModeData.containsKey(infLoopProtectionPath)) {// inf loop protection
            if (genModeData.get(infLoopProtectionPath) instanceof ArrayList) {
                if (((ArrayList) genModeData.get(infLoopProtectionPath)).contains(newData.getName())) {
                    return;
                }

                ((ArrayList) genModeData.get(infLoopProtectionPath)).add(newData.getName());
            } else {
                return;
            }
        } else {
            genModeData.put(infLoopProtectionPath, new ArrayList<>(Collections.singleton(newData.getName())));
        }

        ArrayList<String> preGenList = new ArrayList<>();//fromTop accessible
        String preGenPath = getModeName() + ";preGen;" + data.getName() + ";" + x + ";" + z;
        String last = "";
        String tmpSavePath = savePath.replaceFirst(";StoneGen", "");
        while (genModeData.containsKey(preGenPath)) {
            String owningGen = last = (String) genModeData.get(preGenPath);
            preGenList.add(owningGen + tmpSavePath);
            preGenPath = getModeName() + ";preGen;" + owningGen + ";" + x + ";" + z;
            tmpSavePath = tmpSavePath.replaceFirst(";StoneGen", "");
        }
        genModeData.put(getModeName() + ";preGen;" + newData.getName() + ";" + x + ";" + z, data.getName());
        preGenList.add(data.getName() + savePath);
        preGenList.add(last);

        long seed = newData.getSeed();
        if (seed == worldSeedValue) {
            seed = locData.getStartL().getWorld().getSeed();
        }

        TerrainMode layersMode = newData.getMode("layers");
        TerrainGenerator.GenData genData = TerrainGenerator.GenData.getGenData(x, z, data.getName() + savePath, genStorage);

        int newGenHeight = (newData.getTerrainNoise().noise(x, z, seed) + newData.getHeight());


        Collection<Integer> airPockets = (genData.isFromTop() ? Collections.emptyList() : getAirPockets(x, z, genStorage, genData.getStartGen(), genData.getHeightGen(),
                preGenList.toArray(new String[]{})));

        int startGen = newGenHeight;
        int endGen = genData.getHeightGen();

//        if (!genData.isFromTop()) {
//            startGen = airPockets.stream().mapToInt((t) -> t).min().orElse(startGen);
//            endGen = airPockets.stream().mapToInt((t) -> t).max().orElse(endGen);
//        }

        if (startGen < newData.getMin(x, z, locData.getWorld())) {
            startGen = newData.getMin(x, z, locData.getWorld());
        }
        if (endGen > newData.getMax(x, z, locData.getWorld())) {
            endGen = newData.getMax(x, z, locData.getWorld());
        }

//        for (int y = genData.getHeightGen(); y > newGenHeight && y > genData.getStartGen(); y--) {
        for (int y = endGen; y > startGen && y > genData.getStartGen(); y--) {
            // internal modes: 'layers'

            if (!genData.isFromTop()) {
                if (!airPockets.contains(y)) {
                    continue;
                }
            }

            Block block = new Location(locData.getWorld(), x, y, z).getBlock();

            if (newData.getBiome() != null) {
                if (chunkData != null) {
                    chunkData.getBiomeGrid().setBiome(chunkData.getX(), chunkData.getZ(), newData.getBiome());
                } else {
                    block.setBiome(newData.getBiome());
                }
            }

            if (layersMode == null) {
                if (!newData.getTerrainBlockData().getMaterial().equals(Material.STRUCTURE_VOID)) {
                    if (chunkData != null) {
                        chunkData.setBlock(y, newData.getTerrainBlockData());
                    } else {
                        setType(block, newData.getTerrainBlockData());
                    }
                }
            }
        }

        HashMap<String, TerrainGenerator.GenData> tmpMap = genStorage.getOrDefault(x + ";" + z, new HashMap<>());
        tmpMap.put(newData.getName() + savePath + ";StoneGen",
                new TerrainGenerator.GenData(Math.max(newGenHeight,/* genData.getStartGen()*/ 0), endGen,
                        genData.getStart(), genData.getEnd(),
                        genData.getRandom(), genData.isFromTop()));
        genStorage.put(x + ";" + z, tmpMap);

        useModes(newData, x, z, genStorage, locData, savePath + ";StoneGen", genModeData, chunkData);

//        if (chunkData != null) {
//            com.spaceman.terrainGenerator.terrain.WorldGenerator.
//            useFinalChunkModes(new LinkedList<>(Collections.singletonList(newData.getName())), locData, genStorage, savePath + ";stoneGen", genModeData, chunkData);
//        } else {
//        useFinalModes(new LinkedList<>(Collections.singletonList(newData.getName())), locData, genStorage, savePath + ";stoneGen", recProtection, chunkData);
//        }
    }
    
    @Override
    public void checkGenerator(String generator) {
        if (getModeData().equals(generator)) {
            setModeData(null);
        }
    }
}
