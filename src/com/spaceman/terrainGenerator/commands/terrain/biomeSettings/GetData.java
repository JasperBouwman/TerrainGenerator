package com.spaceman.terrainGenerator.commands.terrain.biomeSettings;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.Pair;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class GetData extends SubCommand {
    
    public GetData() {
        
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("name", ArgumentType.OPTIONAL);
        emptyCommand1.setCommandDescription(textComponent("This command is used to get the biome data of the given TerrainGenerator in the first given TerrainGenerator", infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(textComponent("This command is used to get the overall biome data of the given TerrainGenerator", infoColor));
        emptyCommand.addAction(emptyCommand1);
        emptyCommand.setTabRunnable((args, player) -> {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                return genData.getBiomeSettings().getBiomes();
            }
            return new ArrayList<>();
        });
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return ownGenerators();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain biomeSettings getData <name> [name]
        
        if (args.length == 3) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                
                Message message = new Message();
                
                message.addText(textComponent("Biome settings from ", infoColor));
                message.addText(textComponent(genData.getName(), varInfoColor, genData.toHoverEvent()));
                message.addText(textComponent(": ", infoColor));
                
                message.addText(textComponent("Section size: ", infoColor));
                message.addText(textComponent(String.valueOf(genData.getBiomeSettings().getSectionSize()), varInfoColor, String.valueOf(genData.getBiomeSettings().getSectionSize())));
                message.addText(textComponent(", Biome scale: ", infoColor));
                message.addText(textComponent(String.valueOf(genData.getBiomeSettings().getBiomeScale()), varInfoColor, String.valueOf(genData.getBiomeSettings().getBiomeScale())));
                message.addText(textComponent(", Biome seed: ", infoColor));
                message.addText(textComponent(String.valueOf(genData.getBiomeSettings().getBiomeSeed()), varInfoColor, String.valueOf(genData.getBiomeSettings().getBiomeSeed())));
                
                message.addText(textComponent(", biomes: ", infoColor));
                
                HashMap<String, Pair<Double, Integer>> map = genData.getBiomeSettings().getBiomeGenerators();
                boolean color = true;
                for (String name : map.keySet()) {
                    HoverEvent hEvent = new HoverEvent();
                    hEvent.addText("Weight: ", infoColor);
                    hEvent.addText(String.valueOf(map.get(name).getLeft()), varInfoColor);
                    hEvent.addText("\nSize: ", infoColor);
                    hEvent.addText(String.valueOf(map.get(name).getRight()), varInfoColor);
                    if (color) {
                        message.addText(textComponent(name, varInfoColor, hEvent));
                    } else {
                        message.addText(textComponent(name, ColorFormatter.varInfo2Color, hEvent));
                    }
                    message.addText(textComponent(", ", infoColor));
                    color = !color;
                }
                message.removeLast();
                message.sendMessage(player);
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else if (args.length == 4) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                TerrainGenData otherData = getGen(args[3]);
                if (otherData != null) {
                    Message message = new Message();
    
                    message.addText(textComponent("Biome settings for ", infoColor));
                    message.addText(textComponent(otherData.getName(), varInfoColor, otherData.toHoverEvent()));
                    message.addText(textComponent(" in ", infoColor));
                    message.addText(textComponent(genData.getName(), varInfoColor, genData.toHoverEvent()));
                    message.addText(textComponent(": ", infoColor));
    
                    message.addText(textComponent("Weight: ", infoColor));
                    message.addText(textComponent(String.valueOf(genData.getBiomeSettings().getBiomeWeight(otherData)), varInfoColor).setTextAsInsertion());
                    message.addText(textComponent(", Size: ", infoColor));
                    message.addText(textComponent(String.valueOf(genData.getBiomeSettings().getBiomeSize(otherData)), varInfoColor).setTextAsInsertion());
                    
                    message.sendMessage(player);
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings getData <name> [name]"));
        }
    }
}
