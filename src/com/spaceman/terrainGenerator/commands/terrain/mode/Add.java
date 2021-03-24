package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.infoColor;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.getModes;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.getNewMode;

public class Add extends SubCommand {

    public Add() {
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setTabRunnable((args, player) -> {
            //terrain mode add <name> <mode> ...
            if (getGen(args[2]).getMode(args[3]) == null) {
                TerrainMode mode = getNewMode(args[3]);
                return mode.tabListCreate(args, player);
            } else {
                return new ArrayList<>();
            }
        });
        emptyCommand2.setCommandName("data...", ArgumentType.OPTIONAL);
        emptyCommand2.setLooped(true);
        emptyCommand2.setCommandDescription(textComponent("When given data, the data will be set to that TerrainMode. " +
                "If no data is given the TerrainMode is set to its default", ColorFormatter.infoColor));
    
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable(emptyCommand2.getTabRunnable());
        emptyCommand1.setCommandName("TerrainMode name", ArgumentType.REQUIRED);
        emptyCommand1.addAction(emptyCommand2);
        emptyCommand1.setCommandDescription(textComponent("This command is used to add a TerrainMode to the given TerrainGenerator", infoColor));

        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            //terrain mode add <name> ...
            if (args.length == 4) {
                ArrayList<String> list = new ArrayList<>(getModes());
                TerrainGenData genData = getGen(args[2]);
                genData.getModes().stream().map(TerrainMode::getModeName).forEach(list::remove);
                return list;
            }
            return new ArrayList<>();
        });
        emptyCommand.addAction(emptyCommand1);
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return ownGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode add <name> <TerrainMode name> [data...]

        if (args.length >= 4) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                if (genData.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                if (genData.getMode(args[3]) == null) {

                    TerrainMode mode = TerrainMode.getNewMode(args[3], (args.length < 5 ? null : new LinkedList<>(Arrays.asList(args).subList(4, args.length))), player);

                    if (mode != null) {
                        genData.addMode(mode);

                        Message message = new Message();
                        message.addText(textComponent("TerrainMode successfully added to TerrainGenerator ", ColorFormatter.successColor));
                        message.addText(textComponent(args[2], ColorFormatter.varSuccessColor, genData.toHoverEvent()));
                        message.sendMessage(player);
                    } else {
                        player.sendMessage(formatError("There was no TerrainMode found with the name %s", args[3]));
                    }
                } else {
                    player.sendMessage(formatError("TerrainMode %s is already in that TerrainGenerator", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }

        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain mode add <name> <TerrainMode name> [data...]"));
        }

    }
}
