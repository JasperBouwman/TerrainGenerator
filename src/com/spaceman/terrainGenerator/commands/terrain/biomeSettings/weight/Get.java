package com.spaceman.terrainGenerator.commands.terrain.biomeSettings.weight;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatInfo;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Get extends SubCommand {
    
    public Get() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to get the biome weight of the last given TerrainGenerator of the biome settings" +
                " of the first given TerrainGenerator", ColorFormatter.infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain biomeSettings weight <name> get <name>
        
        if (args.length == 5) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                TerrainGenData getData = getGen(args[4]);
                if (getData != null) {
                    if (genData.getBiomeSettings().containsBiome(getData.getName())) {
                        player.sendMessage(formatInfo("The weight of TerrainGenerator %s is %s", getData.getName(), String.valueOf(genData.getBiomeSettings().getBiomeWeight(getData.getName()))));
                    } else {
                        player.sendMessage(formatError("TerrainGenerator %s is not a biomeGenerator in %s", getData.getName(), genData.getName()));
                    }
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[4]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings weight <name> get <name>"));
        }
    }
}
