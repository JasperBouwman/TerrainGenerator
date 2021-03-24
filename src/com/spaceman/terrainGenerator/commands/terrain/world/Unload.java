package com.spaceman.terrainGenerator.commands.terrain.world;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.terrain.Generate;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.*;

public class Unload extends SubCommand {
    
    public Unload() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("delete", ArgumentType.OPTIONAL);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("When the last argument is 'true' the world will be unloaded and deleted," +
                " if it's not given/'false' it's only going to be unloaded", infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> Arrays.asList("true", "false"));
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to unload the given TerrainWorld", infoColor));
        emptyCommand.setCommandName("world", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return customWorlds.keySet();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain world unload <world> [delete]
        boolean delete = false;
        if (args.length == 4) {
            delete = Main.parseBool(args[3]);
        }
        if (args.length >= 3 && args.length < 5) {
            if (customWorlds.containsKey(args[2])) {
                customWorlds.remove(args[2]);
                org.bukkit.World world = Bukkit.getWorld(worldPrefix + args[2]);
                if (world == null) {
                    player.sendMessage(formatError("World is not loaded, perhaps due to an error while loading the world"));
                    return;
                }
                for (Player pl : world.getPlayers()) {
                    pl.sendMessage(formatInfo("The World you are in is going to be %s. You will be teleported away from this world", (delete ? "deleted" : "unloaded")));
                    tpAway(pl);
                }
                boolean unload = Bukkit.unloadWorld(world, true);
                if (!unload) {
                    player.sendMessage(formatError("The world could not be unloaded"));
                    return;
                }
                
                if (!delete) {
                    player.sendMessage(formatSuccess("World unloaded"));
                } else {
                    UUID playerUUID = player.getUniqueId();
                    File path = new File(world.getWorldFolder().getAbsolutePath().replaceAll("\\.\\\\", ""));
                    try {
                        FileUtils.deleteDirectory(path);
                        Generate.sendSafeMessage(playerUUID, formatSuccess("World unloaded and deleted"));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Generate.sendSafeMessage(playerUUID, formatInfo("World is unloaded, but world could not be deleted. You have to manual delete the world folder."));
                    }
                }
            } else {
                player.sendMessage(formatError("There is TerrainWorld called %s", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain world unload <world> [delete]"));
        }
    }
}
