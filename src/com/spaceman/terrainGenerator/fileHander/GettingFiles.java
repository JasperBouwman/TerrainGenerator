package com.spaceman.terrainGenerator.fileHander;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;

public class GettingFiles {

    public static JavaPlugin p;
    private static HashMap<String, Files> list;

    public GettingFiles(JavaPlugin main) {

        list = new HashMap<>();
        p = main;

        list.put("terrainData", new Files(main, "terrainData.yml"));
        list.put("worldData", new Files(main, "worldData.yml"));

    }

    public static void addFile(String fileName, Files file) {
        list.put(fileName, file);
    }

    public static Files getFiles(String file) {
        return list.getOrDefault(file.replace(".yml", ""), null);
    }

    public static Collection<Files> getFiles() {
        return list.values();
    }

    public static void reloadFile(String file, JavaPlugin p) {
        list.put(file.replace(".yml", ""), new Files(p, file + (file.endsWith(".yml") ? "" : ".yml")));
    }
}
