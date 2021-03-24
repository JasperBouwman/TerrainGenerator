package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.*;

public class AddTrees extends DataMode<Integer> {
    
    public static int maxValue = 100000;
    
    public AddTrees() {
        setModeData(1);
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
        return getModeName() + " is a TerrainMode that add trees, it uses the TerrainMode 'treeTypes'. The change of a tree spawning is the TerrainMode data to " + maxValue;
    }
    
    @Override
    public String getModeName() {
        return "addTrees";
    }
    
    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
        if (getModeData() <= 0) {
            return;
        }
        
        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        int highest = getHighest(x, z, genStorage);
        
        if (genData.getHeightGen() == highest && genData.getHeightGen() > 0 || getAirPockets(x, z, genStorage, genData.getStartGen(), highest).contains(genData.getHeightGen() + 1)) {
            if (genData.getStartGen() < genData.getHeightGen()) {
                
                int chance = new Random(((long) z) * 6000000L + x).nextInt(maxValue);
                
                if (getModeData() > chance) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> new BukkitRunnable() {
                        public void run() {
                            TreeTypes types = (TreeTypes) data.getMode("treeTypes");
                            if (types != null) {
                                TreeTypes.TreeData treeData = types.getTree(genData.getRandom());
                                TreeType tree = treeData.getTreeType();
                                if (tree == null) {
                                    tree = TreeType.TREE;
                                }
                                Block block = new Location(locData.getWorld(), x, genData.getHeightGen(), z).getBlock();
                                Block treeBlock = new Location(locData.getWorld(), x, genData.getHeightGen() + 1, z).getBlock();
                                
                                BlockData groundBlock = null;
                                BlockData supportBlock = null;
                                
                                if (!treeBlock.getType().equals(Material.AIR)) {
                                    groundBlock = treeBlock.getBlockData().clone();
                                    treeBlock.setType(Material.GOLD_BLOCK);
                                }
                                
                                if (treeData.getBlockData().getMaterial() != Material.STRUCTURE_VOID) {
                                    supportBlock = block.getBlockData().clone();
                                    setType(block, treeData.getBlockData());
                                }
                                
                                TreeModifier treeModifier = (TreeModifier) data.getMode("treeModifier");
                                
                                if (treeModifier == null) {
                                    if (!locData.getWorld().generateTree(treeBlock.getLocation(), tree)) {
                                        resetBlocks(treeBlock, groundBlock, block, supportBlock);
                                    }
                                } else {
                                    if (!locData.getWorld().generateTree(treeBlock.getLocation(), tree, treeModifier.getBlockChangeDelegate(locData.getWorld()))) {
                                        resetBlocks(treeBlock, groundBlock, block, supportBlock);
                                    }
                                }
                            }
                        }
                        
                        private void resetBlocks(Block treeBlock, BlockData groundBlock, Block block, BlockData supportBlock) {
                            if (groundBlock != null) setType(treeBlock, groundBlock);
                            if (supportBlock != null) setType(block, supportBlock);
                        }
                        
                    }.runTask(Main.getInstance()), 1);
                    
                }
            }
        }
    }
}
