package com.spaceman.terrainGenerator.commands.terrain.biomeSettings;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.keyValueHelper.Key;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueError;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueHelper;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueTabArgument;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.*;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Add extends SubCommand {
    
    public Add() {
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setCommandName("data...", ArgumentType.OPTIONAL);
        emptyCommand2.setCommandDescription(TextComponent.textComponent("When given data (weight=2.2,size=4), the data will be set to that biome, " +
                "when no given data the biome will use its default values", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) ->
                //weight=345,size=34
                KeyValueHelper.constructTab(args[args.length -1],
                        new KeyValueTabArgument("weight", Collections.singletonList("<X>")),
                        new KeyValueTabArgument("size", Collections.singletonList("<X>")))
        );
        emptyCommand1.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to add a TerrainGenerator as biome to the first given TerrainGenerator", ColorFormatter.infoColor));
        emptyCommand1.addAction(emptyCommand2);
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            List<String> list = ownGenerators();
            list.removeAll(getGen(args[2]).getBiomeSettings().getBiomeGenerators().keySet());
            return list;
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
        //terrain biomeSettings add <name> <name> [data...]
        
        double weight = 0.5 + Math.random();
        int size = 10;
        if (args.length == 5) {
            try {
                try {
                    HashMap<String, Object> map = KeyValueHelper.constructObject(args[args.length - 1],
                            new Key("weight", Double::parseDouble, true),
                            new Key("size", Integer::parseInt, true));
                    if (map.containsKey("weight")) {
                        weight = (double) map.get("weight");
                    }
                    if (map.containsKey("size")) {
                        size = (int) map.get("size");
                    }
                } catch (KeyValueError keyValueError) {
                    keyValueError.sendMessage(player);
                }
            } catch (NumberFormatException nfe) {
                player.sendMessage(formatError("Given number is not a valid number"));
                return;
            }
        }
        if (args.length > 3 && args.length < 6) {
            
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                if (genData.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainGenData otherData = getGen(args[3]);
                if (genData.equals(otherData)) {
                    player.sendMessage(formatError("Can't add it self as biome"));
                    return;
                }
                if (otherData != null) {
                    if (!genData.getBiomeSettings().getBiomeGenerators().containsKey(otherData.getName())) {
                        genData.getBiomeSettings().addBiome(otherData.getName(), weight, size);
                        player.sendMessage(formatSuccess("Successfully added biome %s to TerrainGenerator %s with the weight of %s and size of %s",
                                otherData.getName(), genData.getName(), String.valueOf(weight), String.valueOf(size)));
                    } else {
                        player.sendMessage(formatError("TerrainGenerator %s is already a biome in TerrainGenerator %s", otherData.getName(), genData.getName()));
                    }
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings add <name> <name> [weight]"));
        }
    }
}
