package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.terrain.TerrainGenerator;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public class Generate extends CmdHandler {

    @Override
    public String alias() {
        return "g";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain generate <name> <x> <z>
        if (args.length == 4) {

            int x = 0, z = 0;
            boolean b = false;

            try {
                x = Integer.parseInt(args[2]);
            } catch (NumberFormatException nfe) {
                player.sendMessage(ChatColor.DARK_RED + args[2] + ChatColor.RED + " value isn't a valid number");
                b = true;
            }
            try {
                z = Integer.parseInt(args[3]);
            } catch (NumberFormatException nfe) {
                player.sendMessage(ChatColor.DARK_RED + args[3] + ChatColor.RED + " value isn't a valid number");
                b = true;
            }
            if (b) {
                return;
            }

            if (getGen(args[1]) != null) {

                int finalX = x;
                int finalZ = z;

                new Thread(() -> {
                    player.sendMessage(ChatColor.GREEN + "Generating...");
                    long time = System.currentTimeMillis();
                    try {
                        TerrainGenerator.generate(args[1], finalX, finalZ, player.getLocation());
                    } catch (Exception npe) {
                        player.sendMessage(ChatColor.RED + "An unexpected error occurred.");
                    }
                    player.sendMessage(ChatColor.GREEN + "Generated, time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - time) / 1000 + ChatColor.GREEN + " seconds");
                    long newTime = System.currentTimeMillis();
                    player.sendMessage(ChatColor.GREEN + "Loading terrain...");
                    GettingFiles.p.getServer().getScheduler().scheduleSyncDelayedTask(GettingFiles.p, () ->
                            player.sendMessage(ChatColor.GREEN + "Terrain loaded, time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - newTime) / 1000 + ChatColor.GREEN + " seconds\n" +
                                    "Total time taken: " + ChatColor.DARK_GREEN + (System.currentTimeMillis() - time) / 1000 + ChatColor.GREEN + " seconds")
                    );
                }).start();

            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " does not exist");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain generate <name> <width(x)> <height(z)>");
        }
    }
}
