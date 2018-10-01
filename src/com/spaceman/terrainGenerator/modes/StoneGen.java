package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenData.worldSeedValue;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.*;

@SuppressWarnings("unused, WeakerAccess, unchecked")
public class StoneGen extends TerrainMode.DataBased<String> {

    public StoneGen() {
        setModeData("");
    }

    @Override
    public void saveMode(String savePath) {
        TerrainUtils.saveDataString(savePath, getModeData());
    }

    @Override
    public DataBased getMode(String savePath, DataBased templateMode) {
        return TerrainUtils.getDataString(savePath, templateMode);
    }

    @Override
    public void setData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() >= 1) {
                String gen = data.get(0);
                TerrainGenData d = getGen(gen);
                if (d == null) {
                    player.sendMessage(ChatColor.RED + "TerrainGenerator " + gen + " does not exist");
                    return;
                }
                setModeData(gen);
                player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + getModeName() + " is now set to " + gen);
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }

    @Override
    public String getModeName() {
        return "stoneGen";
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

        if (genModeData.containsKey(getModeName() + ";" + x + ";" + z)) {// inf loop protection
            if (genModeData.get(getModeName() + ";" + x + ";" + z) instanceof ArrayList) {
                if (((ArrayList) genModeData.get(getModeName() + ";" + x + ";" + z)).contains(newData.getName())) {
                    return;
                }

                ((ArrayList) genModeData.get(getModeName() + ";" + x + ";" + z)).add(newData.getName());
            } else {
                return;
            }
        } else {
            genModeData.put(getModeName() + ";" + x + ";" + z, new ArrayList<>(Collections.singleton(newData.getName())));
        }


        long seed = newData.getSeed();
        if (seed == worldSeedValue) {
            seed = locData.getStartL().getWorld().getSeed();
        }
        SimplexOctaveGenerator gen = new SimplexOctaveGenerator(seed, newData.getOctaves());
        gen.setScale(newData.getScale());

        TerrainMode layersMode = newData.getMode("layers");
        TerrainGenerator.GenData genData = TerrainGenerator.GenData.getGenData(x, z, data.getName() + savePath, genStorage);

        int newGenHeight = (int) (gen.noise(x, z, newData.getFrequency(), newData.getAmplitude()) * newData.getMultitude() + newData.getHeight());

        for (int y = genData.getHeightGen(); y > newGenHeight && y > genData.getStartGen(); y--) {
            // internal modes: 'layers'

            Block block = new Location(locData.getWorld(), x, y, z).getBlock();

            if (newData.getBiome() != null) {
                if (chunkData != null) {
                    chunkData.getBiomeGrid().setBiome(x,z, newData.getBiome());
                } else {
                    block.setBiome(newData.getBiome());
                }
            }

            if (layersMode == null) {
                if (!newData.getTerrainBlockData().getMaterial().equals(Material.STRUCTURE_VOID)) {
                    if (chunkData != null) {
                        chunkData.setBlock(y, newData.getTerrainBlockData().getMaterial());
                    } else {
                        setType(block, newData.getTerrainBlockData().getMaterial(), newData.getTerrainBlockData().getBlockFace());
                    }
                }
            }
        }

        HashMap<String, TerrainGenerator.GenData> tmpMap = genStorage.getOrDefault(x + ";" + z, new HashMap<>());
        tmpMap.put(newData.getName() + savePath + ";StoneGen", new TerrainGenerator.GenData(Math.max(newGenHeight,
                genData.getStartGen()), genData.getHeightGen(), genData.getStart(), genData.getEnd(), genData.isTerrain(), genData.getRandom()));
        genStorage.put(x + ";" + z, tmpMap);

        useModes(newData, x, z, genStorage, locData, savePath + ";StoneGen", genModeData, chunkData);

//        if (chunkData != null) {
//            com.spaceman.terrainGenerator.terrain.WorldGenerator.
//            useFinalChunkModes(new LinkedList<>(Collections.singletonList(newData.getName())), locData, genStorage, savePath + ";stoneGen", genModeData, chunkData);
//        } else {
//        useFinalModes(new LinkedList<>(Collections.singletonList(newData.getName())), locData, genStorage, savePath + ";stoneGen", recProtection, chunkData);
//        }
    }
}
