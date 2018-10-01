package com.spaceman.terrainGenerator.terrain;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.terrainGenData;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TerrainGenData {

    public static final long worldSeedValue = -1;
    private double frequency = 5;
    private double amplitude = 0.01;
    private double multitude = 5;
    private double scale = 1 / 64.0;
    private int octaves = 8;
    private int height = 63;
    private long seed = 1;
    private boolean fromTop = true;
    private String start = "35";
    private TerrainBlockData terrainBlockData = new TerrainBlockData(Material.DIRT);
    private Biome biome = null;
    private String name;
    private LinkedList<TerrainMode> modes = new LinkedList<>();
    private LinkedList<String> generators = new LinkedList<>();

    private TerrainGenData(String name) {
        this.name = name;
    }

    public static TerrainGenData terrainGenData(String name) {
        return terrainGenData(name, true);
    }

    public static TerrainGenData terrainGenData(String name, boolean getExisting) {
        TerrainGenData newGenData = new TerrainGenData(name);

        if (!name.equals("null")) {
            TerrainGenData tmpData = TerrainGenerator.addGen(name, newGenData);
            if (tmpData != newGenData) {
                if (getExisting) {
                    return tmpData;
                } else {
                    return null;
                }
            }
        }
        return newGenData;
    }

    public void addMode(TerrainMode mode) {
        if (mode != null) {
            for (TerrainMode oldMode : modes) {
                if (oldMode.getModeName().equals(mode.getModeName())) {
                    modes.remove(oldMode);
                    this.modes.add(mode);
                    return;
                }
            }
            this.modes.add(mode);
        }
    }

    public boolean removeMode(TerrainMode mode) {
        return modes.remove(mode);
    }

    public boolean removeMode(String mode) {
        for (TerrainMode terrainMode : modes) {
            if (terrainMode.getModeName().equals(mode)) {
                modes.remove(terrainMode);
                return true;
            }
        }
        return false;
    }

    public void addModes(LinkedList<TerrainMode> modes) {
        this.modes.addAll(modes);
    }

    public LinkedList<TerrainMode> getModes() {
        return modes;
    }

    public void setModes(LinkedList<TerrainMode> modes) {
        this.modes = modes;
    }

    public LinkedList<String> getGenerators() {
        return generators;
    }

    public void setGenerators(LinkedList<String> generators) {
        this.generators = generators;
    }

    public void addGenerator(String genDataName) {
        if (!generators.contains(genDataName)) {
            generators.add(genDataName);
        }
    }

    public void removeGenerator(String genDataName) {
        generators.remove(genDataName);
    }

    public boolean hadMode(String mode) {
        return (getMode(mode) == null);
    }

    public TerrainMode getMode(String mode) {

        for (TerrainMode tmpMode : getModes()) {
            if (tmpMode.getModeName().equals(mode)) {
                return tmpMode;
            }
        }
        return null;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public int getOctaves() {
        return octaves;
    }

    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    public double getMultitude() {
        return multitude;
    }

    public void setMultitude(double multitude) {
        this.multitude = multitude;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getStart(int x, int z, World world) {

        try {
            return Integer.parseInt(start);
        } catch (NumberFormatException nfe) {

            TerrainGenData i = terrainGenData.get(start);
            if (i == null) {
                return 1;
            }
            long seed = i.getSeed();
            if (seed == worldSeedValue) {
                seed = world.getSeed();
            }
            SimplexOctaveGenerator gen = new SimplexOctaveGenerator(seed, i.getOctaves());
            gen.setScale(i.getScale());

            return (int) (gen.noise(x, z, i.getFrequency(), i.getAmplitude()) * i.getMultitude() + i.getHeight());
        }
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setStart(int start) {
        this.start = String.valueOf(start);
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public TerrainBlockData getTerrainBlockData() {
        return terrainBlockData;
    }

    public void setTerrainBlockData(TerrainBlockData terrainBlockData) {
        this.terrainBlockData = terrainBlockData;
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public String getName() {
        return name;
    }

    public boolean getFromTop() {
        return fromTop;
    }

    public void setFromTop(boolean fromTop) {
        this.fromTop = fromTop;
    }

}