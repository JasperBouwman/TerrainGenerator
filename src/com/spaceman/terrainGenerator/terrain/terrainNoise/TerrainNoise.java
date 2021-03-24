package com.spaceman.terrainGenerator.terrain.terrainNoise;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

import static com.spaceman.terrainGenerator.commandHander.SubCommand.lowerCaseFirst;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.containsSpecialCharacter;

public abstract class TerrainNoise {

    private static HashMap<String, NoiseCreator> noises = new HashMap<>();

    public static boolean registerNoise(NoiseCreator noise) {
        String name = noise.create().getName();

        if (containsSpecialCharacter(name)) {
            return false;
        }

        if (!noises.containsKey(name)) {
            noises.put(name, noise);
            return true;
        }
        return false;
    }

    public static HashMap<NoiseCreator, Boolean> registerNoises(NoiseCreator... noises) {
        HashMap<NoiseCreator, Boolean> map = new HashMap<>();
        for (NoiseCreator noise : noises) {
            map.put(noise, registerNoise(noise));
        }
        return map;
    }

    public static Set<String> getNoises() {
        return noises.keySet();
    }

    public static TerrainNoise getNewNoise(String noiseName) {
        return getNewNoise(noiseName, null, null);
    }

    public static TerrainNoise getNewNoise(String noiseName, LinkedList<String> data, Player player) {

        if (noises.containsKey(noiseName)) {
            try {
                TerrainNoise noise = noises.get(noiseName).create();

                if (data != null && player != null) {
                    noise.editNoiseSettings(data, player);
                }

                return noise;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public String getName() {
        return lowerCaseFirst(this.getClass().getSimpleName());
    }
    
    public abstract Message dataAsString();
    public abstract Message dataAsStringWithHover();

    public abstract String getDescription();

    public Collection<String> tabList(String[] args, Player player) {
        return Collections.emptyList();
    }

    public abstract void editNoiseSettings(List<String> data, Player player);

    public abstract void saveNoise(ConfigurationSection section);

    public abstract TerrainNoise loadNoise(ConfigurationSection section);

    public abstract int noise(int x, int z, long seed);

}
