package com.spaceman.terrainGenerator.commands.terrain.world;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.customWorlds;

public class List extends SubCommand {
    
    @Override
    public Message getCommandDescription() {
        return new Message(textComponent("This command is used to list all active TerrainWorlds", infoColor));
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain world list
        if (args.length == 2) {
            Message message = new Message();
            Files worldData = GettingFiles.getFiles("worldData");

            message.addText(textComponent("Loaded TerrainWorlds: ", infoColor));
            message.addText(textComponent());
            if (worldData.getConfig().contains("customWorlds")) {
                boolean b = true;
                for (String worldName : customWorlds.keySet()) {
                    if (b) {
                        HoverEvent hEvent = hoverEvent("");
                        hEvent.addText(textComponent("TerrainGenerator: ", infoColor));
                        hEvent.addText(textComponent(String.valueOf(customWorlds.get(worldName).getTerrainGeneratorName()), varInfoColor));
                        hEvent.addText(textComponent("\nAutoLoad: ", infoColor));
                        hEvent.addText(textComponent(String.valueOf(customWorlds.get(worldName).isAutoLoad()), varInfoColor));
                        message.addText(textComponent(worldName, varInfoColor, hEvent));
                    } else {
                        HoverEvent hEvent = hoverEvent("");
                        hEvent.addText(textComponent("TerrainGenerator: ", infoColor));
                        hEvent.addText(textComponent(String.valueOf(customWorlds.get(worldName).getTerrainGeneratorName()), varInfoColor));
                        hEvent.addText(textComponent("\nAutoLoad: ", infoColor));
                        hEvent.addText(textComponent(String.valueOf(customWorlds.get(worldName).isAutoLoad()), varInfoColor));
                        message.addText(textComponent(worldName, ColorFormatter.varInfo2Color,hEvent));
                    }
                    b = !b;
                    message.addText(textComponent(", ", infoColor));
                }
                message.removeLast();
            }

            message.sendMessage(player);
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain world list"));
        }
    }
}
