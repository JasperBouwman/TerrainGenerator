package com.spaceman.terrainGenerator.terrain.generators;

import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.biome.TerrainGrid;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.useFinalChunkModes;

@SuppressWarnings({"unused"})
public class TerrainGenerator {
    
    public static void generate(String terrainGenDataName, int width, int height, Location startL) {
        
        TerrainGenData mainGenerator = terrainGenData.get(terrainGenDataName);
        if (mainGenerator == null) {
            throw new IllegalArgumentException("TerrainGenData is null");
        }
        
        //location, genDataName, fromTo
        HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage = new HashMap<>();
        
        LocData locData = new LocData(startL, width, height);
        Random random = new Random(mainGenerator.getSeed() + (startL.getBlockX() + 3000000L) + (startL.getBlockY() + 3000000L) * 5999999L);
        
        HashMap<String, Object> genModeData = new HashMap<>();
        
        HashMap<String, LinkedList<String>> biomes = new HashMap<>();
        for (String biome : mainGenerator.getBiomeSettings().getBiomes()) {
            LinkedList<String> tmpList = new LinkedList<>();
            tmpList.add(biome);
            biomes.put(biome, getGenerators(tmpList));
        }
        
        HashMap<String, String> biomeMap = new HashMap<>();
        
        for (int realX = startL.getBlockX(); realX < width + startL.getBlockX(); realX++) {
            for (int realZ = startL.getBlockZ(); realZ < height + startL.getBlockZ(); realZ++) {
                
                String biome = TerrainGrid.getBiome(realX, realZ, mainGenerator.getBiomeSettings().getBiomes(), mainGenerator);
                LinkedList<String> biomeGenerators = biomes.get(biome);
                
                for (String biomeGenerator : biomeGenerators) {
                    TerrainGenData i = terrainGenData.get(biomeGenerator);
                    
                    if (i != null) {
                        WorldGenerator.generate(0, 0, realX, realZ, 0, 0, i, mainGenerator, startL.getWorld(), genStorage, random,
                                null, null, locData, genModeData);
                    }
                }
                
                if (mainGenerator.isFastRender()) {
                    useFinalChunkModes(realX, realZ, locData, genStorage, "", genModeData, null, biomeGenerators);
                } else {
                    biomeMap.put(realX + ";" + realZ, biome);
                }
            }
        }
        
        if (!mainGenerator.isFastRender()) {
            for (int realX = startL.getBlockX(); realX < width + startL.getBlockX(); realX++) {
                for (int realZ = startL.getBlockZ(); realZ < height + startL.getBlockZ(); realZ++) {
                    //save per coordinate biome (BiomeMap) and get generator
                    String biome = biomeMap.get(realX + ";" + realZ);
                    useFinalChunkModes(realX, realZ, locData, genStorage, "", genModeData, null, biomes.get(biome));
                }
            }
        }
    }
    
    private static LinkedList<String> getGenerators(LinkedList<String> d, LinkedList<String> curr) {
        
        if (d == null) {
            return new LinkedList<>();
        }
        
        for (String s : d) {
            if (curr.contains(s)) {
                return curr;
            }
        }
        
        curr.addAll(d);
        for (String data : d) {
            for (String tmpData : getGenerators(getGenerators(data), curr)) {
                if (!curr.contains(tmpData)) {
                    if (TerrainGenData.terrainGenData(tmpData, true) != null) {
                        curr.add(tmpData);
                    }
                }
            }
        }
        return curr;
    }
    
    private static LinkedList<String> getGenerators(String data) {
        if (terrainGenData.containsKey(data)) {
            return terrainGenData.get(data).getGenerators();
        } else {
            return new LinkedList<>();
        }
    }
    
    public static LinkedList<String> getGenerators(LinkedList<String> d) {
        return getGenerators(d, new LinkedList<>());
    }
    
    public static LinkedList<String> getGenerators(String... d) {
        return getGenerators(new LinkedList<>(Arrays.asList(d)));
    }
    
    public static void useModes(TerrainGenData data, int x, int z, HashMap<String,
            HashMap<String, GenData>> genStorage, LocData locData, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
        
        for (TerrainMode mode : data.getModes()) {
            if (!mode.isFinalMode()) {
                try {
                    try {
                        mode.useMode(x, z, genStorage, locData, data, savePath, genModeData, chunkData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static class LocData {
        private Location startL;
        private int x;
        private int z;
        
        public LocData(Location startL, int x, int z) {
            this.startL = startL;
            this.x = x;
            this.z = z;
        }
        
        public int getZ() {
            return z;
        }
        
        public int getX() {
            return x;
        }
        
        public Location getStartL() {
            return startL;
        }
        
        public World getWorld() {
            return startL.getWorld();
        }
    }
    
    public static class GenData {
        private int startGen;
        private int heightGen;
        private int start;
        private int end;
        private Random random;
        private boolean fromTop;
        
        public GenData(int startGen, int endGen, int start, int genHeight, Random random, boolean fromTop) {
            this.startGen = startGen;
            this.heightGen = endGen;
            this.start = start;
            this.end = genHeight;
            this.random = random;
            this.fromTop = fromTop;
        }
        
        public static int getHighest(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage) {
            int highest = 0;
            for (String loc : genStorage.keySet()) {
                if (loc.equals(x + ";" + z)) {
                    for (GenData genData : genStorage.get(loc).values()) {
                        if (genData.heightGen > highest) {
                            highest = genData.heightGen;
                        }
                    }
                }
            }
            return highest;
        }
        
        public static int getLowest(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage) {
            int lowest = Integer.MAX_VALUE;
            String loc = x + ";" + z;
            if (genStorage.containsKey(loc)) {
                for (GenData genData : genStorage.get(loc).values()) {
                    if (genData.heightGen < lowest) {
                        lowest = genData.heightGen;
                    }
                }
            }
            return lowest;
        }
        
        public static ArrayList<Integer> getAirPockets(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage, int start, int end, String... exclude) {
            ArrayList<Integer> airPockets = new ArrayList<>();
            if (start == -1 || end == -1) {
                return airPockets;
            }
            
            for (int i = end; i >= start; i--) {
                airPockets.add(i);
            }
            List<String> excludeList = Arrays.asList(exclude);
            
            String loc = x + ";" + z;
            if (genStorage.containsKey(loc)) {
                for (String name : genStorage.get(loc).keySet()) {
                    if (!excludeList.contains(name)) {
                        GenData genData = genStorage.get(loc).get(name);
                        for (int i = genData.startGen; i <= genData.heightGen; i++) {
                            airPockets.remove(Integer.valueOf(i));
                        }
                    }
                }
            }
            
            return airPockets;
        }
        
        public static GenData getGenData(int x, int z, String name, HashMap<String, HashMap<String, GenData>> genStorage) {
            return genStorage.getOrDefault(x + ";" + z, new HashMap<>()).getOrDefault(name, new GenData(-1, -1, -1, -1, new Random(), true));
        }
        
        @Override
        public String toString() {
            return startGen + " " + heightGen;
        }
        
        public int getStartGen() {
            return startGen;
        }
        
        public int getHeightGen() {
            return heightGen;
        }
        
        public int getStart() {
            return start;
        }
        
        public int getEnd() {
            return end;
        }
        
        public Random getRandom() {
            return random;
        }
        
        public boolean isFromTop() {
            return fromTop;
        }
    }
}
