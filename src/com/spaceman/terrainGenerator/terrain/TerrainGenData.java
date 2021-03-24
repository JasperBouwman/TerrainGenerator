package com.spaceman.terrainGenerator.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.biome.BiomeSettings;
import com.spaceman.terrainGenerator.terrain.terrainNoise.SimplexOctaveNoise;
import com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.infoColor;
import static com.spaceman.terrainGenerator.ColorFormatter.varInfoColor;
import static com.spaceman.terrainGenerator.commands.terrain.SafeToEdit.SafeToEditClass.safeToEdit;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.*;

@SuppressWarnings({"unused"})
public class TerrainGenData {

    public static final long worldSeedValue = -1;

    private String creator = "Server";
    private TerrainNoise terrainNoise = new SimplexOctaveNoise();
    private int height = 63;
    private long seed = 1;
    private boolean fromTop = true;
    private String start = "35";
    private String min = "0";
    private String max = "256";
    private boolean fastRender = false;
    private TerrainBlockData terrainBlockData = new TerrainBlockData(Material.DIRT);
    private Biome biome = null;
    private String name;
    private LinkedList<TerrainMode> modes = new LinkedList<>();
    private LinkedList<String> generators = new LinkedList<>();

    private HashMap<String, Double> biomes = new HashMap<>();
    private BiomeSettings biomeSettings;

    private TerrainGenData(String name) {
        this.name = name;
        biomeSettings = new BiomeSettings(name);
    }

    public static TerrainGenData terrainGenData(String name) {
        return terrainGenData(name, true);
    }
    public static TerrainGenData terrainGenData(String name, boolean getExisting) {
        TerrainGenData newGenData = new TerrainGenData(name);

        if (!name.equals("null")) {
            TerrainGenData tmpData = addGen(newGenData);
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

    public TerrainNoise getTerrainNoise() {
        return terrainNoise;
    }

    public void setTerrainNoise(TerrainNoise terrainNoise) {
        this.terrainNoise = terrainNoise;
    }

    public void addMode(TerrainMode mode) {
        if (mode != null) {
            for (TerrainMode oldMode : modes) {
                if (oldMode.getModeName().equals(mode.getModeName())) {
                    this.modes.remove(oldMode);
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
    public TerrainMode getMode(String mode) {

        for (TerrainMode tmpMode : getModes()) {
            if (tmpMode.getModeName().equals(mode)) {
                return tmpMode;
            }
        }
        return null;
    }
    public void addModes(LinkedList<TerrainMode> modes) {
        this.modes.addAll(modes);
    }
    public LinkedList<TerrainMode> getModes() {
        return modes;
    }
    public boolean hasMode(String mode) {
        return (getMode(mode) == null);
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
    public void remapGenerators(String file) {
        generators = generators.stream().map(data -> file + "/" + data).collect(Collectors.toCollection(LinkedList::new));
        biomeSettings.remapTerrainGenerators(file);
    }
    public void demapGenerators() {
        generators = generators.stream().filter(data -> data.contains("/")).map(data -> data.split("/")[1]).collect(Collectors.toCollection(LinkedList::new));
        biomeSettings.demapTerrainGenerators();
    }
    
    public boolean isFastRender() {
        return fastRender;
    }
    public void setFastRender(boolean fastRender) {
        this.fastRender = fastRender;
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
            return (i.getTerrainNoise().noise(x, z, seed) + i.getHeight());
        }
    }
    public String getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = String.valueOf(start);
    }
    public void setStart(String start) {
        this.start = start;
    }

    public String getMin() {
        return min;
    }
    public int getMin(int x, int z, World world) {
        try {
            return Integer.parseInt(min);
        } catch (NumberFormatException nfe) {
            TerrainGenData i = terrainGenData.get(min);
            if (i == null) {
                return 0;
            }
            long seed = i.getSeed();
            if (seed == worldSeedValue) {
                seed = world.getSeed();
            }
            return (i.getTerrainNoise().noise(x, z, seed) + i.getHeight());
        }
    }
    public void setMin(int min) {
        this.min = String.valueOf(min);
    }
    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }
    public int getMax(int x, int z, World world) {
        try {
            return Integer.parseInt(max);
        } catch (NumberFormatException nfe) {
            TerrainGenData i = terrainGenData.get(max);
            if (i == null) {
                return 256;
            }
            long seed = i.getSeed();
            if (seed == worldSeedValue) {
                seed = world.getSeed();
            }
            return (i.getTerrainNoise().noise(x, z, seed) + i.getHeight());
        }
    }
    public void setMax(int max) {
        this.max = String.valueOf(max);
    }
    public void setMax(String max) {
        this.max = max;
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

    public BiomeSettings getBiomeSettings() {
        return biomeSettings;
    }
    public void setBiomeSettings(BiomeSettings biomeSettings) {
        this.biomeSettings = biomeSettings;
    }
    
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public static HoverEvent toHoverEvent(String generator) {
        TerrainGenData data = getGen(generator);
        if (data == null) {
            return hoverEvent("Not available", ColorFormatter.errorColor);
        }
        return data.toHoverEvent();
    }
    
    public HoverEvent toHoverEvent() {
        HoverEvent hEvent = hoverEvent("");
    
        //TerrainGenData properties
        hEvent.addText(textComponent("Creator: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getCreator() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nFastRender: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.isFastRender() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nHeight: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getHeight() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nSeed: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getSeed() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nFromTop: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getFromTop() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nStart: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getStart() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nMaterial: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getTerrainBlockData().getMaterial().name(), ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nDirection: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getTerrainBlockData().getBlockFace().name(), ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nWaterLogged: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getTerrainBlockData().isWaterLogged() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nMax: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getMax() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nMin: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getMin() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nBiome: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(this.getBiome() + "", ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nNoiseGenerator: ", ColorFormatter.infoColor));
        hEvent.addMessage(this.getTerrainNoise().dataAsStringWithHover());
        hEvent.addText(textComponent("\nSafeToEdit: ", ColorFormatter.infoColor));
        boolean safeToEdit = safeToEdit(this.getName()).isSafeToEdit();
        hEvent.addText(textComponent(safeToEdit + "", (safeToEdit ? ChatColor.GREEN : ChatColor.RED)));
        hEvent.addText(textComponent("\nGenerators: ", ColorFormatter.infoColor));
        hEvent.addText("");
        for (String gen : this.getGenerators()) {
            hEvent.addText(textComponent(gen, ColorFormatter.varInfoColor));
            hEvent.addText(textComponent(", ", ColorFormatter.infoColor));
        }
        hEvent.removeLast();
    
        hEvent.addText(textComponent("\nModes: ", ColorFormatter.infoColor));
        hEvent.addText("");
        for (TerrainMode mode : this.getModes()) {
            hEvent.addText(textComponent(mode.getModeName(), ColorFormatter.varInfoColor));
            hEvent.addText(textComponent(", ", ColorFormatter.infoColor));
        }
        hEvent.removeLast();
    
        hEvent.addText(textComponent("\nBiomeSettings: {", ColorFormatter.infoColor));
        hEvent.addText(textComponent("\n Section size: ", infoColor));
        hEvent.addText(textComponent(String.valueOf(this.getBiomeSettings().getSectionSize()), varInfoColor));
        hEvent.addText(textComponent("\n Biome scale: ", infoColor));
        hEvent.addText(textComponent(String.valueOf(this.getBiomeSettings().getBiomeScale()), varInfoColor));
        hEvent.addText(textComponent("\n Biome seed: ", infoColor));
        hEvent.addText(textComponent(String.valueOf(this.getBiomeSettings().getBiomeSeed()), varInfoColor));
        hEvent.addText("\n Biomes: ", infoColor);
        for (String biome : this.getBiomeSettings().getBiomes()) {
            hEvent.addText(textComponent(biome, varInfoColor));
            hEvent.addText(", ", infoColor);
        }
        hEvent.removeLast();
        hEvent.addText("\n}", infoColor);
    
        return hEvent;
    }
}