package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.MapMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeInverse;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeWaterLoggable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.getAirPockets;

public class Layers extends MapMode<TerrainBlockData, Integer> implements TerrainModeInverse, TerrainModeWaterLoggable {

    private boolean inverse = false;

    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        return TerrainUtils.tabListAddAndCreateMapTerrainBlockDataInteger(args, "h");
    }

    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        return TerrainUtils.tabListSetMapTerrainBlockDataInteger(args, "h");
    }

    @Override
    public Collection<String> tabListAdd(String[] args, Player player) {
        return TerrainUtils.tabListAddAndCreateMapTerrainBlockDataInteger(args, "h");
    }

    @Override
    public Collection<String> tabListRemove(String[] args, Player player) {
        return TerrainUtils.tabListRemoveMapTerrainBlockDataInteger(args, this);
    }

    @Override
    public void saveMode(ConfigurationSection section) {
        TerrainUtils.saveMapTerrainBlockDataInteger(section, getModeData());
    }

    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        return TerrainUtils.getMapTerrainBlockDataInteger(section, this);
    }

    @Override
    public void addData(LinkedList<String> data, Player player) {
        TerrainUtils.addDataTerrainBlockDataInteger(data, player, "h", this);
    }

    @Override
    public void removeData(LinkedList<String> data, Player player) {
        TerrainUtils.removeDataTerrainBlockDataInteger(data, player, this);
    }

    @Override
    public void setData(LinkedList<String> data, int number, Player player) {
        TerrainUtils.setDataTerrainBlockDataInteger(data, number, player, "h", this);
    }

    @Override
    public void setWaterLogged(LinkedList<String> data, Player player) {
        TerrainUtils.setWaterLoggedMapTerrainBlockDataObject(data, player, this.getModeData().keySet());
    }

    @Override
    public Collection<String> tabListWaterLog(String[] args, Player player) {
        return TerrainUtils.tabListWaterLogMapTerrainBlockDataObject(args, this.getModeData().keySet());
    }

    @Override
    public String getInsertion(TerrainBlockData terrainBlockData, Integer integer) {
        return "m=" + terrainBlockData.getMaterial().name() + ",d=" + terrainBlockData.getBlockFace().name() + ",w=" + terrainBlockData.isWaterLogged() + ",h=" + integer;
    }

    @Override
    public boolean isFinalMode() {
        return false;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that will override the TerrainGenerator material and will add layers of given materials";
    }

    @Override
    public String getModeName() {
        return "layers";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String
                                savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
        if (getModeData() == null || getModeData().isEmpty()) {
            return;
        }

        TerrainGenerator.GenData genData = TerrainGenerator.GenData.getGenData(x, z, data.getName() + savePath, genStorage);

        int fromY = 0;
        Collection<Integer> airPockets = (genData.isFromTop() ? Collections.emptyList() : getAirPockets(x, z, genStorage, genData.getStartGen(), genData.getHeightGen(), data.getName()));

        for (TerrainBlockData is : getModeData().keySet()) {

            for (int mY = 0; mY < getModeData().get(is); mY++) {
                int y = (inverse ? genData.getStartGen() + fromY : genData.getHeightGen() - fromY);
                if (!genData.isFromTop()) {
                    if (!airPockets.contains(y)) {
                        fromY++;
                        continue;
                    }
                }
                if (!is.getMaterial().equals(Material.STRUCTURE_VOID)) {
                    if (genData.getStartGen() <= y) {
                        if (chunkData != null) {
                            chunkData.setBlock(y, is);
                        } else {
                            Block block = new Location(locData.getWorld(), x, y, z).getBlock();
                            setType(block, is);
                        }
                    }
                }
                fromY++;
            }

        }
    }

    @Override
    public boolean isInverse() {
        return inverse;
    }

    @Override
    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }
}