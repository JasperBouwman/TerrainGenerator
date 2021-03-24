package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.MapMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.*;

public class AddGrass extends MapMode<TerrainBlockData, Integer> {

    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        return TerrainUtils.tabListAddAndCreateMapTerrainBlockDataInteger(args, "r");
    }

    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        return TerrainUtils.tabListSetMapTerrainBlockDataInteger(args, "r");
    }

    @Override
    public Collection<String> tabListAdd(String[] args, Player player) {
        return TerrainUtils.tabListAddAndCreateMapTerrainBlockDataInteger(args, "r");
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
        TerrainUtils.addDataTerrainBlockDataInteger(data, player, "r", this);
    }

    @Override
    public void removeData(LinkedList<String> data, Player player) {
        TerrainUtils.removeDataTerrainBlockDataInteger(data, player, this);
    }

    @Override
    public void setData(LinkedList<String> data, int number, Player player) {
        TerrainUtils.setDataTerrainBlockDataInteger(data, number, player, "r", this);
    }

    @Override
    public String getInsertion(TerrainBlockData terrainBlockData, Integer integer) {
        return "m=" + terrainBlockData.getMaterial().name() +
                ",d=" + terrainBlockData.getBlockFace().name() +
                ",w=" + terrainBlockData.isWaterLogged() +
                ",r=" + integer;
    }

    @Override
    public boolean isFinalMode() {
        return true;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that adds a layer of your given material on top of the TerrainGenerator";
    }

    @Override
    public String getModeName() {
        return "addGrass";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
        if (getModeData() == null || getModeData().isEmpty()) {
            return;
        }

        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        int highest = getHighest(x, z, genStorage);

        if (genData.getHeightGen() == highest && genData.getHeightGen() > 0 || getAirPockets(x, z, genStorage, genData.getStartGen(), highest).contains(genData.getHeightGen() + 1)) {

            int total = 0;
            for (TerrainBlockData is : getModeData().keySet()) {
                total += getModeData().get(is);
            }
            int mSelected = genData.getRandom().nextInt(total);

            total = 0;

            for (TerrainBlockData is : getModeData().keySet()) {
                total += getModeData().get(is);
                if (total > mSelected) {
                    if (!is.getMaterial().equals(Material.STRUCTURE_VOID)) {
                        if (genData.getStartGen() < genData.getHeightGen()) {
                            if (chunkData != null) {
                                chunkData.setBlock(genData.getHeightGen() + 1, is);
                            } else {
                                setType(new Location(locData.getWorld(), x, genData.getHeightGen() + 1, z).getBlock(), is);
                            }
                        }
                    }
                    return;
                }
            }
        }
    }
}