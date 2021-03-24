package com.spaceman.terrainGenerator.terrain.terrainMode;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"WeakerAccess"})
public abstract class TerrainMode {

    private static HashMap<String, ModeCreator> modes = new HashMap<>();

    public static boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return true;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9_-]");
        Matcher m = p.matcher(s);

        return m.find();
    }

    public static boolean registerMode(ModeCreator mode) {
        String name = mode.create().getModeName();

        if (containsSpecialCharacter(name)) {
            return false;
        }

        if (!modes.containsKey(name)) {
            modes.put(name, mode);
            return true;
        }
        return false;
    }

    public static HashMap<ModeCreator, Boolean> registerModes(ModeCreator... modes) {
        HashMap<ModeCreator, Boolean> map = new HashMap<>();
        for (ModeCreator mode : modes) {
            map.put(mode, registerMode(mode));
        }
        return map;
    }

    public static Set<String> getModes() {
        return modes.keySet();
    }

    public static TerrainMode getNewMode(String modeName) {
        return getNewMode(modeName, null, null);
    }

    public static TerrainMode getNewMode(String modeName, LinkedList<String> data, Player player) {

        if (modes.containsKey(modeName)) {
            try {
                TerrainMode mode = modes.get(modeName).create();

                if (data != null && player != null) {
                    if (mode instanceof DataMode) {
                        DataMode tmpMode = (DataMode) mode;
                        tmpMode.setData(data, player);
                        return tmpMode;
                    } else if (mode instanceof MapMode) {
                        MapMode tmpMode = (MapMode) mode;
                        tmpMode.addData(data, player);
                        return tmpMode;
                    } else if (mode instanceof ArrayMode) {
                        ArrayMode tmpMode = (ArrayMode) mode;
                        tmpMode.addData(data, player);
                        return tmpMode;
                    }
                }

                return mode;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public abstract String getModeName();

    public abstract boolean isFinalMode();

    public abstract String getModeDescription();

    public void remapTerrainGenerators(String file) {}

    public void demapTerrainGenerators() {}

    public Collection<String> tabListSet(String[] args, Player player) {
        return Collections.emptyList();
    }

    public Collection<String> tabListCreate(String[] args, Player player) {
        return Collections.emptyList();
    }

    public abstract void saveMode(ConfigurationSection section);

    public abstract TerrainMode loadMode(ConfigurationSection section);

    public abstract void useMode(int x, int z,
                                 HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                 TerrainGenerator.LocData locData,
                                 TerrainGenData data,
                                 String savePath,
                                 HashMap<String, Object> genModeData,
                                 WorldGenerator.TerrainChunkData chunkData);

    public abstract Message dataAsString();
    public abstract Message dataAsStringWithHover();

    public abstract String modeType();
}
