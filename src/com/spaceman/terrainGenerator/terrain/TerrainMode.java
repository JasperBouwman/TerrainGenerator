package com.spaceman.terrainGenerator.terrain;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"WeakerAccess"})
public abstract class TerrainMode implements Cloneable {

    private static HashMap<String, TerrainMode> modes = new HashMap<>();

    public static boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return true;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);

        return m.find();
    }

    public static boolean registerMode(TerrainMode mode) {

        if (containsSpecialCharacter(mode.getModeName())) {
            return false;
        }

        if (!modes.containsKey(mode.getModeName())) {
            modes.put(mode.getModeName(), mode);
            return true;
        }
        return false;
    }

    public static HashMap<TerrainMode, Boolean> registerModes(TerrainMode... modes) {
        HashMap<TerrainMode, Boolean> map = new HashMap<>();
        for (TerrainMode mode : modes) {
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
                TerrainMode mode = (TerrainMode) modes.get(modeName).clone();

                if (data != null && player != null) {
                    if (mode instanceof DataBased) {
                        DataBased tmpMode = (DataBased) mode;
                        tmpMode.setData(data, player);
                        return tmpMode;
                    } else if (mode instanceof MapBased) {
                        MapBased tmpMode = (MapBased) mode;
                        tmpMode.addData(data, player);
                        return tmpMode;
                    } else if (mode instanceof ArrayBased) {
                        ArrayBased tmpMode = (ArrayBased) mode;
                        tmpMode.addData(data, player);
                        return tmpMode;
                    }
                }

                return mode;
            } catch (Throwable e) {
                return null;
            }
        } else {
            List<String> l = new ArrayList<>();
            for (String s : modes.keySet()) {
                if (modeName.equalsIgnoreCase(s)) {
                    l.add(s);
                }
            }
            if (l.size() == 1) {
                return getNewMode(l.get(0), data, player);
            }
        }
        return null;
    }

    protected static void setType(Block block, ItemStack material) {
        TerrainGenerator.setType(block, material.getType());
    }

    protected static void setType(Location location, Material material) {
        TerrainGenerator.setType(location, material);
    }

    protected static void setType(Block block, Material material, BlockFace blockFace) {
        TerrainGenerator.setType(block, material, blockFace);
    }

    protected static void setType(Location location, Material material, BlockFace blockFace) {
        TerrainGenerator.setType(location, material, blockFace);
    }

    public abstract String getModeName();

    public abstract boolean isFinalMode();

    public abstract String getModeDescription();

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public abstract void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                                     TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData);

    public static abstract class DataBased<D> extends TerrainMode {

        D modeData = null;

//        public DataBased() {
//        }
//
//        public DataBased(D data) {
//            this.modeData = data;
//        }

        public D getModeData() {
            return modeData;
        }

        public void setModeData(D data) {
            this.modeData = data;
        }

        public abstract void saveMode(String savePath);

        public abstract DataBased getMode(String savePath, DataBased templateMode);

        public abstract void setData(LinkedList<String> data, Player player);
    }

    public static abstract class ArrayBased<D> extends TerrainMode {

        private LinkedList<D> modeData = new LinkedList<>();

//        public ArrayBased(LinkedList<D> data) {
//            this.modeData = data;
//        }
//
//        public ArrayBased() {
//        }

        public LinkedList<D> getModeData() {
            return modeData;
        }

        public void setModeData(LinkedList<D> data) {
            this.modeData = data;
        }

        public abstract void saveMode(String savePath);

        public abstract ArrayBased getMode(String savePath, ArrayBased templateMode);

        public abstract void addData(LinkedList<String> data, Player player);

        public abstract void removeData(LinkedList<String> data, Player player);

        public abstract void setData(LinkedList<String> data, int number, Player player);
    }

    public static abstract class MapBased<V, D> extends TerrainMode {

        private LinkedHashMap<V, D> modeData = new LinkedHashMap<>();

//        public MapBased(LinkedHashMap<V, D> data) {
//            this.modeData = data;
//        }
//
//        public MapBased() {
//        }

        public LinkedHashMap<V, D> getModeData() {
            return modeData;
        }

        public void setModeData(LinkedHashMap<V, D> data) {
            this.modeData = data;
        }

        public abstract void saveMode(String savePath);

        public abstract MapBased getMode(String savePath, MapBased templateMode);

        public abstract void addData(LinkedList<String> data, Player player);

        public abstract void removeData(LinkedList<String> data, Player player);

        public abstract void setData(LinkedList<String> data, int number, Player player);
    }
}
