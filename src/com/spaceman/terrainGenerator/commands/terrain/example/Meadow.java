package com.spaceman.terrainGenerator.commands.terrain.example;

import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.terrain.TerrainGenData.terrainGenData;

public class Meadow extends ExampleHandler {
    @Override
    public String name() {
        return "Meadow";
    }

    @Override
    public void generate(int x, int z, Player player) {
        new Thread(() -> generateGens(x, z, player)).start();
    }

    private void generateGens(int x, int z, Player player) {

        new Thread(() -> {
            player.sendMessage(ChatColor.GREEN + "Generating...");
            long time = System.currentTimeMillis();
            TerrainGenerator.generate("iExamplesTG-Meadow", x, z, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Generated, time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - time) / 1000 + ChatColor.GREEN + " seconds");
            long newTime = System.currentTimeMillis();
            player.sendMessage(ChatColor.GREEN + "Loading terrain...");
            GettingFiles.p.getServer().getScheduler().scheduleSyncDelayedTask(GettingFiles.p, () ->
                    player.sendMessage(ChatColor.GREEN + "Terrain loaded, time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - newTime) / 1000 + ChatColor.GREEN + " seconds\n" +
                            "Total time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - time) / 1000 + ChatColor.GREEN + " seconds")
            );
            TerrainGenerator.removeGen("iExamplesTG-Meadow");
        }).start();

    }

    private void createGens() {
        TerrainGenData d1 = terrainGenData("iExamplesTG-Meadow");
        d1.setHeight(70);
        d1.setStart(45);
        d1.setTerrainBlockData(new TerrainBlockData(Material.GRASS_BLOCK));
    }

    @Override
    public void sendData(Player player) {
        createGens();
        this.sendData(player, "iExamplesTG-Meadow");
        TerrainGenerator.removeGen("iExamplesTG-Meadow");
    }

}
