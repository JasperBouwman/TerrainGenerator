package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.TabCompletes;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeInverse;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeWaterLoggable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.infoColor;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.getModes;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.getNewMode;

public class List extends SubCommand {
    
    public List() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.OPTIONAL);
        emptyCommand.setCommandDescription(textComponent("This command is used to list all TerrainModes in the given TerrainGenerator", infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Message getCommandDescription() {
        return new Message(textComponent("This command is used to list all available TerrainModes", infoColor));
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return TabCompletes.availableGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode list [name]

        if (args.length == 3) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                Message message = new Message();
                message.addText(textComponent("TerrainModes in TerrainGenerator ", ColorFormatter.infoColor));
                message.addText(textComponent(genData.getName(), ColorFormatter.varInfoColor, genData.toHoverEvent()));
                message.addText(textComponent(": ", ColorFormatter.infoColor));
                message.addText(textComponent(""));

                boolean b = true;

                for (TerrainMode mode : genData.getModes()) {
                    HoverEvent hEvent = terrainModeToHEvent(mode);

                    if (b) {
                        message.addText(textComponent(mode.getModeName(), ColorFormatter.varInfoColor, hEvent));
                    } else {
                        message.addText(textComponent(mode.getModeName(), ColorFormatter.varInfo2Color, hEvent));
                    }
                    b = !b;
                    message.addText(textComponent(", ", ColorFormatter.infoColor));
                }
                message.removeLast();

                message.sendMessage(player);
            } else {
                player.sendMessage(formatError("TerrainGenerator %s was not found", args[2]));
            }
        } else if (args.length == 2) {
            Message message = new Message();
            message.addText(textComponent("Available TerrainModes are: ", ColorFormatter.infoColor));
            message.addText(textComponent(""));
            boolean b = true;
            for (String mode : getModes()) {

                TerrainMode terrainMode = getNewMode(mode);
                if (terrainMode == null) {
                    continue;
                }

                HoverEvent hEvent = terrainModeToHEvent(terrainMode);

                if (b) {
                    message.addText(textComponent(terrainMode.getModeName(), ColorFormatter.varInfoColor, hEvent));
                } else {
                    message.addText(textComponent(terrainMode.getModeName(), ColorFormatter.varInfo2Color, hEvent));
                }
                b = !b;
                message.addText(textComponent(", ", ColorFormatter.infoColor));
            }

            message.removeLast();
            message.sendMessage(player);
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain mode list [name]"));
        }
    }

    private HoverEvent terrainModeToHEvent(TerrainMode mode) {
        HoverEvent hEvent = hoverEvent(textComponent("Description: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(ColorFormatter.varInfoColor + mode.getModeDescription() + ColorFormatter.varInfoColor, ColorFormatter.varInfoColor));
        hEvent.addText(textComponent("\nIs final mode: ", ColorFormatter.infoColor));
        if (mode.isFinalMode()) {
            hEvent.addText(textComponent("True", ChatColor.GREEN));
        } else {
            hEvent.addText(textComponent("False", ChatColor.RED));
        }
        hEvent.addText(textComponent("\nIs inverse able mode: ", ColorFormatter.infoColor));
        if (mode instanceof TerrainModeInverse) {
            hEvent.addText(textComponent("True", ChatColor.GREEN));
        } else {
            hEvent.addText(textComponent("False", ChatColor.RED));
        }
        hEvent.addText(textComponent("\nIs WaterLoggable mode: ", ColorFormatter.infoColor));
        if (mode instanceof TerrainModeWaterLoggable) {
            hEvent.addText(textComponent("True", ChatColor.GREEN));
        } else {
            hEvent.addText(textComponent("False", ChatColor.RED));
        }

        hEvent.addText(textComponent("\nData: ", ColorFormatter.infoColor));
        hEvent.addMessage(mode.dataAsString());

        return hEvent;
    }
}
