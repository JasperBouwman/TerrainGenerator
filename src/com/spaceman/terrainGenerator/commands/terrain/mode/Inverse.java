package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeInverse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.infoColor;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;

public class Inverse extends SubCommand {

    public Inverse() {
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setCommandName("boolean", ArgumentType.OPTIONAL);
        emptyCommand2.setCommandDescription(TextComponent.textComponent("This command is used to change the given TerrainMode to inverse or not", infoColor));
        
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> {
            //terrain mode inverse <name> <mode> <boolean>
            TerrainGenData genData = getGen(args[2]);
            if (genData.getMode(args[3]) instanceof TerrainModeInverse) {
                return Arrays.asList("true", "false");
            }
            return Collections.emptyList();
        });
        emptyCommand1.setCommandName("TerrainMode name", ArgumentType.REQUIRED);
        emptyCommand1.addAction(emptyCommand2);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to check if a TerrainMode is inverted", infoColor));

        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> getGen(args[2]).getModes().stream()
                .filter(mode -> mode instanceof TerrainModeInverse).map(TerrainMode::getModeName).collect(Collectors.toList()));
        emptyCommand.addAction(emptyCommand1);
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return ownGenerators().stream().filter(gen -> terrainGenData.get(gen).getModes().stream().anyMatch(mode -> mode instanceof TerrainModeInverse))
                .collect(Collectors.toList());
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode inverse <name> <TerrainMode name> [boolean]

        if (args.length == 4) {

            TerrainGenData data = getGen(args[2]);

            if (data != null) {

                TerrainMode mode = data.getMode(args[3]);
                if (mode != null) {
                    if (mode instanceof TerrainModeInverse) {
                        if (((TerrainModeInverse) mode).isInverse()) {
                            player.sendMessage(ColorFormatter.infoColor + "TerrainMode inverse value is set to " + ChatColor.GREEN + "true");
                        } else {
                            player.sendMessage(ColorFormatter.infoColor + "TerrainMode inverse value is set to " + ChatColor.RED + "false");
                        }
                    } else {
                        player.sendMessage(formatError("TerrainMode %s is not able to inverse", mode.getModeName()));
                    }

                } else {
                    player.sendMessage(formatError("Could not find TerrainMode %s", args[3]));
                }

            } else {
                player.sendMessage(formatError("TerrainGenerator %s was not found", args[2]));
            }


        } else if (args.length == 5) {
            TerrainGenData data = getGen(args[2]);

            if (data != null) {
                if (data.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainMode mode = data.getMode(args[3]);
                if (mode != null) {
                    if (mode instanceof TerrainModeInverse) {
                        boolean b = Main.parseBool(args[4]);
                        ((TerrainModeInverse) mode).setInverse(b);
                        if (b) {
                            player.sendMessage(ColorFormatter.infoColor + "TerrainMode inverse value is now set to " + ChatColor.GREEN + "true");
                        } else {
                            player.sendMessage(ColorFormatter.infoColor + "TerrainMode inverse value is now set to " + ChatColor.RED + "false");
                        }
                    } else {
                        player.sendMessage(formatError("TerrainMode %s is not able to inverse", mode.getModeName()));
                    }

                } else {
                    player.sendMessage(formatError("Could not find TerrainMode %s", args[3]));
                }

            } else {
                player.sendMessage(formatError("TerrainGenerator %s was not found", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain mode inverse <name> <TerrainMode name> [boolean]"));
        }

    }
}
