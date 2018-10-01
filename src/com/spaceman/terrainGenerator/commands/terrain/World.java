package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.terrain.WorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

import static com.spaceman.terrainGenerator.terrain.WorldGenerator.customWorlds;
import static com.spaceman.terrainGenerator.terrain.WorldGenerator.tpAway;

public class World extends CmdHandler {
    @Override
    public String alias() {
        return "";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain world create <world name> <name>
        //terrain world delete <world name>

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain world <create:delete> <world name>");
            return;
        }

        if (args[1].equalsIgnoreCase("create")) {
            if (args.length < 4) {

            }
            try {
                File folder = new File(Bukkit.getWorldContainer(), args[2]);
                if (folder.exists()) {
                    player.sendMessage("The world " + args[2] + " could not be created... file already exist");
                    return;
                }

                try {
                    if (Bukkit.getWorld(args[2]) != null) {
                        player.sendMessage("World " + args[2] + " already exist");
                        return;
                    }
                    player.teleport(new Location(WorldGenerator.generateWorld(args[2], args[3]), 0, 100, 0));
                } catch (NullPointerException npe) {
                    player.sendMessage("some TerrainGenerators does not exist anymore, but are used in other TerrainGenerators");
                }

            } catch (NullPointerException npe) {
                player.sendMessage("The world " + args[2] + " could not be created...");
            }
        } else if (args[1].equalsIgnoreCase("delete")) {
            if (customWorlds.containsKey(args[2])) {
                customWorlds.remove(args[2]);
                org.bukkit.World world = Bukkit.getWorld(args[2]);
                if (world == null) {
                    player.sendMessage("World is not loaded, perhaps due to a error while loading the world");
                    return;
                }
                for (Player pl : world.getPlayers()) {
                    pl.sendMessage("The World you are in is going to be deleted. You will be teleported away from this world");
                    tpAway(pl);
                }
                Bukkit.unloadWorld(world, false);

                File path = world.getWorldFolder();
                if (!path.delete()) {
                    player.sendMessage("World is unloaded, but world could not be deleted. You have to manuel delete the world folder.");
                }

            } else {
                player.sendMessage("there is no record of a TerrainWorld called " + args[2]);
            }
        }
    }

}
