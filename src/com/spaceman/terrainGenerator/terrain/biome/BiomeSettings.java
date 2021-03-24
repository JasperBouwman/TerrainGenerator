package com.spaceman.terrainGenerator.terrain.biome;

import com.spaceman.terrainGenerator.Pair;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BiomeSettings implements ConfigurationSerializable {
    
    private int sectionSize = 64;
    private int biomeScale = 2;
    private long biomeSeed = 1;
    //key=weight, value=size
    //terrainGenerator, weight, size
    private HashMap<String, Pair<Double, Integer>> biomeGenerators = new HashMap<>();
    
    private String owner;
    //biome transitions
    
    public BiomeSettings(String owner) {
        this.owner = owner;
        this.biomeGenerators.put(owner, new Pair<>(1D, 10));
    }
    
    public BiomeSettings(String owner, int sectionSize, int biomeScale, long biomeSeed, HashMap<String, Pair<Double, Integer>> biomeGenerators) {
        if (owner == null) {
            throw new NullPointerException("Owner can not be null");
        }
        this.owner = owner;
        this.sectionSize = sectionSize;
        this.biomeScale = biomeScale;
        this.biomeSeed = biomeSeed;
        this.biomeGenerators = biomeGenerators;
        if (!this.biomeGenerators.containsKey(owner)) {
            this.biomeGenerators.put(owner, new Pair<>(1D, 10));
        }
    }
    
    @SuppressWarnings("unchecked")
    public static BiomeSettings deserialize(Map<String, Object> args) {
        return new BiomeSettings(
                (String) args.get("owner"),
                (Integer) args.getOrDefault("sectionSize", 1000),
                (Integer) args.getOrDefault("biomeScale", 200),
                (Integer) args.getOrDefault("biomeSeed", 1),
                (HashMap<String, Pair<Double, Integer>>) args.getOrDefault("biomeGenerators", new HashMap<>()));
    }
    
    public void remapTerrainGenerators(String file) {
        HashMap<String, Pair<Double, Integer>> map = new HashMap<>();
        for (String s : biomeGenerators.keySet()) {
            if (!s.contains("/")) {
                map.put(file + "/" + s, biomeGenerators.get(s));
            } else {
                map.put(s, biomeGenerators.get(s));
            }
        }
        this.biomeGenerators = map;
    }
    public void demapTerrainGenerators() {
        HashMap<String, Pair<Double, Integer>> map = new HashMap<>();
        for (String s : biomeGenerators.keySet()) {
            if (s.contains("/")) {
                map.put(s.split("/")[1], biomeGenerators.get(s));
            } else {
                map.put(s, biomeGenerators.get(s));
            }
        }
        this.biomeGenerators = map;
    }
    
    public int getBiomeScale() {
        return biomeScale;
    }
    
    public void setBiomeScale(int biomeScale) {
        this.biomeScale = biomeScale;
    }
    
    public int getSectionSize() {
        return sectionSize;
    }
    
    public void setSectionSize(int sectionSize) {
        this.sectionSize = sectionSize;
    }
    
    public long getBiomeSeed() {
        return biomeSeed;
    }
    
    public void setBiomeSeed(long biomeSeed) {
        this.biomeSeed = biomeSeed;
    }
    
    public Set<String> getBiomes() {
        return biomeGenerators.keySet();
    }
    
    public HashMap<String, Pair<Double, Integer>> getBiomeGenerators() {
        return biomeGenerators;
    }
    
    public double getBiomeWeight() {
        return getBiomeWeight(owner);
    }
    
    public void setBiomeWeight(double weight) {
        setBiomeWeight(owner, weight);
    }
    
    public double getBiomeWeight(String biome) {
        return biomeGenerators.getOrDefault(biome, new Pair<>(-1D, -1)).getLeft();
    }
    
    public double getBiomeWeight(TerrainGenData biome) {
        return getBiomeWeight(biome.getName());
    }
    
    public boolean setBiomeWeight(String biome, double weight) {
        if (biomeGenerators.containsKey(biome)) {
            if (weight <= 0) {
                throw new IllegalArgumentException("Given weight can not be lower/equal as 0");
            }
//            biomeGenerators.put(biome, new Pair<>(weight, biomeGenerators.get(biome).getRight()));
            biomeGenerators.get(biome).setLeft(weight);
            return true;
        } else {
            return false;
        }
    }
    
    public int getBiomeSize() {
        return getBiomeSize(owner);
    }
    
    public void setBiomeSize(int size) {
        setBiomeSize(owner, size);
    }
    
    public int getBiomeSize(String biome) {
        return biomeGenerators.getOrDefault(biome, new Pair<>(-1D, -1)).getRight();
    }
    
    public int getBiomeSize(TerrainGenData biome) {
        return getBiomeSize(biome.getName());
    }
    
    public boolean setBiomeSize(String biome, int size) {
        if (biomeGenerators.containsKey(biome)) {
            if (size <= 0) {
                throw new IllegalArgumentException("Given size can not be lower/equal as 0");
            }
//            biomeGenerators.put(biome, new Pair<>(biomeGenerators.get(biome).getLeft(), size));
            biomeGenerators.get(biome).setRight(size);
            return true;
        } else {
            return false;
        }
    }
    
    public void addBiome(String biome, double weight, int size) {
        biomeGenerators.put(biome, new Pair<>(weight, size));
    }
    
    public void removeBiome(String biome) {
        if (!biome.equals(owner)) {
            biomeGenerators.remove(biome);
        }
    }
    
    public boolean containsBiome(String biome) {
        return biomeGenerators.containsKey(biome);
    }
    
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("sectionSize", sectionSize);
        map.put("biomeScale", biomeScale);
        map.put("biomeSeed", biomeSeed);
        map.put("biomeGenerators", biomeGenerators);
        return map;
    }
}
