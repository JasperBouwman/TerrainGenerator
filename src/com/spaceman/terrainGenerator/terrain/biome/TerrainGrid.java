package com.spaceman.terrainGenerator.terrain.biome;

import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

import java.awt.*;
import java.util.*;

public class TerrainGrid {

    //<worldName, <section, <Biomes>>>
    private static HashMap<String, HashMap<Integer, ArrayList<BiomeCenter>>> cachedSections = new HashMap<>();
    
    public static void clearSectionCache() {
        cachedSections.clear();
    }

    private static ArrayList<BiomeCenter> createSection(long biomeSeed, int sectionX, int sectionZ, int size, Collection<String> biomes, World world, int biomeScale, TerrainGenData mainGenerator) {
        ArrayList<BiomeCenter> biomeCenters = new ArrayList<>();
        Random random = new Random(biomeSeed + (sectionX * (6000000 / size)) + (sectionZ));
        Random randomColor = new Random(biomeSeed + (sectionZ * (6000000 / size)) + (sectionX));
        for (int i = 0; i < biomeScale; i++) {
            biomeCenters.add(new BiomeCenter(random.nextInt(size) + sectionX * size, random.nextInt(size) + sectionZ * size, getRandomBiome(randomColor, biomes, mainGenerator)));
        }
        String saveName = world == null ? mainGenerator.getName() : world.getName();
        //clean up the cache
        while (cachedSections.getOrDefault(saveName, new HashMap<>()).size() > (world == null ? 9 : world.getPlayers().size() * 9)) {
            cachedSections.getOrDefault(saveName, new HashMap<>())
                    .remove(
                            cachedSections.getOrDefault(saveName, new HashMap<>())
                                    .keySet().toArray(new Integer[]{})[0]
                    );
        }

        HashMap<Integer, ArrayList<BiomeCenter>> map = cachedSections.getOrDefault(saveName, new HashMap<>());
        map.put((sectionX * (6000000 / size)) + (sectionZ), biomeCenters);
        cachedSections.put(saveName, map);
        return biomeCenters;
    }

    private static String getRandomBiome(Random random, Collection<String> biomes, TerrainGenData mainGenerator) {
//        return biomes.size() == 1 ? biomes.get(0) : biomes.get(random.nextInt(biomes.size()));
        int sum = 0;
        for (String biome : biomes) {
            sum += mainGenerator.getBiomeSettings().getBiomeSize(biome);
        }
        
        int num = (random.nextInt(sum));
        int tmpSum = 0;
        for (String biome : biomes) {
            tmpSum += mainGenerator.getBiomeSettings().getBiomeSize(biome);
            if (tmpSum > num) {
                return biome;
            }
        }
        throw new AssertionError();
    }

    private static BiomeCenter checkSurrounding(int sectionX, int sectionZ, int x, int z, double closest, Collection<String> biomes, TerrainGenData mainGenerator, World world) {
        BiomeCenter chosenBiomeCenter = null;
        double tmpC = closest;
        int size = mainGenerator.getBiomeSettings().getSectionSize();

        for (Dimension d : Arrays.asList(
                new Dimension(sectionX + 1, sectionZ + 1),
                new Dimension(sectionX + 1,        sectionZ),
                new Dimension(sectionX + 1, sectionZ - 1),
                new Dimension(       sectionX,     sectionZ + 1),
                new Dimension(       sectionX,     sectionZ - 1),
                new Dimension(sectionX - 1, sectionZ + 1),
                new Dimension(sectionX - 1,        sectionZ),
                new Dimension(sectionX - 1, sectionZ - 1))) {

            ArrayList<BiomeCenter> biomeCenters = getSection(world, d.width, d.height, size, biomes, mainGenerator);

            for (BiomeCenter biomeCenter : biomeCenters) {
                double distance = Math.sqrt(NumberConversions.square(biomeCenter.getX() - x) + NumberConversions.square(biomeCenter.getZ() - z))
                        * mainGenerator.getBiomeSettings().getBiomeWeight(biomeCenter.getBiome());
                if (distance < tmpC) {
                    tmpC = distance;
                    chosenBiomeCenter = biomeCenter;
                }
            }
        }

        return chosenBiomeCenter;
    }

    private static ArrayList<BiomeCenter> getSection(World world, int sectionX, int sectionZ, int size, Collection<String> biomes, TerrainGenData mainGenerator) {
        ArrayList<BiomeCenter> biomeCenters;
        String saveName = world == null ? mainGenerator.getName() : world.getName();
        if (cachedSections.getOrDefault(saveName, new HashMap<>()).containsKey((sectionX * (6000000 / size)) + (sectionZ))) {
            biomeCenters = cachedSections.get(saveName).get((sectionX * (6000000 / size)) + (sectionZ));
        } else {
            biomeCenters = createSection(mainGenerator.getBiomeSettings().getBiomeSeed(), sectionX, sectionZ, size, biomes, world, mainGenerator.getBiomeSettings().getBiomeScale(), mainGenerator);
        }
        return biomeCenters;
    }
    
    public static String getBiome(int x, int z, Collection<String> biomes, TerrainGenData mainGenerator) {
        return getBiome(x, z, biomes, mainGenerator, null);
    }

    public static String getBiome(int x, int z, Collection<String> biomes, TerrainGenData mainGenerator, World world) {
        if (biomes.size() == 1) {
            return mainGenerator.getName();
        }
        int size = mainGenerator.getBiomeSettings().getSectionSize();

        int sectionX = x / size;
        if (x < 0) sectionX--;

        int sectionZ = z / size;
        if (z < 0) sectionZ--;

        ArrayList<BiomeCenter> biomeCenters = getSection(world, sectionX, sectionZ, size, biomes, mainGenerator);

        double closest = Double.MAX_VALUE;
        BiomeCenter chosenBiomeCenter = new BiomeCenter(Integer.MIN_VALUE, Integer.MAX_VALUE, null);
        for (BiomeCenter biomeCenter : biomeCenters) {
            double distance = Math.sqrt(NumberConversions.square(biomeCenter.getX() - x) + NumberConversions.square(biomeCenter.getZ() - z))
                    * mainGenerator.getBiomeSettings().getBiomeWeight(biomeCenter.getBiome());
            if (distance < closest) {
                closest = distance;
                chosenBiomeCenter = biomeCenter;
            }
        }

        BiomeCenter tmpBiomeCenter = checkSurrounding(sectionX, sectionZ, x, z, closest, biomes, mainGenerator, world);
        if (tmpBiomeCenter != null) {
            chosenBiomeCenter = tmpBiomeCenter;
        }

        return chosenBiomeCenter.getBiome();
    }

    private static class BiomeCenter {

        private int x;
        private int z;
        private String biome;

        private BiomeCenter(int x, int z, String biome) {
            this.x = x;
            this.z = z;
            this.biome = biome;
        }

        public int getZ() {
            return z;
        }

        public int getX() {
            return x;
        }

        public String getBiome() {
            return biome;
        }

    }
}
