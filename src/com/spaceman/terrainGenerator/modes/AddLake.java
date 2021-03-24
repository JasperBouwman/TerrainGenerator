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
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeWaterLoggable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatInfo;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.*;

public class AddLake extends DataMode<AddLake.LakeData> implements TerrainModeWaterLoggable {

    public AddLake() {
        setModeData(new LakeData(64, new TerrainBlockData(Material.WATER)));
    }

    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        //terrain mode add <name> <name> [data]
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //terrain mode edit <name> <TerrainMode name> set <data...>
        if (args[0].equals("mode") && (
                args[1].equals("add") && args.length >= 5 ||
                        args[1].equals("set") && args.length >= 6 ||
                        args[1].equals("edit") && args[4].equals("set") && args.length >= 6)) {
            return KeyValueHelper.constructTab(args[args.length - 1], Arrays.asList(
                    new KeyValueTabArgument("m", TerrainUtils.blockMaterials.stream().map(Material::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("d", Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList())),
                    new KeyValueTabArgument("w", Arrays.asList("false", "true", "null")),
                    new KeyValueTabArgument("y", Collections.singletonList("<X>"))));
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        return tabListSet(args, player);
    }

    @Override
    public String getInsertion() {
        return "m=" + getModeData().getBlockData().getMaterial().name() +
                ",d=" + getModeData().getBlockData().getBlockFace().name() +
                ",w=" + getModeData().getBlockData().isWaterLogged() +
                ",y=" + getModeData().getHeight();
    }

    @Override
    public void setWaterLogged(LinkedList<String> data, Player player) {
        TerrainUtils.setWaterLoggedTerrainBlockData(data, player, getModeData().getBlockData());
    }

    @Override
    public Collection<String> tabListWaterLog(String[] args, Player player) {
        return TerrainUtils.tabListWaterLogTerrainBlockData(args);
    }

    @Override
    public void setData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() > 0) {
                try {
                    HashMap<String, Object> map = KeyValueHelper.constructObject(data.get(0),
                            new Key("m", Material::matchMaterial, true),
                            new Key("d", s -> BlockFace.valueOf(s.toUpperCase()), true),
                            new Key("w", Main::parseBoolean, true),
                            new Key("y", Integer::valueOf, true)
                    );

                    TerrainBlockData terrainBlockData = getModeData().getBlockData();
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
                    
                    int height = getModeData().getHeight();
                    if (map.containsKey("y")) {
                        height = (int) map.get("y");
                    }
    
                    this.setModeData(new LakeData(height, terrainBlockData));

                    Message message = new Message();
                    message.addText(textComponent("TerrainMode ", ColorFormatter.successColor));
                    message.addText(textComponent(getModeName(), ColorFormatter.varSuccessColor));
                    message.addText(textComponent(" is now set to ", ColorFormatter.successColor));
                    message.addText(textComponent(getModeData().toStringSuccess(), ColorFormatter.varSuccessColor).setInsertion(getInsertion()));
                    message.sendMessage(player);

                } catch (KeyValueError e) {
                    e.sendMessage(player);
                }
            }
        }
    }

    @Override
    public String getModeName() {
        return "addLake";
    }

    @Override
    public boolean isFinalMode() {
        return true;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that adds lakes of your chosen material to the chosen height";
    }

    @Override
    public void saveMode(ConfigurationSection section) {
        if (getModeData() != null) {
            section.set("data", getModeData());
        }
    }

    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        LakeData data = (LakeData) section.get("data");
        setModeData(data);
        return this;
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        ArrayList<Integer> airPockets = getAirPockets(x, z, genStorage, genData.getStartGen(), getModeData().getHeight());

        if (getModeData().getHeight() < 0) {
            return;
        }

        for (int y = getModeData().getHeight(); y > 0; y--) {
            if (airPockets.contains(y) || getHighest(x, z, genStorage) < y) {
                if (chunkData != null) {
                    chunkData.setBlock(y, getModeData().getBlockData());
                } else {
                    setType(new Location(locData.getWorld(), x, y, z).getBlock(), getModeData().getBlockData());
                }
            } else {
                return;
            }
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static class LakeData implements ConfigurationSerializable {
        private int height;
        private TerrainBlockData blockData;

        public LakeData() {
        }

        public LakeData(int height, TerrainBlockData blockData) {
            this.height = height;
            this.blockData = blockData;
        }

        public static LakeData deserialize(Map<String, Object> args) {
            LakeData lakeData = new LakeData();
            lakeData.setBlockData((TerrainBlockData) args.getOrDefault("blockData", new TerrainBlockData(Material.WATER)));
            lakeData.setHeight((Integer) args.getOrDefault("height", 64));
            return lakeData;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public TerrainBlockData getBlockData() {
            return blockData;
        }

        public void setBlockData(TerrainBlockData blockData) {
            this.blockData = blockData;
        }

        @Override
        public String toString() {
            return formatInfo("Lake block: (%s), height: %s", blockData.toString(), String.valueOf(height));
        }

        public String toStringSuccess() {
            return formatSuccess("Lake block: (%s), height: %s", blockData.toString(), String.valueOf(height));
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            map.put("blockData", blockData);
            map.put("height", height);
            return map;
        }
    }
}
