package com.spaceman.terrainGenerator.commands.terrain.external;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;

public class List extends SubCommand {

    public List() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            java.util.List<String> list = terrainGenData.keySet().stream().filter(gen -> gen.contains("/")).map((gen) -> gen.split("/")[0]).distinct().collect(Collectors.toList());
            list.removeAll(Arrays.asList(args).subList(2, args.length));
            return list;
        });
        emptyCommand.setLooped(true);
        emptyCommand.setCommandName("file...", ArgumentType.OPTIONAL);
        emptyCommand.setCommandDescription(textComponent("This command is used to list all external TerrainGenerators from the given External Terrain Files", infoColor));
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return terrainGenData.keySet().stream().filter(gen -> gen.contains("/")).map((gen) -> gen.split("/")[0]).distinct().collect(Collectors.toList());
    }
    
    @Override
    public Message getCommandDescription() {
        return new Message(textComponent("This command is used to list all external TerrainGenerators from all External Terrain Files", infoColor));
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain external list [file...]

        java.util.List<String> list = terrainGenData.keySet().stream().filter(gen -> gen.contains("/")).map((gen) -> gen.split("/")[0]).distinct().collect(Collectors.toList());

        if (args.length > 2) {
            ArrayList<String> newList = new ArrayList<>();
            for (String file : Arrays.asList(args).subList(2, args.length)) {
                if (list.contains(file)) {
                    newList.add(file);
                } else {
                    player.sendMessage(formatError("File %s is not loaded", file));
                }
            }
            list = newList;
        }
        
        if (list.isEmpty()) {
            player.sendMessage(formatInfo("There are no external TerrainGenerators found"));
            return;
        }

        for (String file : list) {
            Message message = new Message();
            message.addText(textComponent("Loaded TerrainGenerators from file ", ColorFormatter.infoColor));
            message.addText(textComponent(file, ColorFormatter.varInfoColor));
            message.addText(textComponent(":\n", ColorFormatter.infoColor));
            boolean color = true;
            for (String gen : terrainGenData.keySet()) {
                if (gen.startsWith(file + "/")) {
                    if (color) {
                        message.addText(textComponent(gen, ColorFormatter.varInfoColor, TerrainGenData.toHoverEvent(gen)));
                    } else {
                        message.addText(textComponent(gen, ColorFormatter.varInfo2Color, TerrainGenData.toHoverEvent(gen)));
                    }
                    color = !color;
                    message.addText(textComponent(", ", ColorFormatter.infoColor));
                }
            }
            message.sendMessage(player);
        }

    }
}
