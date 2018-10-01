package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public class Copy extends CmdHandler {
    @Override
    public String alias() {
        return "c";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode copy <name> <name> <TerrainMode name>

        if (args.length == 5) {

            TerrainGenData dataOriginal = getGen(args[2]);
            TerrainGenData dataClone = getGen(args[3]);

            if (dataOriginal != null) {
                if (dataClone != null) {
                    TerrainMode modeClone = dataClone.getMode(args[4]);

                    if (modeClone != null) {

                        try {
                            dataOriginal.addMode((TerrainMode) modeClone.clone());
                            player.sendMessage(ChatColor.GREEN + "Successfully copied TerrainMode to " + ChatColor.DARK_GREEN + dataOriginal.getName());
                        } catch (CloneNotSupportedException e) {
                            player.sendMessage(ChatColor.RED + "Could not copy TerrainMode");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + dataClone.getName() + ChatColor.RED + " has no TerrainMode named " + args[4]);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[3] + ChatColor.RED + " was not found");
                }
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[2] + ChatColor.RED + " was not found");
            }


        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain mode copy <name> <name> <TerrainMode name>");
        }
    }
}
