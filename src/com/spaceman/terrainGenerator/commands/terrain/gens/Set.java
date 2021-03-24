package com.spaceman.terrainGenerator.commands.terrain.gens;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Set extends SubCommand {

    public Set() {
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setCommandName("number", ArgumentType.REQUIRED);
        emptyCommand2.setCommandDescription(textComponent("This command is used to set a TerrainGenerator on a given place in a TerrainGenerator", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> {
            LinkedList<String> list = new LinkedList<>(getGen(args[2]).getGenerators());
            list.remove(args[3]);
            ArrayList<String> returnList = new ArrayList<>();
            for (int i = 1; i <= list.size() + 1; i++) {
                returnList.add(String.valueOf(i));
            }
            return returnList;
        });
        emptyCommand1.addAction(emptyCommand2);
        emptyCommand1.setCommandName("name", ArgumentType.REQUIRED);

        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> availableGenerators());
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
        //terrain gens set <name> <name> <number>

        if (args.length == 5) {
            TerrainGenData data = getGen(args[2]);

            if (data != null) {
                if (data.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }

                TerrainGenData addData = getGen(args[3]);

                if (addData != null) {

                    int number;
                    try {
                        number = Integer.parseInt(args[4]);
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(formatError("%s is not a valid number", args[4]));
                        return;
                    }
                    if (number <= 0) {
                        player.sendMessage(formatError("Your given place must be higher than 0"));
                        return;
                    }

                    LinkedList<String> list = new LinkedList<>(data.getGenerators());
                    list.remove(addData.getName());

                    LinkedList<String> newList = new LinkedList<>();
                    if (number > list.size() + 1) {
                        player.sendMessage(formatError("Your given place must be lower than the amount of TerrainGenerators (%s)", String.valueOf((list.size() + 1))));
                        return;
                    }
                    int i = 1;
                    boolean b = true;
                    for (String gen : list) {
                        if (i == number) {
                            newList.add(addData.getName());
                            b = false;
                        }
                        newList.add(gen);
                        i++;
                    }
                    if (b) {
                        newList.add(addData.getName());
                    }

                    data.setGenerators(newList);

                    Message message = new Message();
                    message.addText(textComponent("Successfully set TerrainGenerator ", ColorFormatter.successColor));
                    message.addText(textComponent(data.getName(), ColorFormatter.varSuccessColor, data.toHoverEvent()));
                    message.addText(textComponent(" to place ", ColorFormatter.successColor));
                    message.addText(textComponent(String.valueOf(number), ColorFormatter.varSuccessColor));
                    message.sendMessage(player);

                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: ", "/terrain gens set <name> <name> <number>"));
        }
    }
}
