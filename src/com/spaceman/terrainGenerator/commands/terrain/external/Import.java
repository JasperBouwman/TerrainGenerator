package com.spaceman.terrainGenerator.commands.terrain.external;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.*;

public class Import extends SubCommand {

    public Import() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> {
            Files externalFile = new Files("/etf", args[2]);
            List<String> argsList = Arrays.asList(args).subList(3, args.length);
        
            return externalFile.getKeys("terrainGenData").stream()
                    .filter(s -> terrainGenData.containsKey(args[2] + "/" + s))
                    .filter(s -> !argsList.contains(s))
                    .collect(Collectors.toCollection(ArrayList::new));
        });
        emptyCommand1.setLooped(true);
        emptyCommand1.setCommandName("TerrainGenerator...", ArgumentType.OPTIONAL);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to import all the given TerrainGenerators from the given External Terrain File", ColorFormatter.infoColor));
    
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable(emptyCommand1.getTabRunnable());
        emptyCommand.setCommandName("fileName", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to import all the TerrainGenerators from the given External Terrain File, " +
                "if a TerrainGenerator is already loaded with the same name it won't be loaded", ColorFormatter.infoColor));
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return terrainGenData.keySet().stream().filter(s -> s.contains("/")).map(s -> s.split("/")[0]).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain external import <fileName> [TerrainGenerator...]

        if (args.length > 2) {

            Files externalFile = new Files("/etf", args[2]);
            ArrayList<String> list = new ArrayList<>();
            if (args.length > 3) {
                for (String arg : Arrays.asList(args).subList(3, args.length)) {
                    if (externalFile.getConfig().contains("terrainGenData." + arg)) {
                        list.add(args[2] + "/" + arg);
                    }
                }
            } else {
                list = terrainGenData.keySet().stream().filter(s -> s.startsWith(args[2] + "/")).collect(Collectors.toCollection(ArrayList::new));
            }

            if (list.isEmpty()) {
                player.sendMessage(formatError("Given External Terrain File was not found/loaded, or given file doesn't have the given TerrainGenerator"));
                return;
            }

            for (String s : list) {
                String name = s.split("/")[1];
                if (terrainGenData.containsKey(name)) {
                    player.sendMessage(formatError("TerrainGenerator %s already exist", name));
                    continue;
                }

                initGenerator("", externalFile.getConfig().getConfigurationSection("terrainGenData." + name));
                TerrainGenData terrainGenData = getGen(name);
                if (terrainGenData != null) {
                    terrainGenData.getModes().forEach(TerrainMode::demapTerrainGenerators);
                    terrainGenData.demapGenerators();
                }
                player.sendMessage(formatSuccess("Loaded " + s));
            }


        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain external import <fileName> [TerrainGenerators...]"));
        }

    }
}
