package com.spaceman.terrainGenerator.fileHander;

import java.util.Collection;
import java.util.HashMap;

public class GettingFiles {

    private static HashMap<String, Files> list = new HashMap<>();

    public static void loadFiles() {
        list = new HashMap<>();

        list.put("terrainData", new Files("terrainData.yml"));
        list.put("worldData", new Files("worldData.yml"));

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

    public static void reloadFile(String file) {
        list.put(file.replace(".yml", ""), new Files(file + (file.endsWith(".yml") ? "" : ".yml")));
    }
}
