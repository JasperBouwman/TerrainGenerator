package com.spaceman.terrainGenerator.commands;

import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;

public final class TabCompletes {

    public static ArrayList<String> availableGenerators() {
        return new ArrayList<>(terrainGenData.keySet());
    }

    public static List<String> ownGenerators() {
        return availableGenerators().stream().filter(s -> !s.contains("/")).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static List<String> dataTabList(int startArg, String[] args) {
        //TerrainGenData properties

        String lastArg = args[args.length - 1];
        if (lastArg.startsWith("material=")) {
            ArrayList<String> list = new ArrayList<>();
            for (Material m : TerrainUtils.blockMaterials) {
                list.add("material=" + m.name());
            }
            return list;
        } else if (lastArg.startsWith("start=")) {
            List<String> genList = availableGenerators();
            ArrayList<String> list = new ArrayList<>();
            for (String s : genList) {
                list.add("start=" + s);
            }
            list.add("start=<X>");
            return list;
        } else if (lastArg.startsWith("min=")) {
            List<String> genList = availableGenerators();
            ArrayList<String> list = new ArrayList<>();
            for (String s : genList) {
                list.add("min=" + s);
            }
            list.add("min=<X>");
            return list;
        } else if (lastArg.startsWith("max=")) {
            List<String> genList = availableGenerators();
            ArrayList<String> list = new ArrayList<>();
            for (String s : genList) {
                list.add("max=" + s);
            }
            list.add("max=<X>");
            return list;
        } else if (lastArg.startsWith("seed=")) {
            return Arrays.asList("seed=<X>", "seed=world", "seed=random");
        } else if (lastArg.startsWith("fromTop=")) {
            return Arrays.asList("fromTop=false", "fromTop=true");
        } else if (lastArg.startsWith("fastRender=")) {
            return Arrays.asList("fastRender=false", "fastRender=true");
        } else if (lastArg.startsWith("waterLogged=")) {
            return Arrays.asList("waterLogged=false", "waterLogged=true", "waterLogged=null");
        } else if (lastArg.startsWith("direction=")) {
            ArrayList<String> list = new ArrayList<>();
            for (BlockFace b : BlockFace.values()) {
                list.add("direction=" + b.name());
            }
            return list;
        } else if (lastArg.startsWith("biome=")) {
            Biome[] biomes = Biome.values();
            ArrayList<String> list = new ArrayList<>();
            for (Biome biome : biomes) {
                list.add("biome=" + biome.name());
            }
            return list;
        }

        ArrayList<String> newList = new ArrayList<>(Arrays.asList("height=",
                "seed=", "fromTop=", "start=", "material=", "direction=", "biome=", "waterLogged=", "max=", "min=", "fastRender="));
        for (int i = startArg; i < args.length; i++) {
            for (String data : (ArrayList<String>) newList.clone()) {
                if (args[i].startsWith(data)) {
                    newList.remove(data);
                }
            }
        }

        return newList;
    }
}
