package com.spaceman.terrainGenerator.terrain.generators;


import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.worldSeedValue;

@SuppressWarnings({"unused", "WeakerAccess"})
public class OldTerrainGenerator {

//    public static void generate(String terrainGenDataName, int x1, int z1, Location startL) {
//        if (startL.getWorld() == null) {
//            throw new IllegalArgumentException("The world of the start location can't be null");
//        }
//        TerrainGenData mainGenerator = terrainGenData.get(terrainGenDataName);
//
//        if (mainGenerator == null) {
//            throw new IllegalArgumentException("TerrainGenData is null");
//        }
//
//        LocData locData = new LocData(startL, x1, z1);
//        Random random = new Random(mainGenerator.getSeed());
//
//        LinkedList<String> internalGenerators = new LinkedList<>();
//        internalGenerators.add(terrainGenDataName);//add main generator so it would startGen with this one
//        internalGenerators.addAll(getGenerators(mainGenerator.getGenerators()));//add all other generators
//        //location, genDataName, fromTo
//        HashMap<String, HashMap<String, GenData>> genStorage = new HashMap<>();
//        HashMap<String, Object> genModeData = new HashMap<>();
//
//        for (String internalGenerator : internalGenerators) {
//            TerrainGenData i = terrainGenData.get(internalGenerator);
//
//            if (i != null) {
//
//                long seed = i.getSeed();
//                if (seed == worldSeedValue) {
//                    seed = startL.getWorld().getSeed();
//                }
//
//                TerrainMode layersMode = i.getMode("layers");
//
//                for (int x = startL.getBlockX(); x < x1 + startL.getBlockX(); x++) {
//                    for (int z = startL.getBlockZ(); z < z1 + startL.getBlockZ(); z++) {
//
//
//                        int start = i.getStart(x, z, startL.getWorld());
//                        int genHeight = (i.getTerrainNoise().noise(x, z, seed) + i.getHeight());
//
//                        if (genHeight > startL.getWorld().getMaxHeight()) {
//                            genHeight = startL.getWorld().getMaxHeight();
//                        }
//
//                        ArrayList<Integer> airPockets = new ArrayList<>();
//                        if (!mainGenerator.getFromTop()) {
//                            airPockets = getAirPockets(x, z, genStorage, start, genHeight);
//                        }
//
//                        int startGen;
//                        if (mainGenerator.getFromTop()) {
//                            int highest = GenData.getHighest(x, z, genStorage);
//                            startGen = (Math.max(start, highest));
//                        } else {
//                            startGen = start;
//                        }
//
//                        int endGen = genHeight;
//
//                        HashMap<String, GenData> tmpMap = genStorage.getOrDefault(x + ";" + z, new HashMap<>());
//                        tmpMap.put(internalGenerator, new GenData(startGen, endGen, start, genHeight, random, mainGenerator.getFromTop()));
//                        genStorage.put(x + ";" + z, tmpMap);
//
//                        for (int y = genHeight; y >= startGen; y--) {
//
//                            if (!mainGenerator.getFromTop()) {
//                                if (!airPockets.contains(y)) {
//                                    continue;
//                                }
//                            }
//
//                            Block block = new Location(startL.getWorld(), x, y, z).getBlock();
//
//                            if (i.getBiome() != null) {
//                                block.setBiome(i.getBiome());
//                            }
//
//                            // internal modes: 'layers'
//                            if (layersMode == null) {
//                                if (!i.getTerrainBlockData().getMaterial().equals(Material.STRUCTURE_VOID)) {
//                                    setType(block, i.getTerrainBlockData());
//                                }
//                            }
//                        }
//
//                        useModes(i, x, z, genStorage, locData, "", genModeData, null);
//
//                    }
//                }
//            }
//        }
//        useFinalModes(internalGenerators, locData, genStorage, "", genModeData, null);
//    }
//
//    private static LinkedList<String> getGenerators(LinkedList<String> d, LinkedList<String> curr) {
//
//        if (d == null) {
//            return new LinkedList<>();
//        }
//
//        for (String s : d) {
//            if (curr.contains(s)) {
//                return curr;
//            }
//        }
//
//        curr.addAll(d);
//        for (String data : d) {
//            for (String tmpData : getGenerators(getGenerators(data), curr)) {
//                if (!curr.contains(tmpData)) {
//                    if (TerrainGenData.terrainGenData(tmpData, true) != null) {
//                        curr.add(tmpData);
//                    }
//                }
//            }
//        }
//        return curr;
//    }
//
//    private static LinkedList<String> getGenerators(String data) {
//        if (terrainGenData.containsKey(data)) {
//            return terrainGenData.get(data).getGenerators();
//        } else {
//            return new LinkedList<>();
//        }
//    }
//
//    public static LinkedList<String> getGenerators(LinkedList<String> d) {
//        return getGenerators(d, new LinkedList<>());
//    }
//
//    public static LinkedList<String> getGenerators(String... d) {
//        return getGenerators(new LinkedList<>(Arrays.asList(d)));
//    }
//
//    public static void useModes(TerrainGenData data, int x, int z, HashMap<String,
//            HashMap<String, GenData>> genStorage, LocData locData, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
//
//        for (TerrainMode mode : data.getModes()) {
//            if (!mode.isFinalMode()) {
//                try {
//                    mode.useMode(x, z, genStorage, locData, data, savePath, genModeData, chunkData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void useFinalModes(LinkedList<String> dataList, LocData locData,
//                                     HashMap<String, HashMap<String, GenData>> genStorage, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
//
//        for (int x = locData.startL.getBlockX(); x < locData.x + locData.startL.getBlockX(); x++) {
//            for (int z = locData.startL.getBlockZ(); z < locData.z + locData.startL.getBlockZ(); z++) {
//
//                for (String sData : dataList) {
//                    TerrainGenData data = terrainGenData.get(sData);
//                    if (data == null) {
//                        Main.println(sData);
//                        continue;
//                    }
//
//                    for (TerrainMode mode : data.getModes()) {
//                        if (mode.isFinalMode()) {
//                            try {
//                                mode.useMode(x,
//                                        z,
//                                        genStorage,
//                                        locData,
//                                        data,
//                                        savePath,
//                                        genModeData,
//                                        chunkData);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    public static class LocData {
//        private Location startL;
//        private int x;
//        private int z;
//
//        public LocData(Location startL, int x, int z) {
//            this.startL = startL;
//            this.x = x;
//            this.z = z;
//        }
//
//        public int getZ() {
//            return z;
//        }
//
//        public int getX() {
//            return x;
//        }
//
//        public Location getStartL() {
//            return startL;
//        }
//
//        public World getWorld() {
//            return startL.getWorld();
//        }
//    }
//
//    public static class GenData {
//        private int startGen;
//        private int heightGen;
//        private int start;
//        private int end;
//        private Random random;
//        private boolean fromTop;
//
//        public GenData(int startGen, int endGen, int start, int genHeight, Random random, boolean fromTop) {
//            this.startGen = startGen;
//            this.heightGen = endGen;
//            this.start = start;
//            this.end = genHeight;
//            this.random = random;
//            this.fromTop = fromTop;
//        }
//
//        public static int getHighest(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage) {
//            int highest = 0;
//            for (String loc : genStorage.keySet()) {
//                if (loc.equals(x + ";" + z)) {
//                    for (GenData genData : genStorage.get(loc).values()) {
//                        if (genData.heightGen > highest) {
//                            highest = genData.heightGen;
//                        }
//                    }
//                }
//            }
//            return highest;
//        }
//
//        public static int getLowest(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage) {
//            int lowest = Integer.MAX_VALUE;
//            String loc = x + ";" + z;
//            if (genStorage.containsKey(loc)) {
//                for (GenData genData : genStorage.get(loc).values()) {
//                    if (genData.heightGen < lowest) {
//                        lowest = genData.heightGen;
//                    }
//                }
//            }
//            return lowest;
//        }
//
//        public static ArrayList<Integer> getAirPockets(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage, int start, int end, String... exclude) {
//            ArrayList<Integer> airPockets = new ArrayList<>();
//            if (start == -1 || end == -1) {
//                return airPockets;
//            }
//
//            for (int i = end; i >= start; i--) {
//                airPockets.add(i);
//            }
//            List<String> excludeList = Arrays.asList(exclude);
//
//            String loc = x + ";" + z;
//            if (genStorage.containsKey(loc)) {
//                for (String name : genStorage.get(loc).keySet()) {
//                    if (!excludeList.contains(name)) {
//                        GenData genData = genStorage.get(loc).get(name);
//                        for (int i = genData.startGen; i <= genData.heightGen; i++) {
//                            airPockets.remove(Integer.valueOf(i));
//                        }
//                    }
//                }
//            }
//
//            return airPockets;
//        }
//
//        public static GenData getGenData(int x, int z, String name, HashMap<String, HashMap<String, GenData>> genStorage) {
//            return genStorage.getOrDefault(x + ";" + z, new HashMap<>()).getOrDefault(name, new GenData(-1, -1, -1, -1, new Random(), true));
//        }
//
//        @Override
//        public String toString() {
//            return startGen + " " + heightGen;
//        }
//
//        public int getStartGen() {
//            return startGen;
//        }
//
//        public int getHeightGen() {
//            return heightGen;
//        }
//
//        public int getStart() {
//            return start;
//        }
//
//        public int getEnd() {
//            return end;
//        }
//
//        public Random getRandom() {
//            return random;
//        }
//
//        public boolean isFromTop() {
//            return fromTop;
//        }
//    }
}
