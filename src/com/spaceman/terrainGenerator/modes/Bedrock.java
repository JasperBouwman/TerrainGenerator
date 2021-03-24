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
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.getGenData;

public class Bedrock extends DataMode<Boolean> {

    private SimplexOctaveGenerator gen1;

    public Bedrock() {
        setModeData(true);
        gen1 = new SimplexOctaveGenerator(1432672635347583L, 8);
        gen1.setScale(2);
    }

    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        return TerrainUtils.tabListCreateAndSetBoolean(args);
    }

    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        return TerrainUtils.tabListCreateAndSetBoolean(args);
    }

    @Override
    public void saveMode(ConfigurationSection section) {
        TerrainUtils.saveDataBoolean(section, getModeData());
    }

    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        return TerrainUtils.getDataBoolean(section, this);
    }

    @Override
    public void setData(LinkedList<String> data, Player player) {
        TerrainUtils.setDataBoolean(data, player, this);
    }

    @Override
    public String getInsertion() {
        return String.valueOf(getModeData());
    }

    @Override
    public boolean isFinalMode() {
        return false;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that adds a layer of bedrock on the start of the TerrainGenerator";
    }

    @Override
    public String getModeName() {
        return "bedrock";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        int lowest = genData.getStartGen();

        if (getModeData()) {
            double bedrockHeight = gen1.noise(x, z, 5, 0.5) + lowest + 1;
            if (bedrockHeight < lowest) {
                bedrockHeight = lowest + 1;
            }
            for (int y = lowest; y < bedrockHeight && y < genData.getHeightGen(); y++) {
                if (chunkData != null) {
                    chunkData.setBlock(y, Material.BEDROCK);
                } else {
                    setType(new Location(locData.getWorld(), x, y, z).getBlock(), Material.BEDROCK);
                }
            }
        }
    }
}
