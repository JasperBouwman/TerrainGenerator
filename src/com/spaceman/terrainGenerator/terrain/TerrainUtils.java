package com.spaceman.terrainGenerator.terrain;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.Pair;
import com.spaceman.terrainGenerator.keyValueHelper.ExtendedKey;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueError;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueHelper;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueTabArgument;
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.MapMode;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;

@SuppressWarnings({"unused"})
public class TerrainUtils {

    private TerrainUtils() {
    }

    public static List<Material> blockMaterials = Arrays.stream(Material.values()).filter(Material::isBlock).collect(Collectors.toList());

    public static TerrainBlockData toTerrainBlockData(String s) {
        //"M:STONE;D:NORTH;W:false"
        String material = s.split("M:")[1].split(";D:")[0];
        String face = s.split(";D:")[1].split(";W:")[0];
        Boolean waterLogged = null;
        if (s.contains(";W:")) {
            waterLogged = Main.parseBoolean(s.split(";W:")[1]);
        }

        BlockFace blockFace = BlockFace.NORTH;
        try {
            blockFace = BlockFace.valueOf(face);
        } catch (IllegalArgumentException ignore) {
        }

        return new TerrainBlockData(Material.getMaterial(material), blockFace, waterLogged);
    }
    public static String toString(TerrainBlockData is) {
        return "M:" + is.getMaterial().toString() +
                ";D:" + is.getBlockFace().toString() +
                (is.isWaterLogged() == null ? "" : ";W:" + is.isWaterLogged());
    }

    public static TreeType toTreeType(String s) {
        return TreeType.valueOf(s);
    }
    public static String toString(TreeType tree) {
        return tree.toString();
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
    public static MapMode<TerrainBlockData, Integer> getMapTerrainBlockDataInteger(ConfigurationSection section, MapMode<TerrainBlockData, Integer> mode) {
        LinkedHashMap<TerrainBlockData, Integer> data = new LinkedHashMap<>();
        if (section.contains("data")) {
            //noinspection ConstantConditions
            for (String s : section.getConfigurationSection("data").getKeys(false)) {
                TerrainBlockData is = toTerrainBlockData(s);
                data.put(is, section.getInt("data." + s));
            }
        }

        mode.setModeData(data);
        return mode;
    }
    public static void addDataTerrainBlockDataInteger(LinkedList<String> data, Player player, String integerName, MapMode<TerrainBlockData, Integer> mode) {
        //m=DIRT,integerName=1
        //m=DIRT,w=TRUE,integerName=1
        //m=DIRT,d=EAST,integerName=1
        //m=DIRT,d=EAST,w=TRUE,integerName=1

        if (data != null) {
            if (data.size() > 0) {
                label:
                for (String arg : data) {
                    try {
                        HashMap<String, Object> map = KeyValueHelper.extendedConstructObject(arg, new TerrainBlockData(),
                                (ExtendedKey) new ExtendedKey("m", Material::matchMaterial, false, (o, v) -> ((TerrainBlockData) o).setMaterial((Material) v)).setErrorMessage(" is not a valid material"),
                                (ExtendedKey) new ExtendedKey("d", s -> BlockFace.valueOf(s.toUpperCase()), true, (o, v) -> ((TerrainBlockData) o).setBlockFace((BlockFace) v)).setErrorMessage(" is not a valid direction"),
                                (ExtendedKey) new ExtendedKey("w", Main::parseBoolean, true, (o, v) -> ((TerrainBlockData) o).setWaterLogged((Boolean) v)).setErrorMessage(" is not a valid water log state").setAcceptNullValue(true),
                                (ExtendedKey) new ExtendedKey(integerName, Integer::valueOf, false).setErrorMessage(" is not a valid integer")
                        );

                        TerrainBlockData terrainBlockData = (TerrainBlockData) map.get(null);
                        int i = (int) map.get(integerName);
                        
                        for (TerrainBlockData tmpBlockData : mode.getModeData().keySet()) {
                            if (tmpBlockData.equals(terrainBlockData)) {
                                mode.getModeData().remove(tmpBlockData);
                                mode.getModeData().put(terrainBlockData, i);
                                player.sendMessage(formatSuccess("Successfully edited %s to %s from TerrainMode %s", tmpBlockData.toString(), terrainBlockData.toString(), mode.getModeName()));
                                continue label;
                            }
                        }
                        mode.getModeData().put(terrainBlockData, i);
                        player.sendMessage(formatSuccess("Successfully added %s to TerrainMode %s", terrainBlockData.toString(), mode.getModeName()));
                    } catch (KeyValueError e) {
                        e.sendMessage(player);
                    }
                }
            } else{
                player.sendMessage(formatError("Missing data"));
            }
        }
    }
    public static void removeDataTerrainBlockDataInteger(LinkedList<String> data, Player player, MapMode<TerrainBlockData, Integer> mode) {
        //m=GRASS,d=NORTH
        if (data != null) {
            if (data.size() > 0) {
                label:
                for (String arg : data) {
                    try {
                        HashMap<String, Object> map = KeyValueHelper.extendedConstructObject(arg, new TerrainBlockData(),
                                (ExtendedKey) new ExtendedKey("m", Material::matchMaterial, false, (o, v) -> ((TerrainBlockData) o).setMaterial((Material) v)).setErrorMessage(" is not a valid material"),
                                (ExtendedKey) new ExtendedKey("d", s -> BlockFace.valueOf(s.toUpperCase()), false, (o, v) -> ((TerrainBlockData) o).setBlockFace((BlockFace) v)).setErrorMessage(" is not a valid direction")
                        );

                        TerrainBlockData terrainBlockData = (TerrainBlockData) map.get(null);

                        for (TerrainBlockData tmpBlockData : mode.getModeData().keySet()) {
                            if (tmpBlockData.equals(terrainBlockData)) {
                                mode.getModeData().remove(tmpBlockData);
                                player.sendMessage(formatSuccess("Successfully removed %s from TerrainMode %s", terrainBlockData.toString(), mode.getModeName()));
                                continue label;
                            }
                        }
                        player.sendMessage(formatError("%s is not in TerrainMode %s", terrainBlockData.toString(), mode.getModeName()));

                    } catch (KeyValueError e) {
                        e.sendMessage(player);
                    }
                }
            } else {
                player.sendMessage(formatError("Missing data"));
            }
        }
    }
    public static void setDataTerrainBlockDataInteger(LinkedList<String> data, int number, Player player, String integerName, MapMode<TerrainBlockData, Integer> mode) {
        //m=DIRT,integerName=1
        //m=DIRT,w=TRUE,integerName=1
        //m=DIRT,d=EAST,integerName=1
        //m=DIRT,d=EAST,w=TRUE,integerName=1

        if (data != null) {
            if (data.size() > 0) {
                try {
                    HashMap<String, Object> map = KeyValueHelper.extendedConstructObject(data.get(0), new TerrainBlockData(),
                            (ExtendedKey) new ExtendedKey("m", Material::matchMaterial, false, (o, v) -> ((TerrainBlockData) o).setMaterial((Material) v)).setErrorMessage(" is not a valid material"),
                            (ExtendedKey) new ExtendedKey("d", s -> BlockFace.valueOf(s.toUpperCase()), true, (o, v) -> ((TerrainBlockData) o).setBlockFace((BlockFace) v)).setErrorMessage(" is not a valid direction"),
                            (ExtendedKey) new ExtendedKey("w", Main::parseBoolean, true, (o, v) -> ((TerrainBlockData) o).setWaterLogged((Boolean) v)).setErrorMessage(" is not a valid water log state").setAcceptNullValue(true),
                            (ExtendedKey) new ExtendedKey(integerName, Integer::valueOf, false).setErrorMessage(" is not a valid integer")
                    );

                    TerrainBlockData terrainBlockData = (TerrainBlockData) map.get(null);
                    int i = (int) map.get(integerName);

                    boolean oldData = false;
                    for (TerrainBlockData tmpBlockData : mode.getModeData().keySet()) {
                        if (terrainBlockData.equals(tmpBlockData)) {
                            oldData = true;
                            mode.getModeData().remove(tmpBlockData);
                            break;
                        }
                    }

                    if (number > mode.getModeData().size() + 1) {
                        player.sendMessage(formatError("Your given place must be lower than the amount of data in the TerrainMode %s (%s)", mode.getModeName(), String.valueOf((mode.getModeData().size() + 1))));
                        return;
                    } else if (number < 1) {
                        player.sendMessage(formatError("Your given place must be higher than 0"));
                        return;
                    }

                    LinkedHashMap<TerrainBlockData, Integer> newMap = new LinkedHashMap<>();

                    int currNumber = 1;
                    boolean b = true;
                    for (TerrainBlockData iss : mode.getModeData().keySet()) {
                        if (currNumber == number) {
                            newMap.put(terrainBlockData, i);
                            b = false;
                        }
                        newMap.put(iss, mode.getModeData().get(iss));
                        currNumber++;
                    }
                    if (b) {
                        newMap.put(terrainBlockData, i);
                    }

                    mode.setModeData(newMap);

//                    player.sendMessage(ChatColor.DARK_AQUA + "Successfully set " + (oldData ? "old" : "new") + " TerrainMode data to place " + number);
                    player.sendMessage(formatSuccess("Successfully set %s %s to place %s in TerrainMode %s", (oldData ? "old" : "new"), terrainBlockData.toString(), String.valueOf(number), mode.getModeName()));
                } catch (KeyValueError e) {
                    e.sendMessage(player);
                }
            }
        }
    }
    public static Collection<String> tabListSetMapTerrainBlockDataInteger(String[] args, String integerName) {
        //terrain mode edit <name> <TerrainMode name> set <number> <data...>
        if (args.length == 7) {
            return KeyValueHelper.constructTab(args[args.length - 1], Arrays.asList(
                    new KeyValueTabArgument("m", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("d", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("w", Arrays.asList("false", "true", "null")),
                    new KeyValueTabArgument(integerName, Collections.singletonList("<X>"))));
        } else {
            return Collections.emptyList();
        }
    }
    public static Collection<String> tabListAddAndCreateMapTerrainBlockDataInteger(String[] args, String integerName) {
        //terrain mode edit <name> <TerrainMode name> add <data...>
        //terrain mode add <name> <name> [data...]
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        return KeyValueHelper.constructTab(args[args.length - 1], Arrays.asList(
                new KeyValueTabArgument("m", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                new KeyValueTabArgument("d", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                new KeyValueTabArgument("w", Arrays.asList("false", "true", "null")),
                new KeyValueTabArgument(integerName, Collections.singletonList("<X>"))));
    }
    @SuppressWarnings("unchecked")
    public static Collection<String> tabListRemoveMapTerrainBlockDataInteger(String[] args, MapMode<TerrainBlockData, Integer> mode) {
        //terrain mode edit <name> <TerrainMode name> remove <data...>
        //format: m=MATERIAL,d=DIRECTION
        return KeyValueHelper.constructTab(args[args.length - 1],
                Arrays.asList(
                        new KeyValueTabArgument("m", mode.getModeData(), ((o, writtenPairs) -> {
                            
                            for (Pair<String, String> keyValue : writtenPairs) {
                                if (keyValue.getLeft().equalsIgnoreCase("d")) {
                                    ArrayList<String> list = new ArrayList<>();
                                    String BlockFace = keyValue.getRight();
                                    
                                    for (TerrainBlockData blockData : ((HashMap<TerrainBlockData, Integer>) o).keySet()) {
                                        if (blockData.getBlockFace().name().equalsIgnoreCase(BlockFace)) {
                                            list.add(blockData.getMaterial().name());
                                        }
                                    }
                                    return list;
                                }
                            }
                            
                            ArrayList<String> list = new ArrayList<>();
                            for (TerrainBlockData blockData : ((HashMap<TerrainBlockData, Integer>) o).keySet()) {
                                list.add(blockData.getMaterial().name());
                            }
                            return list;
                        })),
                        new KeyValueTabArgument("d", mode.getModeData(), (o, writtenPairs) -> {
                            for (Pair<String, String> keyValue : writtenPairs) {
                                if (keyValue.getLeft().equalsIgnoreCase("m")) {
                                    ArrayList<String> list = new ArrayList<>();
                                    String material = keyValue.getRight();
                                    
                                    for (TerrainBlockData blockData : ((HashMap<TerrainBlockData, Integer>) o).keySet()) {
                                        if (blockData.getMaterial().name().equalsIgnoreCase(material)) {
                                            list.add(blockData.getBlockFace().name());
                                        }
                                    }
                                    return list;
                                }
                            }
                            ArrayList<String> list = new ArrayList<>();
                            for (TerrainBlockData blockData : ((HashMap<TerrainBlockData, Integer>) o).keySet()) {
                                list.add(blockData.getBlockFace().name());
                            }
                            return list;
                        })
                )
        );
    }
    //map<TerrainBlockData, Integer>

    //map<TerrainBlockData, ?>
    public static void setWaterLoggedMapTerrainBlockDataObject(LinkedList<String> data, Player player, Collection<TerrainBlockData> genModeData) {
        //m=GRASS,d=NORTH,w=true
        if (data != null) {
            if (data.size() > 0) {
                label:
                for (String arg : data) {
                    try {
                        HashMap<String, Object> map = KeyValueHelper.extendedConstructObject(arg, new TerrainBlockData(),
                                (ExtendedKey) new ExtendedKey("m", Material::matchMaterial, false, (o, v) -> ((TerrainBlockData) o).setMaterial((Material) v)).setErrorMessage(" is not a valid material"),
                                (ExtendedKey) new ExtendedKey("d", s -> BlockFace.valueOf(s.toUpperCase()), true, (o, v) -> ((TerrainBlockData) o).setBlockFace((BlockFace) v)).setErrorMessage(" is not a valid direction"),
                                (ExtendedKey) new ExtendedKey("w", Main::parseBoolean, false).setErrorMessage(" is not a valid water log state").setAcceptNullValue(true)
                        );

                        TerrainBlockData terrainBlockData = (TerrainBlockData) map.get(null);
                        Boolean b = (Boolean) map.get("w");

                        for (TerrainBlockData tmpBlockData : genModeData) {
                            if (tmpBlockData.equals(terrainBlockData)) {
                                tmpBlockData.setWaterLogged(b);
                                player.sendMessage(formatSuccess("Successfully set water logged: %s", String.valueOf(b)));
                                continue label;
                            }
                        }
                        player.sendMessage(formatSuccess("Given data was not in the TerrainMode: %s", arg));

                    } catch (KeyValueError e) {
                        e.sendMessage(player);
                    }
                }
            } else {
                player.sendMessage(formatError("Missing data"));
            }
        }
    }
    public static Collection<String> tabListWaterLogMapTerrainBlockDataObject(String[] args, Collection<TerrainBlockData> genModeData) {
        //terrain mode waterLog <name> <TerrainMode name> set <data...>
        return KeyValueHelper.constructTab(args[args.length - 1], Arrays.asList(
                new KeyValueTabArgument("m",genModeData.stream().map((TerrainBlockData::getMaterial)).map(Material::name).collect(Collectors.toList())),
                new KeyValueTabArgument("d", genModeData, (o, tabArguments) -> {
    
                    for (Pair<String, String> keyValue : tabArguments) {
                        if (keyValue.getLeft().equalsIgnoreCase("m")) {
                            String material = keyValue.getRight();
                            ArrayList<String> list = new ArrayList<>();
                            for (Object objBlockData : ((Collection) o)) {
                                TerrainBlockData terrainBlockData = (TerrainBlockData) objBlockData;
                                if (terrainBlockData.getMaterial().name().equalsIgnoreCase(material)) {
                                    list.add(terrainBlockData.getBlockFace().name());
                                }
                            }
                            return list;
                        }
                    }
                    return new ArrayList<>();
                }),
                new KeyValueTabArgument("", Arrays.asList("false", "true", "null"))));
    }
    //map<TerrainBlockData, ?>

    //Integer
    public static void saveDataInteger(ConfigurationSection section, Integer modeData) {
        if (modeData != null) {
            section.set("data", modeData);
        }
    }
    public static DataMode<Integer> getDataInteger(ConfigurationSection section, DataMode<Integer> mode) {
        int data = section.getInt("data");
        mode.setModeData(data);
        return mode;
    }
    public static void setDataInteger(LinkedList<String> data, Player player, DataMode<Integer> mode) {
        if (data != null) {
            if (data.size() >= 1) {
                try {
                    int i = Integer.parseInt(data.get(0));
                    mode.setModeData(i);
                    player.sendMessage(formatSuccess("TerrainMode %s is now set to %s", mode.getModeName(), String.valueOf(i)));
                } catch (NumberFormatException nfe) {
                    player.sendMessage(formatError("Given data is not a valid number"));
                }
            } else {
                player.sendMessage(formatError("Missing data"));
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
    public static DataMode<String> getDataString(ConfigurationSection section, DataMode<String> mode) {
        String data = section.getString("data");
        mode.setModeData(data);
        return mode;
    }
    public static void setDataString(LinkedList<String> data, Player player, DataMode<String> mode) {
        if (data != null) {
            if (data.size() >= 1) {
                String s = data.get(0);
                mode.setModeData(s);
                player.sendMessage(formatSuccess("TerrainMode %s is now set to %s", mode.getModeName(), s));
            } else {
                player.sendMessage(formatError("Missing data"));
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
    public static DataMode<Boolean> getDataBoolean(ConfigurationSection section, DataMode<Boolean> mode) {
        boolean data = section.getBoolean("data");
        mode.setModeData(data);
        return mode;
    }
    public static void setDataBoolean(LinkedList<String> data, Player player, DataMode<Boolean> mode) {
        if (data != null) {
            if (data.size() > 0) {
                boolean b = Boolean.parseBoolean(data.get(0));
                mode.setModeData(b);
                player.sendMessage(formatSuccess("TerrainMode %s is now set to %s", mode.getModeName(), String.valueOf(b)));
            } else {
                player.sendMessage(formatError("Missing data"));
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
    public static DataMode getDataTerrainBlockData(ConfigurationSection section, DataMode<TerrainBlockData> mode) {
        TerrainBlockData data = (TerrainBlockData) section.get("data");
        mode.setModeData(data);
        return mode;
    }
    public static void setDataTerrainBlockData(LinkedList<String> data, Player player, DataMode<TerrainBlockData> mode) {
        //terrain mode edit <name> <TerrainMode name> set <data...>
        //terrain mode add <name> <name> [data...]
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        if (data != null) {
            if (data.size() >= 1) {
                try {
                    HashMap<String, Object> map = KeyValueHelper.extendedConstructObject(data.get(0), new TerrainBlockData(),
                            (ExtendedKey) new ExtendedKey("m", Material::matchMaterial, false, (o, v) -> ((TerrainBlockData) o).setMaterial((Material) v)).setErrorMessage(" is not a valid material"),
                            (ExtendedKey) new ExtendedKey("d", s -> BlockFace.valueOf(s.toUpperCase()), true, (o, v) -> ((TerrainBlockData) o).setBlockFace((BlockFace) v)).setErrorMessage(" is not a valid direction"),
                            (ExtendedKey) new ExtendedKey("w", Main::parseBoolean, true, (o, v) -> ((TerrainBlockData) o).setWaterLogged((Boolean) v)).setErrorMessage(" is not a valid water log state").setAcceptNullValue(true)
                    );

                    mode.setModeData((TerrainBlockData) map.get(null));
                    player.sendMessage(formatSuccess("TerrainMode %s is now set to %s", mode.getModeName(), map.get(null).toString()));
                } catch (KeyValueError e) {
                    e.sendMessage(player);
                }
            } else {
                player.sendMessage(formatError("Missing data"));
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
                return KeyValueHelper.constructTab(args[args.length - 1], Arrays.asList(
                        new KeyValueTabArgument("m", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                        new KeyValueTabArgument("d", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                        new KeyValueTabArgument("w", Arrays.asList("false", "true", "null")))
                );
            }
        }
        return Collections.emptyList();
    }
    public static void setWaterLoggedTerrainBlockData(LinkedList<String> data, Player player, TerrainBlockData terrainBlockData) {
        //true
        if (data != null) {
            if (data.size() > 0) {
                Boolean b = Main.parseBoolean(data.get(0));
                terrainBlockData.setWaterLogged(b);
                player.sendMessage(formatSuccess("Successfully set water logged: %s", String.valueOf(b)));
            } else {
                player.sendMessage(formatError("Missing data"));
            }
        }
    }
    public static Collection<String> tabListWaterLogTerrainBlockData(String[] args) {
        //terrain mode waterLog <name> <TerrainMode name> set <data...>
        if (args.length == 6) {
            return Arrays.asList("true", "false", "null");
        } else {
            return Collections.emptyList();
        }
    }
    //TerrainBlockData
}
