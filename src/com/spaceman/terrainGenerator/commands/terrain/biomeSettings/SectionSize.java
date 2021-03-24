package com.spaceman.terrainGenerator.commands.terrain.biomeSettings;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class SectionSize extends SubCommand {
    
    public SectionSize() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("size", ArgumentType.OPTIONAL);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to set the section size of the given TerrainGenerator", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to get the section size of the given TerrainGenerator", ColorFormatter.infoColor));
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return ownGenerators();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain biomeSettings sectionSize <name> [size]
        
        if (args.length == 4) {
            try {
                int size = Integer.parseInt(args[3]);
                if (size <= 0) {
                    player.sendMessage(formatError("Given section size can't be lower/equals than 0"));
                    return;
                }
                TerrainGenData genData = getGen(args[2]);
                if (genData != null) {
                    if (genData.getName().contains("/")) {
                        player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                        return;
                    }
                    genData.getBiomeSettings().setSectionSize(size);
                    player.sendMessage(formatSuccess("Successfully set the section size of TerrainGenerator %s to %s", genData.getName(), String.valueOf(size)));
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
                }
            } catch (NumberFormatException nfe) {
                player.sendMessage(formatError("Given number is not a valid number"));
            }
        } else if (args.length == 3) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                player.sendMessage(formatSuccess("Section size of TerrainGenerator %s is %s", genData.getName(), String.valueOf(genData.getBiomeSettings().getSectionSize())));
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings sectionSize <name> [size]"));
        }
    }
}
