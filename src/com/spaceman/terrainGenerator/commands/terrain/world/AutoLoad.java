package com.spaceman.terrainGenerator.commands.terrain.world;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.worldPrefix;

public class AutoLoad extends SubCommand {
    
    public AutoLoad() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("autoLoad", ArgumentType.OPTIONAL);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to set the AutoLoad value of the given TerrainWorld", infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> Arrays.asList("true", "false"));
        emptyCommand.setCommandName("world name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to check if AutoLoad is on or not in the given TerrainWorld. " +
                "When AutoLoad is on no new chunks are generated by it self, to load/generate a chunk use ", infoColor),
                TextComponent.textComponent("/terrain world regenerate", varInfoColor));
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return Bukkit.getWorlds().stream().filter(world -> world.getName().startsWith(worldPrefix)).map(world -> world.getName().replace(worldPrefix, "")).collect(Collectors.toList());
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain world autoLoad <world name> [autoLoad]
        
        if (args.length == 3) {
            World world = Bukkit.getWorld(worldPrefix + args[2]);
            if (world != null) {
                if (world.getGenerator() instanceof WorldGenerator) {
                    player.sendMessage(formatInfo("AutoLoad for TerrainWorld %s is set to %s", args[2], String.valueOf(((WorldGenerator) world.getGenerator()).isAutoLoad())));
                } else {
                    player.sendMessage(formatError("World %s is not a TerrainWorld", args[2]));
                }
            } else {
                player.sendMessage(formatError("World %s does not exist", args[2]));
            }
        } else if (args.length == 4) {
            World world = Bukkit.getWorld(worldPrefix + args[2]);
            if (world != null) {
                if (world.getGenerator() instanceof WorldGenerator) {
                    boolean autoLoad = Main.parseBool(args[3]);
                    ((WorldGenerator) world.getGenerator()).setAutoLoad(autoLoad);
                    player.sendMessage(formatSuccess("AutoLoad in TerrainWorld %s is set to %s", args[2], String.valueOf(autoLoad)));
                } else {
                    player.sendMessage(formatError("World %s is not a TerrainWorld", args[2]));
                }
            } else {
                player.sendMessage(formatError("World %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain world autoLoad <world name> [autoLoad]"));
        }
    }
}
