package com.spaceman.terrainGenerator.commands.terrain.example;

import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import com.spaceman.terrainGenerator.modes.AddGrass;
import com.spaceman.terrainGenerator.modes.AddLava;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

import static com.spaceman.terrainGenerator.terrain.TerrainGenData.terrainGenData;

public class Hell extends ExampleHandler {
    @Override
    public void generate(int x, int z, Player player) {
        new Thread(() -> generateGens(x, z, player)).start();
    }

    @Override
    public void sendData(Player player) {
        createGens();
        this.sendData(player, "iExamplesTG-Hell");
        TerrainGenerator.removeGen("iExamplesTG-Hell");
    }

    @Override
    public String name() {
        return "Hell";
    }

    private void generateGens(int x, int z, Player player) {
        new Thread(() -> {
            createGens();
            player.sendMessage(ChatColor.GREEN + "Generating...");
            long time = System.currentTimeMillis();
            TerrainGenerator.generate("iExamplesTG-Hell", x, z, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Generated, time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - time) / 1000 + ChatColor.GREEN + " seconds");
            long newTime = System.currentTimeMillis();
            player.sendMessage(ChatColor.GREEN + "Loading terrain...");
            GettingFiles.p.getServer().getScheduler().scheduleSyncDelayedTask(GettingFiles.p, () ->
                    player.sendMessage(ChatColor.GREEN + "Terrain loaded, time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - newTime) / 1000 + ChatColor.GREEN + " seconds\n" +
                            "Total time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - time) / 1000 + ChatColor.GREEN + " seconds")
            );
            TerrainGenerator.removeGen("iExamplesTG-Hell");
        }).start();
    }

    private void createGens() {
        TerrainGenData d1 = terrainGenData("iExamplesTG-Hell");
        d1.setHeight(80);
        d1.setStart(45);
        d1.setFrequency(20);
        d1.setMultitude(20);
        d1.setTerrainBlockData(new TerrainBlockData(Material.NETHERRACK));
        AddLava l1 = new AddLava();
        l1.setModeData(75);
        d1.addMode(l1);
        AddGrass g1 = new AddGrass();
        LinkedHashMap<TerrainBlockData, Integer> g1m = new LinkedHashMap<>();
        g1m.put(new TerrainBlockData(Material.FIRE), 1);
        g1m.put(new TerrainBlockData(Material.STRUCTURE_VOID), 5);
        g1.setModeData(g1m);
        d1.addMode(g1);
    }
}
