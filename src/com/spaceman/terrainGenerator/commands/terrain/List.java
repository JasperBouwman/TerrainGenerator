package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGens;

public class List extends SubCommand {
    
    public List() {
        EmptyCommand emptyCommandAll = new EmptyCommand();
        emptyCommandAll.setCommandName("all", ArgumentType.FIXED);
        emptyCommandAll.setCommandDescription(new Message(
                textComponent("This command shows all TerrainGenerators, just like ", infoColor),
                textComponent("/terrain list", varInfoColor)));
        
        EmptyCommand emptyCommandOwn = new EmptyCommand();
        emptyCommandOwn.setCommandName("own", ArgumentType.FIXED);
        emptyCommandOwn.setCommandDescription(textComponent("This command only shows all the internal TerrainGenerators", infoColor));
    
        EmptyCommand empty = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return "";
            }
        };
        empty.setCommandName("", ArgumentType.FIXED);
        empty.setCommandDescription(textComponent("This command is used to list all the available TerrainGenerators, external and internal", infoColor));
        addAction(empty);
        addAction(emptyCommandAll);
        addAction(emptyCommandOwn);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return Arrays.asList("own", "all");
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain list [own|all]

        if (args.length == 1 || args.length == 2 && args[1].equalsIgnoreCase("all")) {
            Message message = new Message();

            message.addText(textComponent("Available TerrainGenerators: ", ColorFormatter.infoColor));
            message.addText("");
            boolean color = true;
            for (String s : getGens()) {
                if (color) {
                    message.addText(textComponent(s, ColorFormatter.varInfoColor, TerrainGenData.toHoverEvent(s)));
                } else {
                    message.addText(textComponent(s, ColorFormatter.varInfo2Color, TerrainGenData.toHoverEvent(s)));
                }
                color = !color;
                message.addText(textComponent(", ", ColorFormatter.infoColor));
            }
            message.removeLast();

            message.sendMessage(player);
        } else if (args.length == 2 && args[1].equalsIgnoreCase("own")) {
            Message message = new Message();

            message.addText(textComponent("Own TerrainGenerators: ", ColorFormatter.infoColor));
            message.addText("");
            boolean color = true;
            for (String s : getGens()) {
                if (s.contains("/")) {
                    continue;
                }
                if (color) {
                    message.addText(textComponent(s, ColorFormatter.varInfoColor, TerrainGenData.toHoverEvent(s)));
                } else {
                    message.addText(textComponent(s, ColorFormatter.varInfo2Color, TerrainGenData.toHoverEvent(s)));
                }
                color = !color;
                message.addText(textComponent(", ", ColorFormatter.infoColor));
            }
            message.removeLast();

            message.sendMessage(player);
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain list [own|all]"));
        }

    }
}
