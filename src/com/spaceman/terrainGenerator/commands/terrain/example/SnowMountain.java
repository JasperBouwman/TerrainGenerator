package com.spaceman.terrainGenerator.commands.terrain.example;

import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import com.spaceman.terrainGenerator.modes.*;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

import static com.spaceman.terrainGenerator.terrain.TerrainGenData.terrainGenData;

public class SnowMountain extends ExampleHandler {
    @Override
    public String name() {
        return "SnowMountain";
    }

    @Override
    public void generate(int x, int z, Player player) {
        new Thread(() -> generateGens(x, z, player)).start();
    }

    private void generateGens(int x, int z, Player player) {
        new Thread(() -> {
            createGens();
            player.sendMessage(ChatColor.GREEN + "Generating...");
            long time = System.currentTimeMillis();
            TerrainGenerator.generate("iExamplesTG-floor", x, z, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Generated, time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - time) / 1000 + ChatColor.GREEN + " seconds");
            long newTime = System.currentTimeMillis();
            player.sendMessage(ChatColor.GREEN + "Loading terrain...");
            GettingFiles.p.getServer().getScheduler().scheduleSyncDelayedTask(GettingFiles.p, () ->
                    player.sendMessage(ChatColor.GREEN + "Terrain loaded, time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - newTime) / 1000 + ChatColor.GREEN + " seconds\n" +
                            "Total time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - time) / 1000 + ChatColor.GREEN + " seconds")
            );
            TerrainGenerator.removeGen("iExamplesTG-floorStart");
            TerrainGenerator.removeGen("iExamplesTG-roof");
            TerrainGenerator.removeGen("iExamplesTG-d1");
            TerrainGenerator.removeGen("iExamplesTG-d2");
            TerrainGenerator.removeGen("iExamplesTG-d3");
            TerrainGenerator.removeGen("iExamplesTG-floor");
        }).start();

    }

    @Override
    public void sendData(Player player) {
        createGens();

        this.sendData(player, "iExamplesTG-floorStart");
        this.sendData(player, "iExamplesTG-roof");
        this.sendData(player, "iExamplesTG-d1");
        this.sendData(player, "iExamplesTG-d2");
        this.sendData(player, "iExamplesTG-d3");
        this.sendData(player, "iExamplesTG-floor");

        TerrainGenerator.removeGen("iExamplesTG-floorStart");
        TerrainGenerator.removeGen("iExamplesTG-roof");
        TerrainGenerator.removeGen("iExamplesTG-d1");
        TerrainGenerator.removeGen("iExamplesTG-d2");
        TerrainGenerator.removeGen("iExamplesTG-d3");
        TerrainGenerator.removeGen("iExamplesTG-floor");
    }

    private void createGens() {
        TerrainGenData floorStart = terrainGenData("iExamplesTG-floorStart");
        floorStart.setHeight(150);
        floorStart.setStart(floorStart.getHeight() - 3);
        floorStart.setSeed(123745);

        TerrainGenData roof = terrainGenData("iExamplesTG-roof");
        roof.setStart(floorStart.getName());
        roof.setHeight(floorStart.getHeight() + 5);
        roof.setSeed(10);
        roof.setTerrainBlockData(new TerrainBlockData(Material.SPONGE));
        LinkedHashMap<TerrainBlockData, Integer> layers = new LinkedHashMap<>();
        layers.put((new TerrainBlockData(Material.LAPIS_BLOCK)), 1);
        layers.put((new TerrainBlockData(Material.WHITE_WOOL)), 4);
        layers.put((new TerrainBlockData(Material.QUARTZ_BLOCK)), 5);
        Layers newLayers2 = new Layers();
        newLayers2.setModeData(layers);
        roof.addMode(newLayers2);

        TerrainGenData d3 = terrainGenData("iExamplesTG-d3");
        d3.setStart(60);
        d3.setHeight(110);
        d3.setTerrainBlockData(new TerrainBlockData(Material.SNOW_BLOCK));
        d3.setSeed(435678);
        Layers newLayers4 = new Layers();
        LinkedHashMap<TerrainBlockData, Integer> layers4 = new LinkedHashMap<>();
        layers4.put(new TerrainBlockData(Material.SNOW_BLOCK), 5);
        layers4.put(new TerrainBlockData(Material.STONE), 100);
        newLayers4.setModeData(layers4);
        d3.addMode(newLayers4);
        Bedrock bedrock = new Bedrock();
        bedrock.setModeData(true);
        d3.addMode(bedrock);

        TerrainGenData d1 = terrainGenData("iExamplesTG-d1");
        d1.setSeed(34856732);
        d1.setTerrainBlockData(new TerrainBlockData(Material.STONE));
        d1.setStart(60);
        d1.setHeight(100);
        d1.setMultitude(30);
        LinkedHashMap<TerrainBlockData, Integer> layers1 = new LinkedHashMap<>();
        layers1.put((new TerrainBlockData(Material.COBBLESTONE)), 4);
        layers1.put((new TerrainBlockData(Material.DIRT)), 3);
        Layers newLayers1 = new Layers();
        newLayers1.setModeData(layers1);
        d1.addMode(newLayers1);
        StoneGen newStoneGen1 = new StoneGen();
        newStoneGen1.setModeData(d3.getName());
        d1.addMode(newStoneGen1);

        TerrainGenData d2 = terrainGenData("iExamplesTG-d2");
        d2.setSeed(58736785);
        d2.setTerrainBlockData(new TerrainBlockData(Material.ICE));
        d2.setStart(d1.getStart());
        d2.setMultitude(10);
        d2.setHeight(d1.getHeight() - 10);

        TerrainGenData floor = terrainGenData("iExamplesTG-floor");
        floor.setStart(60);
        floor.setHeight(d1.getHeight());
        floor.setAmplitude(0.1);
        floor.setMultitude(d1.getMultitude());
        floor.setTerrainBlockData(new TerrainBlockData(Material.DIRT));
        LinkedHashMap<TerrainBlockData, Integer> layers3 = new LinkedHashMap<>();
        layers3.put((new TerrainBlockData(Material.GRASS_BLOCK)), 1);
        layers3.put((new TerrainBlockData(Material.DIRT)), 3);
        layers3.put((new TerrainBlockData(Material.STONE)), 100);
        Layers newLayers3 = new Layers();
        newLayers3.setModeData(layers3);
        floor.addMode(newLayers3);
        LinkedHashMap<TerrainBlockData, Integer> addGrass = new LinkedHashMap<>();
        addGrass.put((new TerrainBlockData(Material.TALL_GRASS)), 1);
        addGrass.put((new TerrainBlockData(Material.STRUCTURE_VOID)), 3);
        AddGrass newAddGrass = new AddGrass();
        newAddGrass.setModeData(addGrass);
        floor.addMode(newAddGrass);
        StoneGen newStoneGen = new StoneGen();
        newStoneGen.setModeData(d1.getName());
        floor.addMode(newStoneGen);
        AddHouse addHouse = new AddHouse();
        addHouse.setModeData(10);
        floor.addMode(addHouse);
        floor.addGenerator(d2.getName());
        floor.addGenerator(roof.getName());
    }

}
