package com.spaceman.terrainGenerator.terrain;

import com.spaceman.terrainGenerator.fileHander.Files;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TerrainUtils {

    public static void saveMapItemStackInteger(String savePath, HashMap<TerrainBlockData, Integer> modeData) {
        Files terrainData = getFiles("terrainData");

        if (modeData == null) {
            return;
        }
        for (TerrainBlockData is : modeData.keySet()) {
            terrainData.getConfig().set(savePath + "." + TerrainUtils.toString(is), modeData.get(is));
        }
        terrainData.saveConfig();
    }

    @SuppressWarnings("unchecked")
    public static TerrainMode.MapBased getMapTerrainBlockDataInteger(String savePath, TerrainMode.MapBased templateMode) {
        Files terrainData = getFiles("terrainData");

        LinkedHashMap<TerrainBlockData, Integer> data = new LinkedHashMap<>();

        for (String s : terrainData.getConfig().getConfigurationSection(savePath).getKeys(false)) {
            TerrainBlockData is = toTerrainBlockData(s);
            data.put(is, terrainData.getConfig().getInt(savePath + "." + s));
        }
        templateMode.setModeData(data);

        return templateMode;
    }

    public static void saveDataInteger(String savePath, Integer modeData) {
        Files terrainData = getFiles("terrainData");
        if (modeData != null) {
            terrainData.getConfig().set(savePath, modeData);
            terrainData.saveConfig();
        }
    }

    @SuppressWarnings("unchecked")
    public static TerrainMode.DataBased getDataInteger(String savePath, TerrainMode.DataBased templateMode) {
        Files terrainData = getFiles("terrainData");
        int data = terrainData.getConfig().getInt(savePath);
        templateMode.setModeData(data);
        return templateMode;
    }

    public static void saveDataString(String savePath, String modeData) {
        Files terrainData = getFiles("terrainData");
        if (modeData != null) {
            terrainData.getConfig().set(savePath, modeData);
            terrainData.saveConfig();
        }
    }

    @SuppressWarnings("unchecked")
    public static TerrainMode.DataBased getDataString(String savePath, TerrainMode.DataBased templateMode) {
        Files terrainData = getFiles("terrainData");
        String data = terrainData.getConfig().getString(savePath);
        templateMode.setModeData(data);
        return templateMode;
    }

    public static void saveDataTerrainBlockData(String savePath, TerrainBlockData modeData) {
        Files terrainData = getFiles("terrainData");
        if (modeData != null) {
            terrainData.getConfig().set(savePath, modeData);
            terrainData.saveConfig();
        }
    }

    @SuppressWarnings("unchecked")
    public static TerrainMode.DataBased getDataTerrainBlockData(String savePath, TerrainMode.DataBased templateMode) {
        Files terrainData = getFiles("terrainData");
        TerrainBlockData data = (TerrainBlockData) terrainData.getConfig().get(savePath);//todo
        templateMode.setModeData(data);
        return templateMode;
    }

    public static TerrainBlockData toTerrainBlockData(String s) {//todo
        //"M:STONE;D:NORTH"
        String material = s.split("M:")[1].split(";D:")[0];
        String face = s.split("M:")[1].split(";D:")[1];

        BlockFace blockFace = BlockFace.NORTH;
        try {
            blockFace = BlockFace.valueOf(face);
        } catch (IllegalArgumentException ignore) {
        }

        return new TerrainBlockData(Material.getMaterial(material), blockFace);
    }

    public static String toString(TerrainBlockData is) {//todo
        return "M:" + is.getMaterial().toString() +
                ";D:" + is.getBlockFace().toString();
    }

    public static Material toMaterial(String s) {
        return Material.getMaterial(s);
    }

    public static String toString(Material material) {
        return material.toString();
    }

    public static TreeType toTreeType(String s) {
        return TreeType.valueOf(s);
    }

    public static String toString(TreeType tree) {
        return tree.toString();
    }

    @SuppressWarnings({"unused", "deprecation"})
    public static void addData(LinkedList<String> data, Player player, HashMap<TerrainBlockData, Integer> genModeData) {
        //m=GRASS,1 m=dirt,3
        if (data != null) {
            if (data.size() > 0) {
                for (String s : data) {
                    String ss = s.toLowerCase();

                    try {
                        String tmp = ss.split("m=")[1].split(",")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,1'");
                        continue;
                    }

                    String mS = ss.split("m=")[1].split(",")[0];
                    Material m = Material.getMaterial(mS);
                    if (m == null) {
                        player.sendMessage(ChatColor.RED + mS + " is not a valid Material");
                        continue;
                    }


                    TerrainBlockData is = new TerrainBlockData(m);//todo add directional
                    try {
                        int i = Integer.parseInt(ss.split("m=")[1].split(",")[1]);
                        if (genModeData.containsKey(is)) {
                            genModeData.put(is, i);
                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully edited: " + s);
                        } else {
                            genModeData.put(is, i);
                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully added: " + s);
                        }

                    } catch (NumberFormatException nfe) {
                        player.sendMessage(ChatColor.RED + ss.split("m=")[1].split(",")[1] + " is not a valid number");
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }

    @SuppressWarnings({"unused", "deprecation"})
    public static void removeData(LinkedList<String> data, Player player, HashMap<TerrainBlockData, Integer> genModeData) {
        //m=GRASS m=dirt
        if (data != null) {
            if (data.size() > 0) {
                for (String s : data) {
                    String ss = s.toLowerCase();

                    try {
                        String tmp = ss.split("m=")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS'");
                        continue;
                    }

                    String mS = ss.split("m=")[1];
                    Material m = Material.getMaterial(mS);
                    if (m == null) {
                        player.sendMessage(ChatColor.RED + mS + " is not a valid Material. Make sure that your given data is in the right format 'm=GRASS'");
                        continue;
                    }

                    TerrainBlockData is = new TerrainBlockData(m);//todo
                    genModeData.remove(is);
                    player.sendMessage(ChatColor.DARK_AQUA + "Successfully removed: " + s);

                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }

    @SuppressWarnings({"unused", "deprecation", "unchecked"})
    public static void setData(LinkedList<String> data, int number, Player player, TerrainMode.MapBased mode) {
        //3 m=GRASS,1

        if (data != null) {
            if (data.size() > 1) {
                String ss = data.get(1).toLowerCase();

                try {
                    String tmp = ss.split("m=")[1].split(",")[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,1'");
                    return;
                }

                String mS = ss.split("m=")[1].split(",")[0].toUpperCase();
                Material m = Material.matchMaterial(mS);
                if (m == null) {
                    player.sendMessage(ChatColor.RED + mS + " is not a valid Material. Make sure that your given data is in the right format 'm=GRASS,1'");
                    return;
                }

                TerrainBlockData is = new TerrainBlockData(m);//todo
                int i;
                try {
                    i = Integer.parseInt(ss.split("m=")[1].split(",")[1]);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED + ss.split("m=")[1].split(",")[1] + " is not a valid number");
                    return;
                }

                mode.getModeData().remove(is);

                if (number > mode.getModeData().size() + 1) {
                    player.sendMessage(ChatColor.RED + "Your given place must be lower than the amount of data in the TerrainMode " + mode.getModeName() + " (" + (mode.getModeData().size() + 1) + ")");
                    return;
                }

                LinkedHashMap<TerrainBlockData, Integer> newMap = new LinkedHashMap<>();

                int tmp = 1;
                boolean b = true;
                for (Object iss : mode.getModeData().keySet()) {
                    if (tmp == number) {
                        newMap.put(is, i);
                        b = false;
                    }
                    newMap.put((TerrainBlockData) iss, (Integer) mode.getModeData().get(iss));
                    tmp++;
                }
                if (b) {
                    newMap.put(is, i);
                }

                mode.setModeData(newMap);

                player.sendMessage(ChatColor.DARK_AQUA + "Successfully set TerrainMode data to place " + number);


            }
        }
    }
}
