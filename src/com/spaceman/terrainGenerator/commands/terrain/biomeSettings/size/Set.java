package com.spaceman.terrainGenerator.commands.terrain.biomeSettings.size;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Set extends SubCommand {
    
    public Set() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("size", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to set the biome size of the last given TerrainGenerator in the biome settings" +
                " of the first given TerrainGenerator", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain biomeSettings size <name> set <name> <size>
        
        if (args.length == 6) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                if (genData.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainGenData setData = getGen(args[4]);
                if (setData != null) {
                    try {
                        int size = Integer.parseInt(args[5]);
                        genData.getBiomeSettings().setBiomeSize(setData.getName(), size);
                        player.sendMessage(formatSuccess("Successfully set the size of TerrainGenerator %s to %s", setData.getName(), String.valueOf(size)));
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(formatError("given number is not a valid number"));
                    }
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[4]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings size <name> set <name> <size>"));
        }
    }
}
