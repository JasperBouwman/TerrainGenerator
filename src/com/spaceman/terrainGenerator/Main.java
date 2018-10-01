package com.spaceman.terrainGenerator;

import com.spaceman.terrainGenerator.commands.Terrain;
import com.spaceman.terrainGenerator.events.WorldEvents;
import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import com.spaceman.terrainGenerator.modes.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.initGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.saveGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainMode.registerModes;
import static com.spaceman.terrainGenerator.terrain.WorldGenerator.initTerrainWorlds;
import static com.spaceman.terrainGenerator.terrain.WorldGenerator.saveTerrainWorlds;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {

    public static void println(String text) {
        ClassData d = ___8d4rd3148796d_Xaf();
        if (d == null) {
            System.out.println(text + " (null:null)");
            return;
        }
        System.out.println(text + " (" + d.className + ":" + d.line + ")");
    }
    private static ClassData ___8d4rd3148796d_Xaf() {
        boolean thisOne = false;
        int thisOneCountDown = 1;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            String methodName = element.getMethodName();
            int lineNum = element.getLineNumber();
            if (thisOne && (thisOneCountDown == 0)) {
                return new ClassData(lineNum, element.getClassName());
            } else if (thisOne) {
                thisOneCountDown--;
            }
            if (methodName.equals("___8d4rd3148796d_Xaf")) {
                thisOne = true;
            }
        }
        return null;
    }

    @Override
    public void onEnable() {
        /*changelog todo:
        *
        * make WorldGenerator ready
        *
        * */

        /* filters
        * TerrainGenData properties
        * */


        new GettingFiles(this);

        getCommand("terrain").setExecutor(new Terrain());
        getCommand("terrain").setTabCompleter(new Terrain());
        Bukkit.getPluginManager().registerEvents(new WorldEvents(), this);

        registerModes(
                new AddGrass(),
                new AddHouse(),
                new AddLava(),
                new AddTrees(),
                new AddWater(),
                new Bedrock(),
                new Layers(),
                new StoneGen(),
                new Top(),
                new TopR(),
                new TreeTypes());

//        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            initGenerators();
//            Bukkit.getServer().getLogger().log(Level.INFO, "[" + this.getDescription().getName() + "] All TerrainGenerators are set up");
            log("All TerrainGenerators are set up");
            initTerrainWorlds();
//            Bukkit.getServer().getLogger().log(Level.INFO, "[" + this.getDescription().getName() + "] All TerrainWorlds are set up");
            log("All TerrainWorlds are set up");
//        });
    }

    @Override
    public void onDisable() {
//        Logger.getLogger("Minecraft").log(Level.INFO, "[" + this.getDescription().getName() + "] Saving TerrainGenerators");
        log("Saving TerrainGenerators");
        saveGenerators();
        log("Saving TerrainWorlds");
        saveTerrainWorlds();
    }

    public static void log(String s) {
        log(Level.INFO, s);
    }
    public static void log(Level l, String s) {
        Bukkit.getLogger().log(l, "[TerrainGenerator] " + s);
    }

    private static class ClassData {
        private final int line;
        private final String className;

        private ClassData(int line, String className) {
            this.line = line;
            this.className = className;
        }
    }
}
