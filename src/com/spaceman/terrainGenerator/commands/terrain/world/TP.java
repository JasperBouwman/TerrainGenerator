package com.spaceman.terrainGenerator.commands.terrain.world;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.worldPrefix;

public class TP extends SubCommand {
    
    public TP() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("world name", ArgumentType.OPTIONAL);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to teleport to the teleport to the spawn location " +
                "of the given TerrainWorld", ColorFormatter.infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Message getCommandDescription() {
        return new Message(TextComponent.textComponent("This command is used to teleport to the main world of the server", ColorFormatter.infoColor));
    }
    
    @Override
    public String getName(String arg) {
        return "tp";
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return Bukkit.getWorlds().stream().filter(world -> world.getName().startsWith(worldPrefix)).map(world -> world.getName().replace(worldPrefix, "")).collect(Collectors.toList());
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain world tp [world name]
        
        if (args.length == 2) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            player.sendMessage(formatSuccess("Successfully teleported to world %s", player.getWorld().getName()));
        } else if (args.length == 3) {
            World world = Bukkit.getWorld(worldPrefix + args[2]);
            if (world != null) {
                player.teleport(world.getSpawnLocation());
                player.sendMessage(formatSuccess("Teleported to the world %s spawn location", args[2]));
            } else {
                player.sendMessage(formatError("TerrainWorld %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain world tp [world name]"));
        }
    }
}
