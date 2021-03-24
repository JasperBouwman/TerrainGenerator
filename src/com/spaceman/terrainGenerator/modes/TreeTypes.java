package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.keyValueHelper.*;
import com.spaceman.terrainGenerator.terrain.terrainMode.MapMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatInfo;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.TerrainUtils.toTreeType;

@SuppressWarnings("unused, WeakerAccess")
public class TreeTypes extends MapMode<TreeType, TreeTypes.TreeData> {

    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        //terrain mode add <name> <name> [data]
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //format: tree=TREE,m=DIRT,d=EAST,w=FALSE,r=1

        return KeyValueHelper.constructTab(args[args.length - 1], Arrays.asList(
                new KeyValueTabArgument("tree", Arrays.stream(TreeType.values()).map(TreeType::name).collect(Collectors.toList())),
                new KeyValueTabArgument("m", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                new KeyValueTabArgument("d", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                new KeyValueTabArgument("w", Arrays.asList("false", "true", "null")),
                new KeyValueTabArgument("r", Collections.singletonList("<X>"))));
    }

    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        //terrain mode edit <name> <TerrainMode name> set <number> <data...>
        if (args.length == 7) {
            return KeyValueHelper.constructTab(args[args.length - 1], Arrays.asList(
                    new KeyValueTabArgument("tree", Arrays.stream(TreeType.values()).map(TreeType::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("m", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("d", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("w", Arrays.asList("false", "true", "null")),
                    new KeyValueTabArgument("r", Collections.singletonList("<X>"))));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<String> tabListAdd(String[] args, Player player) {
        return tabListCreate(args, player);
    }

    @Override
    public Collection<String> tabListRemove(String[] args, Player player) {
        return getModeData().keySet().stream().map(treeType -> "tree=" + treeType.name()).collect(Collectors.toList());
    }

    @Override
    public void saveMode(ConfigurationSection section) {
        if (getModeData() != null) {
            for (TreeType treeType : getModeData().keySet()) {
                section.set("data." + TerrainUtils.toString(treeType), getModeData().get(treeType));
            }
        }
    }

    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        LinkedHashMap<TreeType, TreeData> data = new LinkedHashMap<>();

        if (section.contains("data")) {
            //noinspection ConstantConditions
            for (String s : section.getConfigurationSection("data").getKeys(false)) {
                TreeType tree = toTreeType(s);
                Object o = section.get("data." + s);
                if (o instanceof Integer) {
                    //for the previous update
                    data.put(tree, new TreeData((Integer) o, new TerrainBlockData(Material.STRUCTURE_VOID)));
                } else {
                    data.put(tree, (TreeData) o);
                }
            }
        }
        this.setModeData(data);

        return this;
    }

    @Override
    public void addData(LinkedList<String> data, Player player) {
        //tree=TREE,1 / tree=ACACIA,d=EAST,w=FALSE,m=DIRT,1
        if (data != null) {
            if (data.size() > 0) {
                label:
                for (String arg : data) {
                    try {
                        HashMap<String, Object> map = KeyValueHelper.constructObject(arg,
                                new Key("tree", s -> TreeType.valueOf(s.toUpperCase()), false),
                                new Key("m", Material::matchMaterial, true),
                                new Key("d", s -> BlockFace.valueOf(s.toUpperCase()), true),
                                new Key("w", Main::parseBoolean, true),
                                new Key("r", Integer::valueOf, false)
                        );

                        TerrainBlockData terrainBlockData = new TerrainBlockData(Material.STRUCTURE_VOID);
                        TreeType treeType = (TreeType) map.get("tree");
                        if (map.containsKey("m")) {
                            terrainBlockData.setMaterial((Material) map.get("m"));
                        }
                        if (map.containsKey("d")) {
                            terrainBlockData.setBlockFace((BlockFace) map.get("d"));
                        }
                        if (map.containsKey("w")) {
                            if (map.get("w") != null) {
                                terrainBlockData.setWaterLogged((Boolean) map.get("w"));
                            }
                        }
                        int chance = (int) map.get("r");

                        for (TreeType tmpTree : getModeData().keySet()) {
                            if (tmpTree.equals(treeType)) {
                                getModeData().remove(tmpTree);
                                getModeData().put(treeType, new TreeData(chance, terrainBlockData));

                                Message message = new Message();
                                message.addText(textComponent("Successfully edited ", ColorFormatter.successColor));
                                message.addText(textComponent(treeType.name(), ColorFormatter.varSuccessColor,
                                        hoverEvent(getModeData().get(treeType).toStringSuccess())).setInsertion(getInsertion(treeType, getModeData().get(treeType))));
                                message.addText(textComponent(" from TerrainMode ", ColorFormatter.successColor));
                                message.addText(textComponent(getModeName(), ColorFormatter.varSuccessColor));
                                message.sendMessage(player);
                                continue label;
                            }
                        }
                        getModeData().put(treeType, new TreeData(chance, terrainBlockData));

                        Message message = new Message();
                        message.addText(textComponent("Successfully added ", ColorFormatter.successColor));
                        message.addText(textComponent(treeType.name(), ColorFormatter.varSuccessColor,
                                hoverEvent(getModeData().get(treeType).toStringSuccess())).setInsertion(getInsertion(treeType, getModeData().get(treeType))));
                        message.addText(textComponent(" to TerrainMode ", ColorFormatter.successColor));
                        message.addText(textComponent(getModeName(), ColorFormatter.varSuccessColor));
                        message.sendMessage(player);
                    } catch (KeyValueError e) {
                        e.sendMessage(player);
                    }
                }
            } else{
                player.sendMessage(formatError("Missing data"));
            }
        }
    }

    @Override
    public void removeData(LinkedList<String> data, Player player) {
        //tree=TREE
        if (data != null) {
            if (data.size() > 0) {
                for (String s : data) {
                    String ss = s.toLowerCase();

                    if (Pattern.compile("(?i:tree=).+").matcher(s).matches()) {
                        TreeType tree;
                        try {
                            tree = TreeType.valueOf(ss.split("(?i:tree=)")[1].toUpperCase());
                        } catch (IllegalArgumentException iag) {
                            player.sendMessage(formatError("TreeType %s does not exist", ss.split("(?i:tree=)")[1].toUpperCase()));
                            continue;
                        }
                        if (getModeData().containsKey(tree)) {
                            TreeData oldTreeData = getModeData().get(tree);

                            getModeData().remove(tree);
                            Message message = new Message();
                            message.addText(textComponent("Successfully removed: ", ColorFormatter.successColor));
                            message.addText(textComponent(tree.name(), ColorFormatter.varSuccessColor,
                                    hoverEvent(oldTreeData.toStringSuccess())).setInsertion(getInsertion(tree, oldTreeData)));
                            message.sendMessage(player);
                        } else {
                            player.sendMessage(formatError("Given data is not in the TerrainMode %s", getModeName()));
                        }
                    } else {
                        player.sendMessage(formatError("Given data is not in the right format. Make sure that your given data is in the right format 'tree=TREE'"));
                    }
                }
            } else {
                player.sendMessage(formatError("Missing data"));
            }
        }
    }

    @Override
    public void setData(LinkedList<String> data, int number, Player player) {
        //2 tree=TREE,1 / 3 tree=TREE,m=GRASS,2

        if (data != null) {
            if (data.size() > 0) {
                try {
                    HashMap<String, Object> map = KeyValueHelper.constructObject(data.get(0),
                            new Key("tree", s -> TreeType.valueOf(s.toUpperCase()), false),
                            new Key("m", Material::matchMaterial, true),
                            new Key("d", s -> BlockFace.valueOf(s.toUpperCase()), true),
                            new Key("w", Main::parseBoolean, true),
                            new Key("r", Integer::valueOf, false)
                    );

                    TerrainBlockData terrainBlockData = new TerrainBlockData(Material.STRUCTURE_VOID);
                    TreeType treeType = (TreeType) map.get("tree");
                    if (map.containsKey("m")) {
                        terrainBlockData.setMaterial((Material) map.get("m"));
                    }
                    if (map.containsKey("d")) {
                        terrainBlockData.setBlockFace((BlockFace) map.get("d"));
                    }
                    if (map.containsKey("w")) {
                        if (map.get("w") != null) {
                            terrainBlockData.setWaterLogged((Boolean) map.get("w"));
                        }
                    }
                    int chance = (int) map.get("r");

                    boolean oldData = false;
                    if (getModeData().containsKey(treeType)) {
                        getModeData().remove(treeType);
                        oldData = true;
                    }

                    if (number > getModeData().size() + 1) {
                        player.sendMessage(formatError("Your given place must be lower than the amount of data in the TerrainMode %s (%s)", getModeName(), String.valueOf((getModeData().size() + 1))));
                        return;
                    } else if (number < 1) {
                        player.sendMessage(formatError("Your given place must be higher than the 0"));
                        return;
                    }

                    LinkedHashMap<TreeType, TreeData> newMap = new LinkedHashMap<>();

                    int currNumber = 1;
                    boolean b = true;
                    for (TreeType tmpTreeType : getModeData().keySet()) {
                        if (currNumber == number) {
                            newMap.put(treeType, new TreeData(chance, terrainBlockData));
                            b = false;
                        }
                        newMap.put(tmpTreeType, getModeData().get(tmpTreeType));
                        currNumber++;
                    }
                    if (b) {
                        newMap.put(treeType, new TreeData(chance, terrainBlockData));
                    }

                    setModeData(newMap);

                    Message message = new Message();
                    message.addText(textComponent("Successfully set ", ColorFormatter.successColor));
                    message.addText(textComponent((oldData ? "old " : "new "), ColorFormatter.varSuccessColor));
                    message.addText(textComponent(treeType.name(), ColorFormatter.varSuccessColor,
                            hoverEvent(newMap.get(treeType).toStringSuccess())).setInsertion(getInsertion(treeType, newMap.get(treeType))));
                    message.addText(textComponent(" to place ", ColorFormatter.successColor));
                    message.addText(textComponent(String.valueOf(number), ColorFormatter.varSuccessColor));
                    message.sendMessage(player);
                } catch (KeyValueError e) {
                    e.sendMessage(player);
                }
            }
        }
    }

    @Override
    public String getInsertion(TreeType treeType, TreeData treeData) {
        return "tree=" + treeType.name() +
                ",m=" + treeData.getBlockData().getMaterial().name() +
                ",d=" + treeData.getBlockData().getBlockFace().name() +
                ",w=" + treeData.getBlockData().isWaterLogged() +
                ",r=" + treeData.getI();
    }

    @Override
    public boolean isFinalMode() {
        return true;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is the TerrainMode where you set the treeTypes for the TerrainMode addTrees";
    }

    @Override
    public String getModeName() {
        return "treeTypes";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
    }

    public TreeData getTree(Random random) {
        if (getModeData() == null || getModeData().isEmpty()) {
            return null;
        }

        int total = 0;

        for (TreeType tree : getModeData().keySet()) {
            total += getModeData().get(tree).getI();
        }
    
        int mSelected = random.nextInt(total);

        total = 0;

        for (TreeType tree : getModeData().keySet()) {
            total += getModeData().get(tree).getI();
            if (total > mSelected) {
                TreeData treeData = new TreeData(0, getModeData().get(tree).getBlockData());
                treeData.setTreeType(tree);
                return treeData;
            }
        }
        return null;
    }

    public static class TreeData implements ConfigurationSerializable {
        private int i;
        private TerrainBlockData m;
        private TreeType treeType;

        public TreeData(int i, TerrainBlockData m) {
            this.i = i;
            this.m = m;
        }

        public static TreeData deserialize(Map<String, Object> args) {
            return new TreeData(
                    Integer.parseInt(args.getOrDefault("i", "1").toString()),
                    (TerrainBlockData) args.getOrDefault("m", new TerrainBlockData()));
        }

        @Override
        public String toString() {
            return formatInfo("Support block: (%s), Tree chance: %s", m.toString(), String.valueOf(i));
        }

        public String toStringSuccess() {
            return formatSuccess("Support block: (%s), Tree chance: %s", m.toString(), String.valueOf(i));
        }

        public TreeType getTreeType() {
            return treeType;
        }

        public void setTreeType(TreeType treeType) {
            this.treeType = treeType;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public TerrainBlockData getBlockData() {
            return m;
        }

        public void setBlockData(TerrainBlockData m) {
            this.m = m;
        }

        @Override
        @Nonnull
        public Map<String, Object> serialize() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("i", i);
            map.put("m", m);
            return map;
        }
    }
}
