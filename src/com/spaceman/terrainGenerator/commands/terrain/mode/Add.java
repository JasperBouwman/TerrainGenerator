package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public class Add extends CmdHandler {
    @Override
    public String alias() {
        return "a";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode add <name> <TerrainMode name> [data]

        if (args.length >= 4) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {

                TerrainMode mode = TerrainMode.getNewMode(args[3], (args.length >= 5 ? null : new LinkedList<>(Arrays.asList(args).subList(4, args.length))), player);

                if (mode != null) {

                    genData.addMode(mode);
                    player.sendMessage(ChatColor.GREEN + "TerrainMode successfully added to TerrainGenerator " + ChatColor.DARK_GREEN + args[2]);
                } else {
                    player.sendMessage(ChatColor.RED + "There was no TerrainMode found with the name " + ChatColor.DARK_RED + args[3]);
                }
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[2] + ChatColor.RED + " does not exist");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain mode add <name> <TerrainMode name> [data]");
        }

    }
}
