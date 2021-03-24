package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.Pair;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.keyValueHelper.ExtendedKey;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueError;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueHelper;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueTabArgument;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainCore;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.MapMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class TreeModifier extends MapMode<TerrainBlockData, TerrainBlockData> {
    
    private TreeDelegate treeDelegate = new TreeDelegate();
    
    @Override
    public Collection<String> tabListAdd(String[] args, Player player) {
        return KeyValueHelper.constructTab(args[args.length - 1],
                new KeyValueTabArgument("mFrom", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                new KeyValueTabArgument("dFrom", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                new KeyValueTabArgument("mTo", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                new KeyValueTabArgument("dTo", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                new KeyValueTabArgument("wTo", Arrays.asList("true", "false", "null"))
        );
    }
    
    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        return tabListAdd(args, player);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> tabListRemove(String[] args, Player player) {
        //terrain mode edit <name> <TerrainMode name> remove <data...>
        //format: mFrom=MATERIAL,dFrom=DIRECTION
        return KeyValueHelper.constructTab(args[args.length - 1],
                
                new KeyValueTabArgument("mFrom", getModeData(), ((o, writtenPairs) -> {
                    
                    for (Pair<String, String> keyValue : writtenPairs) {
                        if (keyValue.getLeft().equalsIgnoreCase("dFrom")) {
                            ArrayList<String> list = new ArrayList<>();
                            String blockFace = keyValue.getRight();
                            
                            for (TerrainBlockData blockData : ((HashMap<TerrainBlockData, TerrainBlockData>) o).keySet()) {
                                if (blockData.getBlockFace().name().equalsIgnoreCase(blockFace)) {
                                    list.add(blockData.getMaterial().name());
                                }
                            }
                            return list;
                        }
                    }
                    
                    ArrayList<String> list = new ArrayList<>();
                    for (TerrainBlockData blockData : ((HashMap<TerrainBlockData, TerrainBlockData>) o).keySet()) {
                        list.add(blockData.getMaterial().name());
                    }
                    return list;
                })),
                new KeyValueTabArgument("dFrom", getModeData(), (o, writtenPairs) -> {
                    for (Pair<String, String> keyValue : writtenPairs) {
                        if (keyValue.getLeft().equalsIgnoreCase("mFrom")) {
                            ArrayList<String> list = new ArrayList<>();
                            String material = keyValue.getRight();
                            
                            for (TerrainBlockData blockData : ((HashMap<TerrainBlockData, TerrainBlockData>) o).keySet()) {
                                if (blockData.getMaterial().name().equalsIgnoreCase(material)) {
                                    list.add(blockData.getBlockFace().name());
                                }
                            }
                            return list;
                        }
                    }
                    ArrayList<String> list = new ArrayList<>();
                    for (TerrainBlockData blockData : ((HashMap<TerrainBlockData, TerrainBlockData>) o).keySet()) {
                        list.add(blockData.getBlockFace().name());
                    }
                    return list;
                })
        
        );
    }
    
    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        if (args.length == 7) {
            return KeyValueHelper.constructTab(args[args.length - 1],
                    new KeyValueTabArgument("mFrom", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("dFrom", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("mTo", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("dTo", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("wTo", Arrays.asList("true", "false", "null"))
            );
        } else {
            return Collections.emptyList();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void addData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() > 0) {
                label:
                for (String arg : data) {
                    try {
                        
                        TerrainBlockData from = new TerrainBlockData();
                        from.setBlockFace(BlockFace.SELF);
                        TerrainBlockData to = new TerrainBlockData();
                        to.setBlockFace(BlockFace.SELF);
                        
                        HashMap<String, Object> map = KeyValueHelper.extendedConstructObject(arg, new Pair<>(from, to),
                                (ExtendedKey) new ExtendedKey("mFrom", Material::matchMaterial, false, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getLeft().setMaterial((Material) value))).setErrorMessage(""),
                                (ExtendedKey) new ExtendedKey("dFrom", s -> BlockFace.valueOf(s.toUpperCase()), true, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getLeft().setBlockFace((BlockFace) value))).setErrorMessage(""),
                                (ExtendedKey) new ExtendedKey("mTo", Material::matchMaterial, false, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getRight().setMaterial((Material) value))).setErrorMessage(""),
                                (ExtendedKey) new ExtendedKey("dTo", s -> BlockFace.valueOf(s.toUpperCase()), true, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getRight().setBlockFace((BlockFace) value))).setErrorMessage(""),
                                (ExtendedKey) new ExtendedKey("wTo", Main::parseBoolean, true, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getRight().setWaterLogged((Boolean) value))).setErrorMessage("")
                        );
                        
                        Pair<TerrainBlockData, TerrainBlockData> pair = (Pair<TerrainBlockData, TerrainBlockData>) map.get(null);
                        from = pair.getLeft();
                        to = pair.getRight();
                        
                        for (TerrainBlockData oldFrom : getModeData().keySet()) {
                            if (oldFrom.equals(from)) {
                                TerrainBlockData oldTo = getModeData().get(oldFrom);
                                getModeData().remove(oldFrom);
                                getModeData().put(from, to);
                                
                                Message message = new Message();
                                message.addText(textComponent("Successfully edited ", successColor));
                                message.addText(textComponent(oldFrom.toString(), varSuccessColor, new HoverEvent(textComponent(oldTo.toString()))));
                                message.addText(textComponent(" to ", successColor));
                                message.addText(textComponent(from.toString(), varSuccessColor, new HoverEvent(textComponent(to.toString()))));
                                message.addText(textComponent(" from TerrainMode ", successColor));
                                message.addText(textComponent(this.getModeName(), varSuccessColor));
                                message.sendMessage(player);

//                                player.sendMessage(formatSuccess(
//                                        "Successfully edited %s to %s from TerrainMode %s", oldFrom.toString(), from.toString(), this.getModeName()));
                                continue label;
                            }
                        }
                        getModeData().put(from, to);
                        Message message = new Message();
                        message.addText(textComponent("Successfully added ", successColor));
                        message.addText(textComponent(from.toString(), varSuccessColor, new HoverEvent(textComponent(to.toString()))));
                        message.addText(textComponent(" to TerrainMode ", successColor));
                        message.addText(textComponent(this.getModeName(), varSuccessColor));
                        message.sendMessage(player);//todo test
//                        player.sendMessage(formatSuccess("Successfully added %s to TerrainMode %s", from.toString(), this.getModeName()));
                    
                    } catch (KeyValueError e) {
                        e.sendMessage(player);
                    }
                }
            } else {
                player.sendMessage(formatError("Missing data"));
            }
        }
    }
    
    @Override
    public void removeData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() > 0) {
                for (String arg : data) {
                    try {
                        TerrainBlockData from = new TerrainBlockData();
                        from.setBlockFace(BlockFace.SELF);
                        HashMap<String, Object> map = KeyValueHelper.extendedConstructObject(arg, from,
                                (ExtendedKey) new ExtendedKey("mFrom", Material::matchMaterial, false, ((o, value) -> ((TerrainBlockData) o).setMaterial((Material) value))).setErrorMessage(""),
                                (ExtendedKey) new ExtendedKey("dFrom", s -> BlockFace.valueOf(s.toUpperCase()), true, ((o, value) -> ((TerrainBlockData) o).setBlockFace((BlockFace) value))).setErrorMessage("")
                        );
                        
                        from = (TerrainBlockData) map.get(null);
                        
                        for (TerrainBlockData old : getModeData().keySet()) {
                            if (old.equals(from)) {
                                getModeData().remove(old);
                                player.sendMessage(formatSuccess("Successfully removed %s", from.toString()));
                                return;
                            }
                        }
                        
                        player.sendMessage(formatError("%s is not in the TerrainMode: %s", from.toString(), getModeName()));
                        
                    } catch (KeyValueError e) {
                        e.sendMessage(player);
                    }
                }
            } else {
                player.sendMessage(formatError("Missing data"));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setData(LinkedList<String> data, int number, Player player) {
        if (data != null) {
            if (data.size() > 0) {
                try {
                    TerrainBlockData from = new TerrainBlockData();
                    from.setBlockFace(BlockFace.SELF);
                    TerrainBlockData to = new TerrainBlockData();
                    to.setBlockFace(BlockFace.SELF);
                    
                    HashMap<String, Object> map = KeyValueHelper.extendedConstructObject(data.get(0), new Pair<>(from, to),
                            (ExtendedKey) new ExtendedKey("mFrom", Material::matchMaterial, false, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getLeft().setMaterial((Material) value))).setErrorMessage(""),
                            (ExtendedKey) new ExtendedKey("dFrom", s -> BlockFace.valueOf(s.toUpperCase()), true, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getLeft().setBlockFace((BlockFace) value))).setErrorMessage(""),
                            (ExtendedKey) new ExtendedKey("mTo", Material::matchMaterial, false, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getRight().setMaterial((Material) value))).setErrorMessage(""),
                            (ExtendedKey) new ExtendedKey("dTo", s -> BlockFace.valueOf(s.toUpperCase()), true, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getRight().setBlockFace((BlockFace) value))).setErrorMessage(""),
                            (ExtendedKey) new ExtendedKey("wTo", Main::parseBoolean, true, ((o, value) -> ((Pair<TerrainBlockData, TerrainBlockData>) o).getRight().setWaterLogged((Boolean) value))).setErrorMessage("")
                    );
                    
                    Pair<TerrainBlockData, TerrainBlockData> pair = (Pair<TerrainBlockData, TerrainBlockData>) map.get(null);
                    from = pair.getLeft();
                    to = pair.getRight();
                    
                    boolean oldData = false;
                    TerrainBlockData toBeRemoved = null;
                    for (TerrainBlockData tmpBlockData : getModeData().keySet()) {
                        if (from.equals(tmpBlockData)) {
                            oldData = true;
                            toBeRemoved = tmpBlockData;
//                            getModeData().remove(tmpBlockData);
                            break;
                        }
                    }
                    //todo check: if fail object vanished?
                    
                    if (number > getModeData().size() + (oldData ? 0 : 1)) {
                        player.sendMessage(formatError("Your given place must be lower than the amount of data in the TerrainMode %s (%s)", getModeName(), String.valueOf((getModeData().size() + (oldData ? 0 : 1)))));
                        return;
                    } else if (number < 1) {
                        player.sendMessage(formatError("Your given place must be higher than 0"));
                        return;
                    }
                    
                    if (oldData) {
                        getModeData().remove(toBeRemoved);
                    }
                    
                    LinkedHashMap<TerrainBlockData, TerrainBlockData> newMap = new LinkedHashMap<>();
                    
                    int currNumber = 1;
                    boolean b = true;
                    for (TerrainBlockData iss : getModeData().keySet()) {
                        if (currNumber == number) {
                            newMap.put(from, to);
                            b = false;
                        }
                        newMap.put(iss, getModeData().get(iss));
                        currNumber++;
                    }
                    if (b) {
                        newMap.put(from, to);
                    }
                    
                    setModeData(newMap);
                    
                    player.sendMessage(formatSuccess("Successfully set %s %s to place %s in TerrainMode %s", (oldData ? "old" : "new"), from.toString(), String.valueOf(number), getModeName()));
                } catch (KeyValueError e) {
                    e.sendMessage(player);
                }
            }
        }
    }
    
    @Override
    public String getModeName() {
        return "treeModifier";
    }
    
    @Override
    public String getInsertion(TerrainBlockData dataFrom, TerrainBlockData dataTo) { //todo test
        return "mFrom=" + dataFrom.getMaterial() + ",dFrom=" + dataFrom.getBlockFace() +
                ",mTo=" + dataTo.getMaterial() + "dTo=" + dataTo.getBlockFace() + ",wTo=" + dataTo.isWaterLogged();
    }
    
    @Override
    public boolean isFinalMode() {
        return true;
    }
    
    @Override
    public String getModeDescription() {
        return getModeName() + " is the TerrainMode that changes the generated blocks for the addTrees TerrainMode, " +
                "mFrom is the material to change, " +
                "dFrom is the direction of the material, " +
                "mTo is the new material, " +
                "dTo is the new direction, " +
                "wTo is if the block should be waterlogged (when null is used, the default will be used). " +
                "PS: for the dFrom, when the block used an axis (like logs) the axis will be converted to a direction, " +
                "the X axis will be converted to EAST, the Y axis to UP and the Z axis to NORTH. " +
                "When dFrom is set to self every direction is a match, " +
                "but if there is a key found with the same mFrom material and a dFrom NORTH " +
                "it will win when the block that is changed has the direction NORTH.";
    }
    
    @Override
    public void saveMode(ConfigurationSection section) {
        if (getModeData() != null) {
            for (TerrainBlockData is : getModeData().keySet()) {
                section.set("data." + TerrainUtils.toString(is), TerrainUtils.toString(getModeData().get(is)));
            }
        }
    }
    
    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        if (section.contains("data")) {
            //noinspection ConstantConditions
            for (String from : section.getConfigurationSection("data").getKeys(false)) {
                TerrainBlockData fromData = TerrainUtils.toTerrainBlockData(from);
                //noinspection ConstantConditions
                this.getModeData().put(fromData, TerrainUtils.toTerrainBlockData(section.getString("data." + from, "")));
            }
        }
        return this;
    }
    
    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage, TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
    
    }
    
    private void updateDelegate(@Nonnull World world) {
        if (treeDelegate == null) {
            treeDelegate = new TreeDelegate();
        }
        treeDelegate.setWorld(world);
    }
    
    public BlockChangeDelegate getBlockChangeDelegate(@Nonnull World world) {
        updateDelegate(world);
        return treeDelegate;
    }
    
    private class TreeDelegate implements BlockChangeDelegate {
        private World world;
        
        public void setWorld(@Nonnull World world) {
            this.world = world;
        }
        
        @Override
        public boolean setBlockData(int x, int y, int z, @Nonnull BlockData blockData) {
            
            if (world == null) {
                return false;
            }
            
            /*
             * m=OAK_LOG,d=SELF  -> m=dirt
             * m=OAK_LOG,d=SOUTH -> m=stone
             *
             * input: m=OAK_LOG,d=NORTH, output: m=dirt
             * input: m=OAK_LOG,d=SOUTH, output: m=stone
             * */
            
            Block block = new Location(world, x, y, z).getBlock();
            BlockFace currentFace = null;
            if (blockData instanceof Directional) {
                currentFace = ((Directional) blockData).getFacing();
            }
            if (blockData instanceof Rotatable) {
                currentFace = ((Rotatable) blockData).getRotation();
            }
            if (blockData instanceof Orientable) {
                currentFace = TerrainCore.convertAxisToBlockFace(((Orientable) blockData).getAxis());
            }
            
            Material m = null;
            BlockFace face = currentFace;
            Boolean waterLog = null;
            
            for (TerrainBlockData from : getModeData().keySet()) {
                if (from.getMaterial().equals(blockData.getMaterial())) {
                    if (currentFace == from.getBlockFace() ||
                            (from.getBlockFace() == BlockFace.SELF && m == null)) {
                        TerrainBlockData to = getModeData().get(from);
                        m = to.getMaterial();
                        face = to.getBlockFace();
                        waterLog = to.isWaterLogged();
                    }
                }
            }
            
            if (m != null) {
                TerrainCore.setType(block, m, face, waterLog);
                return true;
            } else {
                TerrainCore.setType(block, blockData.getMaterial(), face, waterLog);
                return false;
            }
        }
        
        @Override
        @Nonnull //this method should never be called
        public BlockData getBlockData(int i, int i1, int i2) {
            //noinspection ConstantConditions
            return null;
        }
        
        @Override
        public int getHeight() {
            return 0;
        }
        
        @Override
        public boolean isEmpty(int i, int i1, int i2) {
            return false;
        }
    }
    
}
