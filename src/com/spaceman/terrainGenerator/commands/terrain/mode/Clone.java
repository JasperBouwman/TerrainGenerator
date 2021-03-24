package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeInverse;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.infoColor;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Clone extends SubCommand {

    public Clone() {
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setCommandName("name to", ArgumentType.REQUIRED);
        emptyCommand2.setCommandDescription(textComponent("This command is used to clone a TerrainMode form one TerrainGenerator to another TerrainGenerator", infoColor));
        
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> {
            List<String> list = ownGenerators();
            list.remove(args[2]);
            return list;
        });
        emptyCommand1.setCommandName("mode", ArgumentType.REQUIRED);
        emptyCommand1.addAction(emptyCommand2);

        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            TerrainGenData genData = getGen(args[2]);
            return genData.getModes().stream().map(TerrainMode::getModeName).collect(Collectors.toCollection(ArrayList::new));
        });
        emptyCommand.addAction(emptyCommand1);
        emptyCommand.setCommandName("name from", ArgumentType.REQUIRED);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode clone <name from> <mode> <name to>

        if (args.length == 5) {
            TerrainGenData dataFrom = getGen(args[2]);
            if (dataFrom != null) {
                TerrainMode mode = dataFrom.getMode(args[3]);
                if (mode != null) {
                    TerrainGenData dataTo = getGen(args[4]);
                    if (dataTo != null) {
                        if (dataTo.getName().contains("/")) {
                            player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                            return;
                        }
                        Files terrainData;
                        String path;
                        if (!args[2].contains("/")) {
                            try {
                                terrainData = getFiles("terrainData");
                                mode.saveMode(terrainData.getConfig().createSection("terrainGenData." + dataFrom.getName() + ".terrainMode." + mode.getModeName()));
                                if (mode instanceof TerrainModeInverse) {
                                    terrainData.getConfig().set("terrainGenData." + dataFrom.getName() + ".terrainMode." + mode.getModeName() + ".inverse", ((TerrainModeInverse) mode).isInverse());
                                }
                                path = "terrainGenData." + dataFrom.getName() + ".terrainMode.";
                            } catch (Exception e) {
                                player.sendMessage(formatError("Could not save TerrainMode"));
                                return;
                            }
                        } else {
                            terrainData = new Files("/etf", args[2].split("/")[0]);
                            path = "terrainGenData." + dataFrom.getName().split("/")[1] + ".terrainMode.";
                        }

                        TerrainMode cloneMode = TerrainMode.getNewMode(args[3]);

                        if (cloneMode != null) {
                            dataTo.addMode(cloneMode.loadMode(terrainData.getConfig().getConfigurationSection(path + mode.getModeName())));
                            Message message = new Message();
                            message.addText(textComponent("TerrainMode successfully cloned to TerrainGenerator ", ColorFormatter.successColor));
                            message.addText(textComponent(dataTo.getName(), ColorFormatter.varSuccessColor, dataTo.toHoverEvent()));
                            message.sendMessage(player);
                        } else {
                            player.sendMessage(formatError("There was no TerrainMode found with the name %s", args[3]));
                        }
                    } else {
                        player.sendMessage(formatError("TerrainGenerator %s does not exist", args[4]));
                    }
                } else {
                    Message message = new Message();
                    message.addText(textComponent("Could not find TerrainMode ", ColorFormatter.errorColor));
                    message.addText(textComponent(args[3], ColorFormatter.varErrorColor));
                    message.addText(textComponent(" in TerrainGenerator ", ColorFormatter.errorColor));
                    message.addText(textComponent(dataFrom.getName(), ColorFormatter.varErrorColor, dataFrom.toHoverEvent()));
                    message.sendMessage(player);
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }

        } else {
            player.sendMessage(formatError("Usage: ", "/terrain mode clone <name from> <mode> <name to>"));
        }

    }
}
