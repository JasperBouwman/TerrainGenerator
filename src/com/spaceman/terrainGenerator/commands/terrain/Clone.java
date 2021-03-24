package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.*;

public class Clone extends SubCommand {
    
    public Clone() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("new name", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(new Message(TextComponent.textComponent("This command is used to clone a TerrainGenerator", ColorFormatter.infoColor)));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("old name", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain clone <old name> <new name>

        if (args.length == 3) {
            TerrainGenData terrainGenData = getGen(args[1]);
            if (terrainGenData != null) {
                if (!containsGen(args[2])) {
                    Files file;
                    if (terrainGenData.getName().contains("/")) {
                        file = new Files("/etf", terrainGenData.getName().split("/")[0]);
                    } else {
                        file = getFiles("terrainData");
                    }

                    saveGenerator(file, terrainGenData);

                    ConfigurationSection configurationSection = file.getConfig().getConfigurationSection("terrainGenData." + terrainGenData.getName());
    
                    //noinspection ConstantConditions, configuration section should exist because its set in saveGenerator(file, terrainGenData)
                    configurationSection.set("data.name", args[2]);

                    initGenerator("", configurationSection);
                    player.sendMessage(formatSuccess("Created clone of %s", terrainGenData.getName()));
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s already exist", args[2]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[1]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain clone <name> <new name>"));
        }
    }
}
