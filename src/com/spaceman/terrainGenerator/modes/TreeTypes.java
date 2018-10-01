package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.terrain.*;
import org.bukkit.ChatColor;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;

import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;
import static com.spaceman.terrainGenerator.terrain.TerrainUtils.toTreeType;

@SuppressWarnings("unused, WeakerAccess, unchecked")
public class TreeTypes extends TerrainMode.MapBased<TreeType, Integer> {

    @Override
    public void saveMode(String savePath) {
        Files terrainData = getFiles("terrainData");
        if (getModeData() != null) {
            for (TreeType is : getModeData().keySet()) {
                terrainData.getConfig().set(savePath + "." + TerrainUtils.toString(is), getModeData().get(is));
            }
            terrainData.saveConfig();
        }
    }

    @Override
    public MapBased getMode(String savePath, MapBased templateMode) {
        Files terrainData = getFiles("terrainData");

        LinkedHashMap<TreeType, Integer> data = new LinkedHashMap<>();

        for (String s : terrainData.getConfig().getConfigurationSection(savePath).getKeys(false)) {
            TreeType tree = toTreeType(s);
            data.put(tree, terrainData.getConfig().getInt(savePath + "." + s));
        }
        templateMode.setModeData(data);

        return templateMode;
    }

    @Override
    public boolean isFinalMode() {
        return false;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is the TerrainMode where you set the treeTypes for the TerrainMode addTrees";
    }

    @Override
    @SuppressWarnings({"unused", "deprecation"})
    public void addData(LinkedList<String> data, Player player) {
        //tree=TREE,1 tree=ACACIA,1
        if (data != null) {
            if (data.size() > 0) {
                for (String s : data) {
                    String ss = s.toLowerCase();

                    try {
                        String tmp = ss.split("tree=")[1].split(",")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,d=0,1'");
                        continue;
                    }
                    TreeType tree;
                    try {
                        tree = TreeType.valueOf(ss.split("tree=")[1].split(",")[0]);
                    } catch (IllegalArgumentException iag) {
                        player.sendMessage(ChatColor.RED + "TreeType " + ss.split("tree=")[1].split(",")[0] + " does not exist");
                        continue;
                    }

                    try {
                        int i = Integer.parseInt(ss.split("m=")[1].split(",d=")[1].split(",")[1]);
                        if (getModeData().containsKey(tree)) {
                            getModeData().put(tree, i);
                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully edited: " + s);
                        } else {
                            getModeData().put(tree, i);
                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully added: " + s);
                        }

                    } catch (NumberFormatException nfe) {
                        player.sendMessage(ChatColor.RED + ss.split("m=")[1].split(",d=")[1].split(",")[1] + " is not a valid number");
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }

    @Override
    @SuppressWarnings({"unused", "deprecation"})
    public void removeData(LinkedList<String> data, Player player) {
        //tree=TREE tree=ACACIA
        if (data != null) {
            if (data.size() > 0) {
                for (String s : data) {
                    String ss = s.toLowerCase();

                    try {
                        String tmp = ss.split("tree=")[1].split(",")[0];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,d=0,1'");
                        continue;
                    }
                    TreeType tree;
                    try {
                        tree = TreeType.valueOf(ss.split("tree=")[1].split(",")[0]);
                    } catch (IllegalArgumentException iag) {
                        player.sendMessage(ChatColor.RED + "TreeType " + ss.split("tree=")[1].split(",")[0] + " does not exist");
                        continue;
                    }

                    getModeData().remove(tree);
                    player.sendMessage(ChatColor.DARK_AQUA + "Successfully removed: " + s);

                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }

    @Override
    @SuppressWarnings({"unused", "deprecation"})
    public void setData(LinkedList<String> data, int number, Player player) {
        //2 tree=TREE,1

        if (data != null) {
            if (data.size() > 1) {
                String ss = data.get(1).toLowerCase();

                try {
                    String tmp = ss.split("tree=")[1].split(",")[0];
                } catch (ArrayIndexOutOfBoundsException e) {
                    player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,d=0,1'");
                    return;
                }
                TreeType tree;
                try {
                    tree = TreeType.valueOf(ss.split("tree=")[1].split(",")[0]);
                } catch (IllegalArgumentException iag) {
                    player.sendMessage(ChatColor.RED + "TreeType " + ss.split("tree=")[1].split(",")[0] + " does not exist");
                    return;
                }

                int i;
                try {
                    i = Integer.parseInt(ss.split("m=")[1].split(",d=")[1].split(",")[1]);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED + ss.split("m=")[1].split(",d=")[1].split(",")[1] + " is not a valid number");
                    return;
                }

                LinkedHashMap<TreeType, Integer> map = getModeData();
                map.remove(tree);

                if (number > map.size() + 1) {
                    player.sendMessage(ChatColor.RED + "Your given place must be lower than the amount of data in the TerrainMode " + getModeName() + " (" + (map.size() + 1) + ")");
                    return;
                }

                LinkedHashMap<TreeType, Integer> newMap = new LinkedHashMap<>();

                int tmp = 1;
                boolean b = true;
                for (TreeType iss : map.keySet()) {
                    if (tmp == number) {
                        newMap.put(tree, i);
                        b = false;
                    }
                    newMap.put(iss, map.get(iss));
                    tmp++;
                }
                if (b) {
                    newMap.put(tree, i);
                }

                setModeData(newMap);

                player.sendMessage(ChatColor.DARK_AQUA + "Successfully set TerrainMode data to place " + number);
            }
        }
    }

    @Override
    public String getModeName() {
        return "treeTypes";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                            TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
    }

    public TreeType useMode() {

        int total = 0;

        for (TreeType tree : getModeData().keySet()) {
            total += getModeData().get(tree);
        }

        Random random = new Random();

        int mSelected = random.nextInt(total);

        total = 0;

        for (TreeType tree : getModeData().keySet()) {
            total += getModeData().get(tree);
            if (total > mSelected) {
                return tree;
            }
        }
        return null;
    }
}
