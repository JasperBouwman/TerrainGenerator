package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.terrain.WorldGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.getGenData;
import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;

@SuppressWarnings("unused, WeakerAccess, unchecked")
public class Bedrock extends TerrainMode.DataBased<Boolean> {

    private SimplexOctaveGenerator gen1;

    public Bedrock() {
        gen1 = new SimplexOctaveGenerator(1432672635347583L, 8);
        gen1.setScale(2);
        setModeData(true);
    }

    @Override
    public void saveMode(String savePath) {
        Files terrainData = getFiles("terrainData");
        if (getModeData() != null) {
            terrainData.getConfig().set(savePath, getModeData());
            terrainData.saveConfig();
        }
    }

    @Override
    public DataBased getMode(String savePath, DataBased templateMode) {
        Files terrainData = getFiles("terrainData");
        boolean data = terrainData.getConfig().getBoolean(savePath);
        templateMode.setModeData(data);
        return templateMode;
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
    public void setData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() >= 1) {
                boolean b = Boolean.parseBoolean(data.get(0));
                setModeData(b);
                player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + getModeName() + " is now set to " + b);
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
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
            for (int y = lowest; y < bedrockHeight && y < genData.getHeightGen(); y++) {
                if (chunkData != null) {
                    chunkData.setBlock(y, Material.BEDROCK);
                } else {
                    setType(new Location(locData.getWorld(), x, y, z).getBlock(), new ItemStack(Material.BEDROCK));
                }
            }
        }
    }
}
