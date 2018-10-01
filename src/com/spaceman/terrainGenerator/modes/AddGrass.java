package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.*;

public class AddGrass extends TerrainMode.MapBased<TerrainBlockData, Integer> {

    @Override
    public void saveMode(String savePath) {
        TerrainUtils.saveMapItemStackInteger(savePath, getModeData());
    }

    @Override
    public MapBased getMode(String savePath, MapBased templateMode) {
        return TerrainUtils.getMapTerrainBlockDataInteger(savePath, templateMode);
    }

    @Override
    @SuppressWarnings({"unused", "deprecation"})
    public void addData(LinkedList<String> data, Player player) {
//        //m=GRASS,1 m=dirt,3
//        if (data != null) {
//            if (data.size() > 0) {
//                for (String s : data) {
//                    String ss = s.toLowerCase();
//
//                    try {
//                        String tmp = ss.split("m=")[1].split(",")[1];
//                    } catch (ArrayIndexOutOfBoundsException e) {
//                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,1'");
//                        continue;
//                    }
//
//                    String mS = ss.split("m=")[1].split(",")[0];
//                    Material m = Material.getMaterial(mS);
//                    if (m == null) {
//                        player.sendMessage(ChatColor.RED + mS + " is not a valid Material");
//                        continue;
//                    }
//
//
//                    ItemStack is = new ItemStack(m);
//                    try {
//                        int i = Integer.parseInt(ss.split("m=")[1].split(",")[1]);
//                        if (getModeData().containsKey(is)) {
//                            getModeData().put(is, i);
//                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully edited: " + s);
//                        } else {
//                            getModeData().put(is, i);
//                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully added: " + s);
//                        }
//
//                    } catch (NumberFormatException nfe) {
//                        player.sendMessage(ChatColor.RED + ss.split("m=")[1].split(",")[1] + " is not a valid number");
//                    }
//                }
//            } else {
//                player.sendMessage(ChatColor.RED + "Missing data");
//            }
//        }
        TerrainUtils.addData(data, player, getModeData());
    }

    @Override
    @SuppressWarnings({"unused", "deprecation"})
    public void removeData(LinkedList<String> data, Player player) {
//        //m=GRASS m=dirt
//        if (data != null) {
//            if (data.size() > 0) {
//                for (String s : data) {
//                    String ss = s.toLowerCase();
//
//                    try {
//                        String tmp = ss.split("m=")[1];
//                    } catch (ArrayIndexOutOfBoundsException e) {
//                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS'");
//                        continue;
//                    }
//
//                    String mS = ss.split("m=")[1];
//                    Material m = Material.getMaterial(mS);
//                    if (m == null) {
//                        player.sendMessage(ChatColor.RED + mS + " is not a valid Material. Make sure that your given data is in the right format 'm=GRASS'");
//                        continue;
//                    }
//
//                    ItemStack is = new ItemStack(m);
//                    getModeData().remove(is);
//                    player.sendMessage(ChatColor.DARK_AQUA + "Successfully removed: " + s);
//
//                }
//            } else {
//                player.sendMessage(ChatColor.RED + "Missing data");
//            }
//        }
        TerrainUtils.removeData(data, player, getModeData());
    }

    @Override
    @SuppressWarnings({"unused", "deprecation"})
    public void setData(LinkedList<String> data, int number, Player player) {
//        //3 m=GRASS,1
//
//        if (data != null) {
//            if (data.size() > 1) {
//                String ss = data.get(1).toLowerCase();
//
//                try {
//                    String tmp = ss.split("m=")[1].split(",")[1];
//                } catch (ArrayIndexOutOfBoundsException e) {
//                    player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,1'");
//                    return;
//                }
//
//                String mS = ss.split("m=")[1].split(",")[0];
//                Material m = Material.getMaterial(mS);
//                if (m == null) {
//                    player.sendMessage(ChatColor.RED + mS + " is not a valid Material. Make sure that your given data is in the right format 'm=GRASS,1'");
//                    return;
//                }
//
//                ItemStack is = new ItemStack(m);
//                int i;
//                try {
//                    i = Integer.parseInt(ss.split("m=")[1].split(",")[1]);
//                } catch (NumberFormatException nfe) {
//                    player.sendMessage(ChatColor.RED + ss.split("m=")[1].split(",")[1] + " is not a valid number");
//                    return;
//                }
//
//                LinkedHashMap<ItemStack, Integer> map = getModeData();
//                map.remove(is);
//
//                if (number > map.size() + 1) {
//                    player.sendMessage(ChatColor.RED + "Your given place must be lower than the amount of data in the TerrainMode " + getModeName() + " (" + (map.size() + 1) + ")");
//                    return;
//                }
//
//                LinkedHashMap<ItemStack, Integer> newMap = new LinkedHashMap<>();
//
//                int tmp = 1;
//                boolean b = true;
//                for (ItemStack iss : map.keySet()) {
//                    if (tmp == number) {
//                        newMap.put(is, i);
//                        b = false;
//                    }
//                    newMap.put(iss, map.get(iss));
//                    tmp++;
//                }
//                if (b) {
//                    newMap.put(is, i);
//                }
//
//                setModeData(newMap);
//
//                player.sendMessage(ChatColor.DARK_AQUA + "Successfully set TerrainMode data to place " + number);
//
//
//            }
//        }
        TerrainUtils.setData(data, number, player, this);
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

        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        int highest = getHighest(x, z, genStorage);
        ArrayList<Integer> airPockets = getAirPockets(x, z, genStorage, genData.getStartGen(), highest);

        if (genData.getHeightGen() == highest && genData.getHeightGen() > 0 || airPockets.contains(genData.getHeightGen() + 1)) {

//            System.out.println(x + " " + z + " " + chunkData.getChunkX() + " " + chunkData.getChunkZ());

            int total = 0;
            for (TerrainBlockData is : getModeData().keySet()) {
                total += getModeData().get(is);
            }

            Random random = new Random();

            int mSelected = random.nextInt(total);

            total = 0;

            for (TerrainBlockData is : getModeData().keySet()) {
                total += getModeData().get(is);
                if (getModeData().get(is) > mSelected) {
                    if (!is.getMaterial().equals(Material.STRUCTURE_VOID)) {
                        if (chunkData != null) {
                            chunkData.setBlock(genData.getHeightGen() + 1, is.getMaterial());
                        } else {
                            setType(new Location(locData.getWorld(), x, genData.getHeightGen() + 1, z).getBlock(), is.getMaterial(), is.getBlockFace());
                        }
                    }
                    return;
                }
            }
        }
    }
}
