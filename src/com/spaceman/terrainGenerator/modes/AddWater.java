package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.*;

public class AddWater extends DataMode<Integer> {

    public AddWater() {
        setModeData(-1);
    }

    @Override
    public void saveMode(ConfigurationSection section) {
        TerrainUtils.saveDataInteger(section, getModeData());
    }

    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        return TerrainUtils.getDataInteger(section, this);
    }

    @Override
    public void setData(LinkedList<String> data, Player player) {
        TerrainUtils.setDataInteger(data, player, this);
    }

    @Override
    public String getInsertion() {
        return String.valueOf(getModeData());
    }

    @Override
    public boolean isFinalMode() {
        return true;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that adds water lakes starting of the given TerrainMode data";
    }

    @Override
    public String getModeName() {
        return "addWater";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                            TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        ArrayList<Integer> airPockets = getAirPockets(x, z, genStorage, genData.getStartGen(), getModeData());

        if (getModeData() < 0) {
            return;
        }

        for (int y = getModeData(); y > 0; y--) {
            if (airPockets.contains(y) || getHighest(x, z, genStorage) < y) {
                if (chunkData != null) {
                    chunkData.setBlock(y, Material.WATER);
                } else {
                    setType(new Location(locData.getWorld(), x, y, z).getBlock(), Material.WATER);
                }
            } else {
                return;
            }
        }
    }
}
