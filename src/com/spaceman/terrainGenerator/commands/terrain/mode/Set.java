package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.getNewMode;

public class Set extends SubCommand {

    public Set() {
        EmptyCommand emptyCommand3 = new EmptyCommand();
        emptyCommand3.setTabRunnable((args, player) -> {
            //terrain mode set <name> <TerrainMode name> <number> [data...]
            if (getGen(args[2]).getMode(args[3]) == null) {
                TerrainMode mode = getNewMode(args[3]);
                return mode.tabListCreate(args, player);
            } else {
                return new ArrayList<>();
            }
        });
        emptyCommand3.setLooped(true);
        emptyCommand3.setCommandName("data...", ArgumentType.OPTIONAL);
        emptyCommand3.setCommandDescription(TextComponent.textComponent("If the TerrainMode is already in the TerrainGenerator you can give it data just like", infoColor),
                TextComponent.textComponent("/terrain mode add", varInfoColor));
    
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setTabRunnable(emptyCommand3.getTabRunnable());
        emptyCommand2.setCommandName("number", ArgumentType.REQUIRED);
        emptyCommand2.addAction(emptyCommand3);
        emptyCommand2.setCommandDescription(TextComponent.textComponent("This command is used to set a TerrainMode to a given place in a TerrainGenerator. " +
                "If the TerrainMode is not in the TerrainGenerator there will be created a new one, if it is in the TerrainMode it will use that one", infoColor));

        //terrain mode set <name> <TerrainMode name> <number>
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> {
            TerrainGenData genData = getGen(args[2]);
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i <= genData.getModes().size(); i++) {
                list.add(String.valueOf(i + 1));
            }
            if (genData.getMode(args[3]) != null) {
                list.remove(list.get(list.size() - 1));
            }
            return list;
        });
        emptyCommand1.addAction(emptyCommand2);
        emptyCommand1.setCommandName("TerrainMode name", ArgumentType.REQUIRED);

        //terrain mode set <name> <TerrainMode name>
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> new ArrayList<>(TerrainMode.getModes()));
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
        //terrain mode set <name> <TerrainMode name> <number> [data... (only for new TerrainModes)]

        if (args.length >= 5) {
            TerrainGenData data = getGen(args[2]);

            if (data != null) {
                if (data.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainMode oldMode = null;
                LinkedList<TerrainMode> list = new LinkedList<>(data.getModes());
                for (TerrainMode terrainMode : list) {
                    if (terrainMode.getModeName().equals(args[3])) {
                        list.remove(terrainMode);
                        oldMode = terrainMode;
                        if (args.length > 5) {
                            player.sendMessage(formatInfo("The TerrainMode was already in the given TerrainGenerator, the data you gave is therefore not added"));
                        }
                        break;
                    }
                }
                TerrainMode mode = oldMode;
                if (oldMode == null) {
                    mode = TerrainMode.getNewMode(args[3], (args.length == 5 ? null : new LinkedList<>(Arrays.asList(args).subList(5, args.length))), player);
                }

                if (mode != null) {

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

                    LinkedList<TerrainMode> newList = new LinkedList<>();
                    if (number > list.size() + 1) {
                        player.sendMessage(formatError("Your given place must be lower than the amount of TerrainModes (%s)", String.valueOf((list.size() + 1))));
                        return;
                    }
                    int i = 1;
                    boolean b = true;
                    for (TerrainMode tmpMode : list) {
                        if (i == number) {
                            newList.add(mode);
                            b = false;
                        }
                        newList.add(tmpMode);
                        i++;
                    }
                    if (b) {
                        newList.add(mode);
                    }

                    data.setModes(newList);

                    player.sendMessage(formatSuccess("Successfully set %s TerrainMode to place %s", (oldMode != null ? "existing" : "new"), String.valueOf(number)));

                } else {
                    player.sendMessage(formatError("TerrainMode %s does not exist", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s","/terrain mode <name> <TerrainMode name> <number> [data]"));
        }

    }
}
