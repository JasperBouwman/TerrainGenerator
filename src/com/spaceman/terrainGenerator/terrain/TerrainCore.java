package com.spaceman.terrainGenerator.terrain;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.terrain.biome.BiomeSettings;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeContainsGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeInverse;
import com.spaceman.terrainGenerator.terrain.terrainNoise.SimplexOctaveNoise;
import com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.customWorlds;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.getNewMode;
import static com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise.getNewNoise;

public class TerrainCore {
    
    public static HashMap<String, TerrainGenData> terrainGenData = new HashMap<>();
    
    public static void setType(final Location l, final Material material, BlockFace blockFace) {
        setType(l.getBlock(), material, blockFace);
    }
    
    public static void setType(final Location l, final Material material) {
        setType(l.getBlock(), material);
    }
    
    public static void setType(final Block b, final Material material) {
        new BukkitRunnable() {
            public void run() {
                b.setType(material);
            }
        }.runTask(Main.getInstance());
    }
    
    public static void setType(final Block block, final Material material, BlockFace blockFace) {
        setType(block, material, blockFace, null);
    }
    
    public static void setType(final Block block, BlockData data) {
        Material m = data.getMaterial();
        BlockFace d = null;
        Boolean w = null;
        if (data instanceof Orientable) {
            d = convertAxisToBlockFace(((Orientable) data).getAxis());
        }
        if (data instanceof Directional) {
            d = ((Directional) data).getFacing();
        }
        if (data instanceof Rotatable) {
            d = ((Rotatable) data).getRotation();
        }
        if (data instanceof Waterlogged) {
            w = ((Waterlogged) data).isWaterlogged();
        }
        
        setType(block, m, d, w);
    }
    
    public static void setType(Block block, Material material, BlockFace blockFace, Boolean waterLogged) {
        new BukkitRunnable() {
            public void run() {
                block.setType(material);
                BlockData blockData = block.getBlockData();
                try {
                    if (blockFace != null) {
                        if (blockData instanceof Directional) {
                            ((Directional) blockData).setFacing(blockFace);
                        }
                        if (blockData instanceof Orientable) {
                            ((Orientable) blockData).setAxis(convertBlockFaceToAxis(blockFace));
                        }
                        if (blockData instanceof Rotatable) {
                            ((Rotatable) blockData).setRotation(blockFace);
                        }
                    }
                    if (waterLogged != null && blockData instanceof Waterlogged) {
                        ((Waterlogged) blockData).setWaterlogged(waterLogged);
                    }
                    if (blockData instanceof Leaves) {
                        ((Leaves) blockData).setPersistent(true);
                    }
                } catch (Error | Exception ignore) {
                }
                block.setBlockData(blockData);
            }
        }.runTask(Main.getInstance());
    }
    
    public static void setType(Block block, TerrainBlockData terrainBlockData) {
        setType(block, terrainBlockData.getMaterial(), terrainBlockData.getBlockFace(), terrainBlockData.isWaterLogged());
    }
    
    public static Axis convertBlockFaceToAxis(BlockFace face) {
        switch (face) {
            case NORTH:
            case SOUTH:
                return Axis.Z;
            case UP:
            case DOWN:
                return Axis.Y;
            case EAST:
            case WEST:
            default:
                return Axis.X;
        }
    }
    
    public static BlockFace convertAxisToBlockFace(Axis axis) {
        switch (axis) {
            case X:
            default:
                return BlockFace.EAST;
            case Y:
                return BlockFace.UP;
            case Z:
                return BlockFace.NORTH;
        }
    }
    
    public static TerrainGenData addGen(TerrainGenData data) {
        if (terrainGenData.containsKey(data.getName())) {
            return terrainGenData.get(data.getName());
        } else {
            terrainGenData.put(data.getName(), data);
            return data;
        }
    }
    
    public static void removeGen(String name) throws TerrainGeneratorUsedException, IllegalArgumentException {
        TerrainGenData data = getGen(name);
        
        if (data != null) {
            if (data.getName().contains("/")) {
                throw new IllegalArgumentException(formatError("Can't delete an external TerrainGenerator"));
            }
            
            for (String worldName : customWorlds.keySet()) {
                if (customWorlds.get(worldName).getTerrainGeneratorName().equals(data.getName())) {
                    throw new TerrainGeneratorUsedException(formatError("TerrainGenerator is used in the TerrainWorld %s therefor could not be deleted", worldName));
                }
            }
            
            for (TerrainGenData tmpData : terrainGenData.values()) {
                if (!tmpData.getName().contains("/")) {
                    tmpData.getGenerators().remove(data.getName());
                    tmpData.getBiomeSettings().removeBiome(data.getName());
                    for (TerrainMode terrainMode : tmpData.getModes()) {
                        if (terrainMode instanceof TerrainModeContainsGenerator) {
                            ((TerrainModeContainsGenerator) terrainMode).checkGenerator(data.getName());
                        }
                    }
                }
            }
            terrainGenData.remove(name);
        } else {
            throw new IllegalArgumentException(formatError("TerrainGenerator %s does not exist", name));
        }
    }
    
    public static TerrainGenData getGen(String name) {
        return terrainGenData.getOrDefault(name, null);
    }
    
    public static Set<String> getGens() {
        return terrainGenData.keySet();
    }
    
    public static boolean containsGen(String name) {
        return terrainGenData.containsKey(name);
    }
    
    public static void saveGenerators() {
        Files terrainData = getFiles("terrainData");
        
        terrainData.getConfig().set("terrainGenData", null);
        for (String name : terrainGenData.keySet()) {
            TerrainGenData genData = terrainGenData.get(name);
            if (!name.contains("/")) {
                try {
                    saveGenerator(terrainData, genData);
                } catch (Exception e) {
                    Main.log(Level.WARNING, "TerrainGenerator " + name + " could not be fully saved");
                }
            }
        }
    }
    
    public static void saveGenerator(Files terrainData, TerrainGenData genData) {
        String name = genData.getName();
        //TerrainGenData properties
        terrainData.getConfig().set("terrainGenData." + name + ".data.name", name);
        terrainData.getConfig().set("terrainGenData." + name + ".data.creator", genData.getCreator());
        terrainData.getConfig().set("terrainGenData." + name + ".data.fastRender", genData.isFastRender());
        terrainData.getConfig().set("terrainGenData." + name + ".data.height", genData.getHeight());
        terrainData.getConfig().set("terrainGenData." + name + ".data.startGen", genData.getStart());
        terrainData.getConfig().set("terrainGenData." + name + ".data.max", genData.getMax());
        terrainData.getConfig().set("terrainGenData." + name + ".data.min", genData.getMin());
        terrainData.getConfig().set("terrainGenData." + name + ".data.seed", genData.getSeed());
        terrainData.getConfig().set("terrainGenData." + name + ".data.fromTop", genData.getFromTop());
        terrainData.getConfig().set("terrainGenData." + name + ".data.noise", genData.getTerrainNoise());
        genData.getTerrainNoise().saveNoise(terrainData.getConfig().createSection("terrainGenData." + name + ".data.noise." + genData.getTerrainNoise().getName()));
        terrainData.getConfig().set("terrainGenData." + name + ".data.terrainBlockData", genData.getTerrainBlockData());
        terrainData.getConfig().set("terrainGenData." + name + ".data.generators", genData.getGenerators());
        terrainData.getConfig().set("terrainGenData." + name + ".data.biome", genData.getBiome() == null ? null : genData.getBiome().name());
        terrainData.getConfig().set("terrainGenData." + name + ".data.biomeSettings", genData.getBiomeSettings());
        
        //save terrainModes
        for (TerrainMode mode : genData.getModes()) {
//            String savePath = "terrainGenData." + name + ".terrainMode." + mode.getModeName() + ".data";
            try {
                mode.saveMode(terrainData.getConfig().createSection("terrainGenData." + name + ".terrainMode." + mode.getModeName()));
//                mode.saveMode(terrainData.getConfig().getConfigurationSection("terrainGenData." + name + ".terrainMode." + mode.getModeName()));
                
                if (mode instanceof TerrainModeInverse) {
                    terrainData.getConfig().set("terrainGenData." + name + ".terrainMode." + mode.getModeName() + ".inverse", ((TerrainModeInverse) mode).isInverse());
                }
            } catch (Exception e) {
                Main.log(Level.WARNING, "TerrainMode " + mode.getModeName() + " in the TerrainGenerator " + name + " could not be fully saved");
                e.printStackTrace();
            }
        }
        terrainData.saveConfig();
    }
    
    public static void initGenerators() {
        terrainGenData = new HashMap<>();
        
        Files terrainData = getFiles("terrainData");
        if (terrainData.getConfig().contains("terrainGenData")) {
            for (String name : terrainData.getKeys("terrainGenData")) {
                try {
                    initGenerator("", terrainData.getConfig().getConfigurationSection("terrainGenData." + name));
                } catch (Exception e) {
                    Main.log(Level.WARNING, "The TerrainGenerator " + name + " could not be fully loaded. " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        File externalTerrainGenerators = new File(Main.getInstance().getDataFolder() + "/etf");
        if (externalTerrainGenerators.exists()) {
            File[] files = externalTerrainGenerators.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.getName().contains(" ")) {
                            Main.log(Level.WARNING, "External Terrain File '" + file.getName() + "' could not be loaded, these files can't contain any spaces");
                        } else {
                            Files externalData = new Files("/etf", file.getName());
                            
                            if (externalData.getConfig().contains("terrainGenData")) {
                                for (String name : externalData.getKeys("terrainGenData")) {
                                    try {
                                        initGenerator(file.getName().replaceAll("\\.yml.{0}", "") + "/",
                                                externalData.getConfig().getConfigurationSection("terrainGenData." + name));
                                    } catch (Exception e) {
                                        Main.log(Level.WARNING, "The TerrainGenerator " + name + " could not be fully loaded. " + e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void initGenerator(String prefix, ConfigurationSection section) {
        if (section == null) {
            return;
        }
        String name = section.getString("data.name");
        TerrainGenData genData = terrainGenData(prefix + name, true);
        
        if (genData == null) {
            return;
        }
        
        //TerrainGenData properties
        genData.setCreator(section.getString("data.creator", genData.getCreator()));
        genData.setFastRender(section.getBoolean("data.fastRender", genData.isFastRender()));
        genData.setHeight(section.getInt("data.height", genData.getHeight()));
        genData.setStart(section.getString("data.startGen", genData.getStart()));
        genData.setMax(section.getString("data.max", genData.getMax()));
        genData.setMin(section.getString("data.min", genData.getMin()));
        genData.setSeed(section.getLong("data.seed", genData.getSeed()));
        genData.setFromTop(section.getBoolean("data.fromTop", genData.getFromTop()));
        
        if (section.contains("data.noise")) {
            //noinspection ConstantConditions, 'data.noise' exists
            String noiseName = section.getConfigurationSection("data.noise").getKeys(false).iterator().next();
            try {
                TerrainNoise noise = getNewNoise(noiseName);
                if (noise != null) {
                    genData.setTerrainNoise(noise.loadNoise(section.getConfigurationSection("data.noise." + noiseName)));
                } else {
                    throw new NullPointerException("Given Noise is not registered");
                }
            } catch (Exception e) {
                Main.log(Level.WARNING, "The TerrainNoise " + noiseName + " in the TerrainGenerator " + genData.getName() + " could not be loaded. Falling back to the default. " + e.getMessage());
                genData.setTerrainNoise(new SimplexOctaveNoise());
            }
        } else {
            if (section.contains("data.frequency") && section.contains("data.amplitude") && section.contains("data.multitude") && section.contains("data.scale")) {
                SimplexOctaveNoise newNoise = (SimplexOctaveNoise) getNewNoise("simplexOctaveNoise");
                newNoise.setFrequency(section.getDouble("data.frequency", newNoise.getFrequency()));
                newNoise.setAmplitude(section.getDouble("data.amplitude", newNoise.getAmplitude()));
                newNoise.setMultitude(section.getDouble("data.multitude", newNoise.getMultitude()));
                newNoise.setScale(section.getDouble("data.scale", newNoise.getScale()));
                genData.setTerrainNoise(newNoise);
                Main.log(Level.INFO, "Converted some data into a simplexOctaveNoise TerrainNoise in TerrainGenerator " + genData.getName());
            } else {
                Main.log(Level.WARNING, "The TerrainNoise in the TerrainGenerator " + genData.getName() + " does not exist. Falling back to the default");
                genData.setTerrainNoise(new SimplexOctaveNoise());
            }
        }
        
        genData.setTerrainBlockData((TerrainBlockData) section.get("data.terrainBlockData", genData.getTerrainBlockData()));
        genData.setGenerators(new LinkedList<>(section.getStringList("data.generators")));
        if (section.contains("data.biome")) {
            genData.setBiome(Biome.valueOf(section.getString("data.biome")));
        }
        genData.setBiomeSettings((BiomeSettings) section.get("data.biomeSettings", genData.getBiomeSettings()));
        genData.setModes(new LinkedList<>());
        
        if (section.contains("terrainMode")) {
            //noinspection ConstantConditions, 'terrainMode' exists
            for (String modeName : section.getConfigurationSection("terrainMode").getKeys(false)) {
                try {
                    TerrainMode terrainMode = getNewMode(modeName);
                    if (terrainMode != null) {
                        genData.addMode(terrainMode.loadMode(section.getConfigurationSection("terrainMode." + modeName)));
                        
                        if (terrainMode instanceof TerrainModeInverse) {
                            if (section.contains("terrainMode." + terrainMode.getModeName() + ".inverse")) {
                                ((TerrainModeInverse) terrainMode).setInverse(section.getBoolean("terrainMode." + terrainMode.getModeName() + ".inverse"));
                            }
                        }
                    } else {
                        throw new NullPointerException("Given TerrainMode is not registered");
                    }
                } catch (Exception e) {
                    Main.log(Level.WARNING, "The TerrainMode " + modeName + " in the TerrainGenerator " + genData.getName() + " could not be fully loaded. " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
