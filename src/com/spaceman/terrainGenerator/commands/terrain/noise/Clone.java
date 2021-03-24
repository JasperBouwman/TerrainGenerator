package com.spaceman.terrainGenerator.commands.terrain.noise;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Clone extends SubCommand {

    public Clone() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("name to", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(textComponent("This command is used to clone a NoiseGenerator to another TerrainGenerator", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            List<String> list = ownGenerators();
            list.remove(args[2]);
            return list;
        });
        emptyCommand.setCommandName("name from", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain noise clone <name from> <name to>

        if (args.length == 4) {
            TerrainGenData dataFrom = getGen(args[2]);
            if (dataFrom != null) {
                TerrainNoise terrainNoise = dataFrom.getTerrainNoise();
                TerrainGenData genDataTo = getGen(args[3]);
                if (genDataTo != null) {
                    if (genDataTo.getName().contains("/")) {
                        player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                        return;
                    }

                    Files terrainData;
                    String path;

                    if (!dataFrom.getName().contains("/")) {
                        try {
                            terrainData = getFiles("terrainData");
                            terrainNoise.saveNoise(terrainData.getConfig().createSection("terrainGenData." + dataFrom.getName() + ".data.noise." + terrainNoise.getName()));
                            path = "terrainGenData." + dataFrom.getName() + ".data.noise.";
                        } catch (Exception e) {
                            player.sendMessage(formatError("Could not save TerrainNoise"));
                            return;
                        }
                    } else {
                        terrainData = new Files("/etf", args[2].split("/")[0]);
                        path = "terrainGenData." + dataFrom.getName().split("/")[1] + ".data.noise.";
                    }

                    TerrainNoise newNoise = TerrainNoise.getNewNoise(terrainNoise.getName());

                    if (newNoise != null) {
                        genDataTo.setTerrainNoise(newNoise.loadNoise(terrainData.getConfig().getConfigurationSection(path + terrainNoise.getName())));
                        Message message = new Message();
                        message.addText(textComponent("TerrainNoise successfully cloned to TerrainGenerator ", ColorFormatter.successColor));
                        message.addText(textComponent(genDataTo.getName(), ColorFormatter.varSuccessColor, genDataTo.toHoverEvent()));
                        message.sendMessage(player);
                    } else {
                        player.sendMessage(formatError("There was no TerrainNoise found with the name %s", args[3]));
                    }


                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain noise clone <name from> <name to>"));
        }
    }
}
