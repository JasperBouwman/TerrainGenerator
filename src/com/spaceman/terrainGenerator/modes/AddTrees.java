package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import com.spaceman.terrainGenerator.terrain.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.*;

@SuppressWarnings("unused, WeakerAccess, unchecked")
public class AddTrees extends TerrainMode.DataBased<Integer> {

    public AddTrees() {
        setModeData(1);
    }

    @Override
    public void saveMode(String savePath) {
        TerrainUtils.saveDataInteger(savePath, getModeData());
    }

    @Override
    public DataBased getMode(String savePath, DataBased templateMode) {
        return TerrainUtils.getDataInteger(savePath, templateMode);
    }

    @Override
    public boolean isFinalMode() {
        return true;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that add trees, it uses the TerrainMode 'treeTypes'. The chance of a tree spawning is the TerrainMode data to 1000";
    }

    @Override
    public void setData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() >= 1) {
                try {
                    int i = Integer.parseInt(data.get(0));
                    setModeData(i);
                    player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + getModeName() + " is now set to " + i);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED + "Given data is not a valid number");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }

    @Override
    public String getModeName() {
        return "addTrees";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                            TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
        if (getModeData() == 0) {
            return;
        }

        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        int highest = getHighest(x, z, genStorage);
        ArrayList<Integer> airPockets = getAirPockets(x, z, genStorage, genData.getStartGen(), highest);

        if (genData.getHeightGen() == highest && genData.getHeightGen() > 0 || airPockets.contains(genData.getHeightGen() + 1)) {
            if (genData.getStartGen() < genData.getHeightGen()) {

                Random random = new Random();

                int chance = random.nextInt(1000);

                if (getModeData() > chance) {
                    new BukkitRunnable() {
                        public void run() {
                            TreeTypes types = (TreeTypes) data.getMode("treeTypes");
                            if (types != null) {
                                TreeType tree = types.useMode();
                                if (tree == null) {
                                    tree = TreeType.JUNGLE;
                                }
                                Block block = new Location(locData.getWorld(), x, genData.getHeightGen(), z).getBlock();
                                Location treeBlock = new Location(locData.getWorld(), x, genData.getHeightGen() + 1, z);
                                if (treeBlock.getBlock().getType().equals(Material.AIR)) {
                                    block.setType(Material.DIRT);
                                    locData.getWorld().generateTree(treeBlock, tree);
                                }
                            }
                        }
                    }.runTask(GettingFiles.p);
                }
            }
        }
    }
}
