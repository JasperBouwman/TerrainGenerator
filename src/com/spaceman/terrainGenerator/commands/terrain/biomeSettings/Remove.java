package com.spaceman.terrainGenerator.commands.terrain.biomeSettings;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Remove extends SubCommand {
    
    public Remove() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to remove a TerrainGenerator as biome from the first given TerrainGenerator", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            ArrayList<String> set = new ArrayList<>(getGen(args[2]).getBiomeSettings().getBiomeGenerators().keySet());
            set.remove(args[2]);
            return set;
        });
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        List<String> list = ownGenerators();
        list.remove(args[2]);
        return list;
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain biomeSettings remove <name> <name>
    
        if (args.length == 4) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                if (genData.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainGenData removeData = getGen(args[3]);
                if (removeData != null) {
                    if (genData.getName().equals(removeData.getName())) {
                        player.sendMessage(formatError("Can't remove it self"));
                        return;
                    }
                    if (genData.getBiomeSettings().getBiomeGenerators().containsKey(removeData.getName())) {
                        genData.getBiomeSettings().removeBiome(removeData.getName());
                        player.sendMessage(formatSuccess("Successfully removed biome %s in TerrainGenerator %s", removeData.getName(), genData.getName()));
                    } else {
                        player.sendMessage(formatError("TerrainGenerator %s is not a biome in TerrainGenerator %s", removeData.getName(), genData.getName()));
                    }
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings <name> <name>"));
        }
    }
}
