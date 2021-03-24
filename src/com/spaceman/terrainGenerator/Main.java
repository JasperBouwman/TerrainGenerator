package com.spaceman.terrainGenerator;

import com.spaceman.terrainGenerator.commands.Terrain;
import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import com.spaceman.terrainGenerator.modes.*;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.biome.BiomeSettings;
import com.spaceman.terrainGenerator.terrain.terrainNoise.Flatlands;
import com.spaceman.terrainGenerator.terrain.terrainNoise.SimplexOctaveNoise;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.initGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.saveGenerators;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.initTerrainWorlds;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.saveTerrainWorlds;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.registerModes;
import static com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise.registerNoises;

public class Main extends JavaPlugin {
    
    public static Main getInstance() {
        return JavaPlugin.getPlugin(Main.class);
    }
    
    public static <O> O getOrDefault(O object, O def) {
        return object == null ? def : object;
    }
    
    public static boolean parseBool(String b) {
        Boolean bo = parseBoolean(b);
        return bo != null && bo;
    }
    public static Boolean parseBoolean(String b) {
        if (b == null) {
            return null;
        }
        String bo = b.toLowerCase();
        
        if (Arrays.asList("true", "yes", "y").contains(bo)) {
            return true;
        } else if (Arrays.asList("false", "no", "n").contains(bo)) {
            return false;
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unused")
    public static void println(String text) {
        Pair<Integer, String> d = ___8d4rd3148796d_Xaf();
        if (d == null) {
            System.out.println(text + " (null:null)");
            return;
        }
        Bukkit.getLogger().log(Level.INFO,text + " (" + d.getLeft() + ":" + d.getRight() + ")");
    }
    
    private static Pair<Integer, String> ___8d4rd3148796d_Xaf() {
        boolean thisOne = false;
        int thisOneCountDown = 1;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            String methodName = element.getMethodName();
            int lineNum = element.getLineNumber();
            if (thisOne && (thisOneCountDown == 0)) {
                return new Pair<>(lineNum, element.getClassName());
            } else if (thisOne) {
                thisOneCountDown--;
            }
            if (methodName.equals("___8d4rd3148796d_Xaf")) {
                thisOne = true;
            }
        }
        return null;
    }
    
    public static void log(String s) {
        log(Level.INFO, s);
    }
    
    public static void log(Level l, String s) {
        Bukkit.getLogger().log(l, "[TerrainGenerator] " + s);
    }
    
    @Override
    public void onEnable() {
        
        /* filters
         * TerrainGenData properties
         */
        
        ConfigurationSerialization.registerClass(TreeTypes.TreeData.class, "TreeData");//todo register all other classes
        ConfigurationSerialization.registerClass(AddLake.LakeData.class, "LakeData");
        ConfigurationSerialization.registerClass(BiomeSettings.class, "BiomeSettings");
        ConfigurationSerialization.registerClass(SimplexOctaveNoise.class, "SimplexOctaveNoise");
        ConfigurationSerialization.registerClass(TerrainBlockData.class, "TerrainBlockData");
        ConfigurationSerialization.registerClass(Pair.class, "Pair");
        
        GettingFiles.loadFiles();
        
        Permissions.loadPermissionConfig(getFiles("terrainData"));

//        getCommand("terrain").setExecutor(new Terrain());
        new Terrain();
        
        registerNoises(Flatlands::new, SimplexOctaveNoise::new);
        
        registerModes(
                AddGrass::new,
                AddHouse::new,
                AddLava::new,
                AddTrees::new,
                AddWater::new,
                Bedrock::new,
                Layers::new,
                StoneGen::new,
                Top::new,
                TopR::new,
                TreeTypes::new,
                AddLake::new,
                TreeModifier::new);
        
        initGenerators();
        log("All TerrainGenerators are set up");
        initTerrainWorlds();
        log("All TerrainWorlds are set up");
    }
    
    @Override
    public void onDisable() {
        log("Saving TerrainGenerators");
        saveGenerators();
        log("Saving TerrainWorlds");
        saveTerrainWorlds();
    }
}
