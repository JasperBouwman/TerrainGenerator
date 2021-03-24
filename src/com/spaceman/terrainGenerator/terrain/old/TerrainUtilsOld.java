package com.spaceman.terrainGenerator.terrain.old;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.MapMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.TerrainUtils.toTerrainBlockData;

@Deprecated
@SuppressWarnings({"WeakerAccess", "unchecked", "unused"})
public class TerrainUtilsOld {

    private TerrainUtilsOld() {
    }

    //map<TerrainBlockData, Integer>
    public static void saveMapTerrainBlockDataInteger(ConfigurationSection section, HashMap<TerrainBlockData, Integer> modeData) {
        if (modeData == null) {
            return;
        }
        for (TerrainBlockData is : modeData.keySet()) {
            section.set("data." + TerrainUtils.toString(is), modeData.get(is));
        }
    }
    public static MapMode getMapTerrainBlockDataInteger(ConfigurationSection section, MapMode mode) {
        LinkedHashMap<TerrainBlockData, Integer> data = new LinkedHashMap<>();
        if (section.contains("data")) {
            for (String s : section.getConfigurationSection("data").getKeys(false)) {
                TerrainBlockData is = toTerrainBlockData(s);
                data.put(is, section.getInt("data." + s));
            }
        }

        mode.setModeData(data);
        return mode;
    }
    public static void addDataTerrainBlockDataInteger(LinkedList<String> data, Player player, HashMap<TerrainBlockData, Integer> genModeData) {
        //m=GRASS,d=NORTH,1

        //m=DIRT,1
        //m=DIRT,w=TRUE,1
        //m=DIRT,d=EAST,1
        //m=DIRT,d=EAST,w=TRUE,1

        if (data != null) {
            if (data.size() > 0) {
                label:
                for (String s : data) {
                    String ss = s.toLowerCase();

                    try {
                        String tmp = ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,d=NORTH,1'");
                        continue;
                    }

                    String mS = ss.split("[Mm]=")[1].split(",[Dd]=")[0];
                    Material m = Material.getMaterial(mS.toUpperCase());
                    if (m == null) {
                        player.sendMessage(ChatColor.RED + mS + " is not a valid Material");
                        continue;
                    }

                    String dS = ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[0].toUpperCase();
                    BlockFace blockFace;
                    try {
                        blockFace = BlockFace.valueOf(dS);
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Direction " + dS + " does not exist");
                        continue;
                    }

                    TerrainBlockData is = new TerrainBlockData(m, blockFace, false);
                    try {
                        int i = Integer.parseInt(ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[1]);

                        for (TerrainBlockData tmpBlockData : genModeData.keySet()) {
                            if (tmpBlockData.equals(is)) {
                                genModeData.remove(tmpBlockData);
                                genModeData.put(is, i);
                                player.sendMessage(ChatColor.DARK_AQUA + "Successfully edited: " + s);
                                continue label;
                            }
                        }
                        genModeData.put(is, i);
                        player.sendMessage(ChatColor.DARK_AQUA + "Successfully added: " + s);

//                        if (genModeData.containsKey(is)) {
//                            genModeData.put(is, i);
//                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully edited: " + s);
//                        } else {
//                            genModeData.put(is, i);
//                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully added: " + s);
//                        }

                    } catch (NumberFormatException nfe) {
                        player.sendMessage(ChatColor.RED + ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[1] + " is not a valid number");
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }
    public static void removeDataTerrainBlockDataInteger(LinkedList<String> data, Player player, HashMap<TerrainBlockData, Integer> genModeData) {
        //m=GRASS,d=NORTH
        if (data != null) {
            if (data.size() > 0) {
                label:
                for (String s : data) {
                    String ss = s.toLowerCase();

                    try {
                        String tmp = ss.split("[Mm]=")[1].split(",[Dd]=")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,d=NORTH'");
                        continue;
                    }

                    String mS = ss.split("[Mm]=")[1].split(",[Dd]=")[0];
                    Material m = Material.getMaterial(mS.toUpperCase());
                    if (m == null) {
                        player.sendMessage(ChatColor.RED + mS + " is not a valid Material");
                        continue;
                    }

                    String dS = ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[0].toUpperCase();
                    BlockFace blockFace;
                    try {
                        blockFace = BlockFace.valueOf(dS);
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Direction " + dS + " does not exist");
                        continue;
                    }

                    TerrainBlockData is = new TerrainBlockData(m);
                    is.setBlockFace(blockFace);

                    for (TerrainBlockData tmpBlockData : genModeData.keySet()) {
                        if (tmpBlockData.equals(is)) {
                            genModeData.remove(tmpBlockData);
                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully removed: " + is.toString());
                            continue label;
                        }
                    }
                    player.sendMessage(ChatColor.DARK_RED + is.toString() + ChatColor.RED + " was not in this TerrainMode");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }
    public static void setDataTerrainBlockDataInteger(LinkedList<String> data, int number, Player player, MapMode mode) {
        //m=GRASS,d=NORTH,1

        if (data != null) {
            if (data.size() > 0) {
                String ss = data.get(0).toLowerCase();

                try {
                    String tmp = ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,d=NORTH,1'");
                    return;
                }

                String mS = ss.split("[Mm]=")[1].split(",[Dd]=")[0];
                Material m = Material.getMaterial(mS.toUpperCase());
                if (m == null) {
                    player.sendMessage(ChatColor.RED + mS + " is not a valid Material");
                    return;
                }

                String dS = ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[0].toUpperCase();
                BlockFace blockFace;
                try {
                    blockFace = BlockFace.valueOf(dS);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "Direction " + dS + " does not exist");
                    return;
                }

                int i;
                try {
                    i = Integer.parseInt(ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[1]);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED + ss.split("[Mm]=")[1].split(",[Dd]=]")[1] + " is not a valid number");
                    return;
                }

                TerrainBlockData newTerrainBlockData = new TerrainBlockData(m, blockFace, false);

                boolean oldData = false;
                for (Object tmpBlockData : mode.getModeData().keySet()) {
                    if (newTerrainBlockData.equals(tmpBlockData)) {
                        oldData = true;
                        mode.getModeData().remove(tmpBlockData);
                        break;
                    }
                }

                if (number > mode.getModeData().size() + 1) {
                    player.sendMessage(ChatColor.RED + "Your given place must be lower than the amount of data in the TerrainMode " + mode.getModeName() + " (" + (mode.getModeData().size() + 1) + ")");
                    return;
                } else if (number < 1) {
                    player.sendMessage(ChatColor.RED + "Your given place must be higher than the 0");
                    return;
                }

                LinkedHashMap<TerrainBlockData, Integer> newMap = new LinkedHashMap<>();

                int tmp = 1;
                boolean b = true;
                for (Object iss : mode.getModeData().keySet()) {
                    if (tmp == number) {
                        newMap.put(newTerrainBlockData, i);
                        b = false;
                    }
                    newMap.put((TerrainBlockData) iss, (Integer) mode.getModeData().get(iss));
                    tmp++;
                }
                if (b) {
                    newMap.put(newTerrainBlockData, i);
                }

                mode.setModeData(newMap);

                player.sendMessage(ChatColor.DARK_AQUA + "Successfully set " + (oldData ? "old" : "new") + " TerrainMode data to place " + number);

            }
        }
    }
    public static Collection<String> tabListCreateAndSetMapTerrainBlockDataInteger(String[] args) {
        //terrain mode add <name> <name> [data]
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //terrain mode edit <name> <TerrainMode name> set <number> <data...>
        //format: m=MATERIAL,d=DIRECTION,INTEGER_VALUE

        if (args[0].equalsIgnoreCase("mode") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("set") || (args[1].equalsIgnoreCase("edit") && args[4].equalsIgnoreCase("set")))) {
            int arg = 4;
            arg += args.length - 5;
            if (args[1].equalsIgnoreCase("set")) {
                arg = 5;
                arg += args.length - 6;
            }
            if ((args[1].equalsIgnoreCase("edit") && args[4].equalsIgnoreCase("set"))) {
                arg = 6;
            }
            if (args.length == arg + 1) {
                if (args[arg].equals("")) {
                    return Collections.singletonList("m=");
                } else if (args[arg].equalsIgnoreCase("m")) {
                    return Collections.singletonList("m=");
                } else if (args[arg].toLowerCase().startsWith("m=")) {

                    if (Material.getMaterial(args[arg].replaceAll("[Mm]=", "").split(",")[0].toUpperCase()) != null) {
                        if (!args[arg].toLowerCase().endsWith(",d=") && !args[arg].toLowerCase().contains(",d=")) {
                            return Collections.singletonList(args[arg].split(",")[0] + ",d=");
                        }
                        if (Pattern.compile("m=[a-z_]+,d=.*+").matcher(args[arg].toLowerCase()).matches()) { //m=MATERIAL,d=
                            if (Pattern.compile("m=[a-z_]+,[dD]=(north|east|south|west|up|down)").matcher(args[arg].toLowerCase()).matches()) {
                                return Collections.singletonList(args[arg] + ",");
                            } else if (Pattern.compile("m=[a-z_]+,[dD]=(north|east|south|west|up|down),(-)?+([0-9])*+").matcher(args[arg].toLowerCase()).matches()) {
                                return Arrays.asList(args[arg] + "0", args[arg] + "1", args[arg] + "2", args[arg] + "3", args[arg] + "4", args[arg] + "5", args[arg] + "6", args[arg] + "7", args[arg] + "8", args[arg] + "9");
                            }

                            return Arrays.asList(
                                    args[arg].split(",[Dd]=")[0] + ",d=NORTH",
                                    args[arg].split(",[Dd]=")[0] + ",d=EAST",
                                    args[arg].split(",[Dd]=")[0] + ",d=SOUTH",
                                    args[arg].split(",[Dd]=")[0] + ",d=WEST",
                                    args[arg].split(",[Dd]=")[0] + ",d=UP",
                                    args[arg].split(",[Dd]=")[0] + ",d=DOWN");
                        }
                    }

                    ArrayList<String> newList = new ArrayList<>();
                    for (Material m : TerrainUtils.blockMaterials) {
                        newList.add("m=" + m.name());
                    }
                    return newList;
                }
            }
        }
        return Collections.emptyList();
    }
    public static Collection<String> tabListAddMapTerrainBlockDataInteger(String[] args, HashMap<TerrainBlockData, Integer> genModeData) {
        //terrain mode edit <name> <TerrainMode name> add <data...>

        if (args[0].equalsIgnoreCase("mode") && args[1].equalsIgnoreCase("edit") && args[4].equalsIgnoreCase("add")) {
            int arg = args.length - 1;

            if (args.length == arg + 1) {
                if (args[arg].equals("")) {
                    return Collections.singletonList("m=");
                } else if (args[arg].equalsIgnoreCase("m")) {
                    return Collections.singletonList("m=");
                } else if (args[arg].toLowerCase().startsWith("m=")) {

                    HashMap<Material, Integer> amountList = new HashMap<>();
                    ArrayList<String> blackList = new ArrayList<>();
                    for (TerrainBlockData m : genModeData.keySet()) {
                        amountList.put(m.getMaterial(), amountList.getOrDefault(m.getMaterial(), 0) + 1);
                        if (amountList.getOrDefault(m.getMaterial(), 0) == 4) {
                            blackList.add(m.getMaterial().name());
                        }
                    }

                    ArrayList<String> newList = new ArrayList<>();
                    for (Material m : TerrainUtils.blockMaterials) {
                        if (!blackList.contains(m.name())) {
                            newList.add("m=" + m.name());
                        }
                    }

                    Material testMaterial = Material.getMaterial(args[arg].replaceAll("[Mm]=", "").split(",")[0].toUpperCase());

                    if (testMaterial != null && newList.contains("m=" + testMaterial.name())) {
                        if (!args[arg].toLowerCase().endsWith(",d=") && !args[arg].toLowerCase().contains(",d=")) {
                            return Collections.singletonList(args[arg].split(",")[0] + ",d=");
                        }
                        if (Pattern.compile("m=[a-z_]+,d=.*+").matcher(args[arg].toLowerCase()).matches()) { //m=MATERIAL,d=

                            String acceptedFaces = "north|east|south|west|up|down";
                            for (TerrainBlockData terrainBlockData : genModeData.keySet()) {
                                if (terrainBlockData.getMaterial().equals(testMaterial)) {
                                    acceptedFaces = acceptedFaces.replaceAll("\\|?+" + terrainBlockData.getBlockFace().name().toLowerCase(), "");
                                }
                            }
                            if (acceptedFaces.startsWith("|")) {
                                acceptedFaces = acceptedFaces.replaceFirst("\\|", "");
                            }

                            if (Pattern.compile("m=[a-z_]+,[dD]=(" + acceptedFaces + ")").matcher(args[arg].toLowerCase()).matches()) {
                                return Collections.singletonList(args[arg] + ",");
                            } else if (Pattern.compile("m=[a-z_]+,[dD]=(" + acceptedFaces + "),(-)?+([0-9])*+").matcher(args[arg].toLowerCase()).matches()) {
                                return Arrays.asList(args[arg] + "0", args[arg] + "1", args[arg] + "2", args[arg] + "3", args[arg] + "4", args[arg] + "5", args[arg] + "6", args[arg] + "7", args[arg] + "8", args[arg] + "9");
                            }

                            ArrayList<String> returnList = new ArrayList<>();

                            for (String faces : acceptedFaces.split("\\|")) {
                                returnList.add(args[arg].split(",[Dd]=")[0] + ",d=" + faces.toUpperCase());
                            }
                            return returnList;
                        }
                    }
                    return newList;
                }
            }
        }
        return Collections.emptyList();
    }
    public static Collection<String> tabListRemoveMapTerrainBlockDataInteger(String[] args, HashMap<TerrainBlockData, Integer> genModeData) {
        //terrain mode edit <name> <TerrainMode name> remove <data...>
        //format: m=MATERIAL,d=DIRECTION

        if (args[0].equalsIgnoreCase("mode") && args[1].equalsIgnoreCase("edit") && args[4].equalsIgnoreCase("remove")) {
            int arg = args.length - 1;

            if (args[arg].equals("")) {
                return Collections.singletonList("m=");
            } else if (args[arg].equalsIgnoreCase("m")) {
                return Collections.singletonList("m=");
            } else if (args[arg].toLowerCase().startsWith("m=")) {

                Material testMaterial = Material.getMaterial(args[arg].replaceAll("[Mm]=", "").split(",")[0].toUpperCase());

                if (testMaterial != null) {
                    boolean returnBoolean = true;
                    for (TerrainBlockData terrainBlockData : genModeData.keySet()) {
                        if (terrainBlockData.getMaterial().equals(testMaterial)) {
                            returnBoolean = false;
                        }
                    }
                    if (returnBoolean) return Collections.emptyList();

                    if (!args[arg].toLowerCase().endsWith(",d=") && !args[arg].toLowerCase().contains(",d=")) {
                        return Collections.singletonList(args[arg].split(",")[0] + ",d=");
                    }
                    if (Pattern.compile("m=[a-z_]+,d=.*+").matcher(args[arg].toLowerCase()).matches()) { //m=MATERIAL,d=
                        Material currentMaterial = Material.getMaterial(args[arg].toLowerCase().split("[Mm]=")[1].split(",[Dd]=.*+")[0].toUpperCase());
                        ArrayList<String> list = new ArrayList<>();
                        String prefix = args[arg].split(",[Dd]=")[0] + ",d=";
                        for (TerrainBlockData terrainBlockData : genModeData.keySet()) {
                            if (terrainBlockData.getMaterial().equals(currentMaterial)) {
                                list.add(prefix + terrainBlockData.getBlockFace().name());
                            }
                        }
                        return list;
                    }
                }

                Set<TerrainBlockData> materialList = genModeData.keySet();
                ArrayList<String> newList = new ArrayList<>();
                for (TerrainBlockData m : materialList) {
                    newList.add("m=" + m.getMaterial().name());
                }
                return newList;
            }

        }
        return Collections.emptyList();
    }
    //map<TerrainBlockData, Integer>

    //map<TerrainBlockData, ?>
    public static void setWaterLoggedMapTerrainBlockDataObject(LinkedList<String> data, Player player, Set<TerrainBlockData> genModeData) {
        //m=GRASS,d=NORTH,true
        if (data != null) {
            if (data.size() > 0) {
                label:
                for (String s : data) {
                    String ss = s.toLowerCase();

                    try {
                        String tmp = ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        player.sendMessage(ChatColor.RED + "Given data is not in the right format. Make sure that your given data is in the right format 'm=GRASS,d=NORTH,true'");
                        continue;
                    }

                    String mS = ss.split("[Mm]=")[1].split(",[Dd]=")[0];
                    Material m = Material.getMaterial(mS.toUpperCase());
                    if (m == null) {
                        player.sendMessage(ChatColor.RED + mS + " is not a valid Material");
                        continue;
                    }

                    String dS = ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[0].toUpperCase();
                    BlockFace blockFace;
                    try {
                        blockFace = BlockFace.valueOf(dS);
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Direction " + dS + " does not exist");
                        continue;
                    }

                    Boolean b = null;
                    if (!ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[1].equalsIgnoreCase("null")) {
                        b = Boolean.parseBoolean(ss.split("[Mm]=")[1].split(",[Dd]=")[1].split(",")[1]);
                    }

                    for (TerrainBlockData tmpBlockData : genModeData) {
                        if (tmpBlockData.equals(new TerrainBlockData(m, blockFace, false))) {
                            tmpBlockData.setWaterLogged(b);
                            player.sendMessage(ChatColor.DARK_AQUA + "Successfully set water logged: " + b);
                            continue label;
                        }
                    }
                    player.sendMessage(ChatColor.DARK_AQUA + "Given data was not in the TerrainMode: " + s);
                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }
    public static Collection<String> tabListWaterLogMapTerrainBlockDataObject(String[] args, Set<TerrainBlockData> genModeData) {
        //terrain mode waterLog <name> <TerrainMode name> set <data...>

        if (args[0].equalsIgnoreCase("mode") && args[1].equalsIgnoreCase("waterLog") && args[4].equalsIgnoreCase("set")) {
            int arg = args.length - 1;

            if (args.length == arg + 1) {
                if (args[arg].equals("")) {
                    return Collections.singletonList("m=");
                } else if (args[arg].equalsIgnoreCase("m")) {
                    return Collections.singletonList("m=");
                } else if (args[arg].toLowerCase().startsWith("m=")) {

                    Material testMaterial = Material.getMaterial(args[arg].replaceAll("[Mm]=", "").split(",")[0].toUpperCase());

                    if (testMaterial != null) {
                        boolean returnBoolean = true;
                        for (TerrainBlockData terrainBlockData : genModeData) {
                            if (terrainBlockData.getMaterial().equals(testMaterial)) {
                                returnBoolean = false;
                            }
                        }
                        if (returnBoolean) return Collections.emptyList();

                        if (!args[arg].toLowerCase().endsWith(",d=") && !args[arg].toLowerCase().contains(",d=")) {
                            return Collections.singletonList(args[arg].split(",")[0] + ",d=");
                        }
                        if (Pattern.compile("m=[a-z_]+,d=.*+").matcher(args[arg].toLowerCase()).matches()) { //m=MATERIAL,d=

                            StringBuilder acceptedFaces = new StringBuilder();
                            for (TerrainBlockData terrainBlockData : genModeData) {
                                if (terrainBlockData.getMaterial().equals(testMaterial)) {
//                                    acceptedFaces = acceptedFaces.replaceAll("\\|?+" + terrainBlockData.getBlockFace().name().toLowerCase(), "");
                                    if (!acceptedFaces.toString().contains(terrainBlockData.getBlockFace().name().toLowerCase())) {
                                        acceptedFaces.append("|").append(terrainBlockData.getBlockFace().name().toLowerCase());
                                    }
                                }
                            }
                            if (acceptedFaces.toString().startsWith("|")) {
                                acceptedFaces = new StringBuilder(acceptedFaces.toString().replaceFirst("\\|", ""));
                            }

                            if (Pattern.compile("m=[a-z_]+,[dD]=(" + acceptedFaces + ")").matcher(args[arg].toLowerCase()).matches()) {
//                                return Collections.singletonList(args[arg] + ",");
                                return Arrays.asList(args[arg] + ",false",
                                        args[arg] + ",true",
                                        args[arg] + ",null");
                            } else if (Pattern.compile("m=[a-z_]+,[dD]=(" + acceptedFaces + "),.*+").matcher(args[arg].toLowerCase()).matches()) {

                                String prefix = String.join(",", Arrays.asList(args[arg].split(",")).subList(0, 2)) + ",";
                                return Arrays.asList(
                                        prefix + "false",
                                        prefix + "true",
                                        prefix + "null");
                            }
                            Material currentMaterial = Material.getMaterial(args[arg].toLowerCase().split("[Mm]=")[1].split(",[Dd]=.*+")[0].toUpperCase());
                            ArrayList<String> list = new ArrayList<>();
                            String prefix = args[arg].split(",[Dd]=")[0] + ",d=";
                            for (TerrainBlockData terrainBlockData : genModeData) {
                                if (terrainBlockData.getMaterial().equals(currentMaterial)) {
                                    list.add(prefix + terrainBlockData.getBlockFace().name());
                                }
                            }
                            return list;
                        }
                    }

                    ArrayList<String> newList = new ArrayList<>();
                    for (TerrainBlockData m : genModeData) {
                        newList.add("m=" + m.getMaterial().name());
                    }
                    return newList;
                }
            }
        }
        return Collections.emptyList();
    }
    //map<TerrainBlockData, ?>

    //Integer
    public static void saveDataInteger(ConfigurationSection section, Integer modeData) {
        if (modeData != null) {
            section.set("data", modeData);
        }
    }
    public static DataMode getDataInteger(ConfigurationSection section, DataMode templateMode) {
        int data = section.getInt("data");
        templateMode.setModeData(data);
        return templateMode;
    }
    public static void setDataInteger(LinkedList<String> data, Player player, DataMode mode) {
        if (data != null) {
            if (data.size() >= 1) {
                try {
                    int i = Integer.parseInt(data.get(0));
                    mode.setModeData(i);
                    player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + mode.getModeName() + " is now set to " + i);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED + "Given data is not a valid number");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }
    //Integer

    //String
    public static void saveDataString(ConfigurationSection section, String modeData) {
        if (modeData != null) {
            section.set("data", modeData);
        }
    }
    public static DataMode getDataString(ConfigurationSection section, DataMode templateMode) {
        String data = section.getString("data");
        templateMode.setModeData(data);
        return templateMode;
    }
    public static void setDataString(LinkedList<String> data, Player player, DataMode mode) {
        if (data != null) {
            if (data.size() >= 1) {
                String s = data.get(0);
                mode.setModeData(s);
                player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + mode.getModeName() + " is now set to " + s);
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }
    public static Collection<String> tabListCreateAndSetTerrainGenerator(String[] args) {
        //terrain mode add <name> <name> [data]
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //terrain mode edit <name> <TerrainMode name> set <data...>
        if (args[0].equals("mode") && (
                args[1].equals("add") && args.length == 5 ||
                        args[1].equals("set") && args.length == 6 ||
                        args[1].equals("edit") && args[4].equals("set") && args.length == 6)) {
            return terrainGenData.keySet();
        }
        return Collections.emptyList();
    }
    //String

    //Boolean
    public static void saveDataBoolean(ConfigurationSection section, Boolean modeData) {
        if (modeData != null) {
            section.set("data", modeData);
        }
    }
    public static DataMode<Boolean> getDataBoolean(ConfigurationSection section, DataMode templateMode) {
        boolean data = section.getBoolean("data");
        templateMode.setModeData(data);
        return templateMode;
    }
    public static void setDataBoolean(LinkedList<String> data, Player player, DataMode mode) {
        if (data != null) {
            if (data.size() > 0) {
                boolean b = Boolean.parseBoolean(data.get(0));
                mode.setModeData(b);
                player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + mode.getModeName() + " is now set to " + b);
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }
    public static Collection<String> tabListCreateAndSetBoolean(String[] args) {
        //terrain mode add <name> <name> [data]
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //terrain mode edit <name> <TerrainMode name> set <data...>
        if (args[0].equals("mode") && (
                args[1].equals("add") && args.length == 5 ||
                        args[1].equals("set") && args.length == 6 ||
                        args[1].equals("edit") && args[4].equals("set") && args.length == 6)) {
            return Arrays.asList("true", "false");
        }
        return new ArrayList<>();
    }
    //Boolean

    //TerrainBlockData
    public static void saveDataTerrainBlockData(ConfigurationSection section, TerrainBlockData modeData) {
        if (modeData != null) {
            section.set("data", modeData);
        }
    }
    public static DataMode getDataTerrainBlockData(ConfigurationSection section, DataMode templateMode) {
        TerrainBlockData data = (TerrainBlockData) section.get("data");
        templateMode.setModeData(data);
        return templateMode;
    }
    public static void setDataTerrainBlockData(LinkedList<String> data, Player player, DataMode mode) {
        if (data != null) {
            if (data.size() >= 1) {
                String s = data.get(0);
                TerrainBlockData terrainBlockData;
                try {
                    terrainBlockData = TerrainBlockData.fromString(s);
                } catch (IllegalArgumentException iae) {
                    player.sendMessage(iae.getMessage());
                    return;
                }
                if (terrainBlockData == null) {
                    Message message = new Message();
                    message.addText(TextComponent.textComponent("Given value is not valid, use the following format '", ChatColor.RED));
                    message.addText(TextComponent.textComponent("m=STONE", ChatColor.DARK_RED));
                    message.addText(TextComponent.textComponent("' or '", ChatColor.RED));
                    message.addText(TextComponent.textComponent("m=DIRT,d=WEST", ChatColor.DARK_RED));
                    message.addText(TextComponent.textComponent("'.", ChatColor.RED));
                    message.sendMessage(player);
                    return;
                }

                mode.setModeData(terrainBlockData);
                player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + mode.getModeName() + " is now set to " + terrainBlockData.toString());
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }
    public static Collection<String> tabListCreateAndSetTerrainBlockData(String[] args) {
        //terrain mode add <name> <name> [data]
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //terrain mode edit <name> <TerrainMode name> set <data...>
        if (args[0].equalsIgnoreCase("mode") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("set") || (args[1].equalsIgnoreCase("edit") && args[4].equalsIgnoreCase("set")))) {
            int arg = 4;
            if (args[1].equalsIgnoreCase("set") || (args[1].equalsIgnoreCase("edit") && args[4].equalsIgnoreCase("set"))) {
                arg = 5;
            }
            if (args.length == arg + 1) {
                if (args[arg].equals("")) {
                    return Collections.singletonList("m=");
                } else if (args[arg].equalsIgnoreCase("m")) {
                    return Collections.singletonList("m=");
                } else if (args[arg].toLowerCase().startsWith("m=")) {

                    if (Material.getMaterial(args[arg].replaceAll("[Mm]=", "").split(",")[0].toUpperCase()) != null) {
                        if (!args[arg].toLowerCase().endsWith(",d=") && !args[arg].toLowerCase().contains(",d=")) {
                            return Collections.singletonList(args[arg].split(",")[0] + ",d=");
                        }
//                        if (args[arg].replace("m=", "").split(",")[1].startsWith("d=")) {
                        if (Pattern.compile("m=[a-z_]+,d=.*+").matcher(args[arg].toLowerCase()).matches()) { //m=MATERIAL,d=
                            return Arrays.asList(
                                    args[arg].split(",[Dd]=")[0] + ",d=NORTH",
                                    args[arg].split(",[Dd]=")[0] + ",d=EAST",
                                    args[arg].split(",[Dd]=")[0] + ",d=SOUTH",
                                    args[arg].split(",[Dd]=")[0] + ",d=WEST",
                                    args[arg].split(",[Dd]=")[0] + ",d=UP",
                                    args[arg].split(",[Dd]=")[0] + ",d=DOWN");
                        }
                    }

                    ArrayList<String> newList = new ArrayList<>();
                    for (Material m : TerrainUtils.blockMaterials) {
                        newList.add("m=" + m.name());
                    }
                    return newList;
                }
            }
        }
        return Collections.emptyList();
    }
    public static void setWaterLoggedTerrainBlockData(LinkedList<String> data, Player player, TerrainBlockData terrainBlockData) {
        //true
        if (data != null) {
            if (data.size() > 0) {
                Boolean b = null;
                if (!data.get(0).equalsIgnoreCase("null")) {
                    b = Boolean.parseBoolean(data.get(0));
                }
                terrainBlockData.setWaterLogged(b);
                player.sendMessage(ChatColor.DARK_AQUA + "Successfully set water logged: " + (b == null ? "null" : b));
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }
    public static Collection<String> tabListWaterLogTerrainBlockData(String[] args, TerrainBlockData terrainBlockData) {
        //terrain mode waterLog <name> <TerrainMode name> set <data...>
        return Arrays.asList("true", "false", "null");
    }
    //TerrainBlockData
}
