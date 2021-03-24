package com.spaceman.terrainGenerator.commands.terrain.biomeSettings.weight;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatInfo;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Own extends SubCommand {
    
    public Own() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("weight", ArgumentType.OPTIONAL);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to set the biome weight of the given TerrainGenerator in its own biome settings",
                ColorFormatter.infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Message getCommandDescription() {
        return new Message(TextComponent.textComponent("This command is used to get the biome weight of the given TerrainGenerator in its own biome settings",
                ColorFormatter.infoColor));
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain biomeSettings weight <name> own [weight]
        
        if (args.length == 5) {
            try {
                double weight = Double.parseDouble(args[4]);
        
                TerrainGenData genData = getGen(args[2]);
                if (genData != null) {
                    if (genData.getName().contains("/")) {
                        player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                        return;
                    }
                    genData.getBiomeSettings().setBiomeWeight(weight);
                    player.sendMessage(formatSuccess("Successfully set the biome weight of TerrainGenerator %s to %s", genData.getName(), String.valueOf(weight)));
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
                }
            } catch (NumberFormatException nfe) {
                player.sendMessage(formatError("Given number is not a valid number"));
            }
        } else if (args.length == 4) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                player.sendMessage(formatInfo("TerrainGenerator %s has the weight of %s", genData.getName(), String.valueOf(genData.getBiomeSettings().getBiomeWeight())));
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings weight <name> own [weight]"));
        }
    }
}
