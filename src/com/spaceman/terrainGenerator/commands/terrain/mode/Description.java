package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeInverse;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeWaterLoggable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.getModes;

public class Description extends SubCommand {
    
    public Description() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("TerrainMode name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(textComponent("This command is used to get information and default values of the given TerrainMode", ColorFormatter.infoColor));
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return getModes();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode description <TerrainMode name>

        if (args.length == 3) {

            TerrainMode mode = TerrainMode.getNewMode(args[2]);
            if (mode != null) {
                Message message = new Message();

                message.addText(textComponent("Description of ", ColorFormatter.infoColor));
                message.addText(textComponent(mode.getModeName(), ColorFormatter.varInfoColor));
                message.addText(textComponent(": ", ColorFormatter.infoColor));
                message.addText(textComponent(ColorFormatter.varInfoColor + mode.getModeDescription() + ColorFormatter.varInfoColor, ColorFormatter.varInfoColor));
                message.addText(textComponent("\nIs final mode: ", ColorFormatter.infoColor));
                if (mode.isFinalMode()) {
                    message.addText(textComponent("True", ChatColor.GREEN));
                } else {
                    message.addText(textComponent("False", ChatColor.RED));
                }
                message.addText(textComponent("\nIs inverse able mode: ", ColorFormatter.infoColor));
                if (mode instanceof TerrainModeInverse) {
                    message.addText(textComponent("True", ChatColor.GREEN));
                } else {
                    message.addText(textComponent("False", ChatColor.RED));
                }
                message.addText(textComponent("\nIs WaterLoggable mode: ", ColorFormatter.infoColor));
                if (mode instanceof TerrainModeWaterLoggable) {
                    message.addText(textComponent("True", ChatColor.GREEN));
                } else {
                    message.addText(textComponent("False", ChatColor.RED));
                }
                message.addText(textComponent("\nModeType: ", ColorFormatter.infoColor));
                message.addText(textComponent(mode.modeType(), ColorFormatter.varInfoColor));

                message.addText(textComponent("\nDefault value: ", ColorFormatter.infoColor));
                message.addMessage(mode.dataAsStringWithHover());
                
                message.sendMessage(player);
            } else {
                player.sendMessage(formatError("TerrainMode %s was not found", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain mode description <TerrainMode name>"));
        }
    }
}
