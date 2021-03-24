package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeInverse;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeWaterLoggable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.*;

public class Top extends DataMode<TerrainBlockData> implements TerrainModeInverse, TerrainModeWaterLoggable {

    public Top() {
        setModeData(new TerrainBlockData(Material.GRASS));
    }

    private boolean inverse = false;

    @Override
    public boolean isInverse() {
        return inverse;
    }

    @Override
    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        return TerrainUtils.tabListCreateAndSetTerrainBlockData(args);
    }

    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        return TerrainUtils.tabListCreateAndSetTerrainBlockData(args);
    }

    @Override
    public void saveMode(ConfigurationSection section) {
        TerrainUtils.saveDataTerrainBlockData(section, getModeData());
    }

    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        return TerrainUtils.getDataTerrainBlockData(section, this);
    }

    @Override
    public void setData(LinkedList<String> data, Player player) {
        TerrainUtils.setDataTerrainBlockData(data, player, this);
    }

    @Override
    public void setWaterLogged(LinkedList<String> data, Player player) {
        TerrainUtils.setWaterLoggedTerrainBlockData(data, player, getModeData());
    }

    @Override
    public Collection<String> tabListWaterLog(String[] args, Player player) {
        return TerrainUtils.tabListWaterLogTerrainBlockData(args);
    }

    @Override
    public String getInsertion() {
        return "m=" + getModeData().getMaterial().name() +
                ",d=" + getModeData().getBlockFace().name() +
                ",w=" + getModeData().isWaterLogged();
    }

    @Override
    public boolean isFinalMode() {
        return false;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that will change the top layer to given material";
    }

    @Override
    public String getModeName() {
        return "top";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        if (!getModeData().getMaterial().equals(Material.STRUCTURE_VOID)) {

            TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
            int highest = getHighest(x, z, genStorage);

            if (genData.getHeightGen() == highest && genData.getHeightGen() > 0 || getAirPockets(x, z, genStorage, genData.getStartGen(), highest).contains(genData.getHeightGen() + 1)) {

                if (genData.getStartGen() < genData.getHeightGen()) {
                    int y = (inverse ? genData.getStartGen() : genData.getHeightGen());

                    if (chunkData != null) {
                        chunkData.setBlock(y, getModeData());
                    } else {
                        setType(new Location(locData.getWorld(), x, y, z).getBlock(), getModeData());
                    }
                }
            }
        }
    }
}

