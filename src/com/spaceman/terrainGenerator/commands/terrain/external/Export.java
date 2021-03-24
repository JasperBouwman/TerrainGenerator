package com.spaceman.terrainGenerator.commands.terrain.external;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.*;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.containsSpecialCharacter;

public class Export extends SubCommand {
    
    public Export() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> {
            List<String> list = ownGenerators();
            list.removeAll(Arrays.asList(args).subList(3, args.length));
            return list;
        });
        emptyCommand1.setCommandName("TerrainGenerator...", ArgumentType.OPTIONAL);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to export the given TerrainGenerator to an external .yml file. " +
                "The export function can mostly be used as a backup of your TerrainGenerators. " +
                "External TerrainGenerators can't be edited", infoColor));
        emptyCommand1.setLooped(true);
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable(emptyCommand1.getTabRunnable());
        emptyCommand.setCommandName("fileName", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to export all the TerrainGenerators to an external .yml file. " +
                "The export function can mostly be used as a backup of your TerrainGenerators. " +
                "External TerrainGenerators can't be edited", infoColor));
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain external export <fileName> [TerrainGenerator...]
        List<String> list;
        
        if (args.length == 3) {
            if (containsSpecialCharacter(args[2])) {
                player.sendMessage(formatError("Name can't contain any special characters"));
                return;
            }
            try {
                if (new File(Main.getInstance().getDataFolder() + "/etf/" + args[2] + ".yml").createNewFile()) {
                    player.sendMessage(formatSuccess("File successfully created"));
                } else {
                    player.sendMessage(formatError("Given file already exist, choose another name"));
                    return;
                }
            } catch (IOException e) {
                player.sendMessage(formatError("Could not create file, try again later...\n%s", e.getMessage()));
                return;
            }
            list = ownGenerators();
        } else if (args.length > 3) {
            if (containsSpecialCharacter(args[2])) {
                player.sendMessage(formatError("Name can't contain any special characters"));
                return;
            }
            try {
                if (new File(Main.getInstance().getDataFolder() + "/etf/" + args[2] + ".yml").createNewFile()) {
                    player.sendMessage(formatSuccess("File successfully created"));
                } else {
                    player.sendMessage(formatError("Given file already exist, choose another name"));
                    return;
                }
            } catch (IOException e) {
                player.sendMessage(formatError("Could not create file, try again later...\n%s", e.getMessage()));
                return;
            }
            
            list = new ArrayList<>();
            
            for (String arg : Arrays.asList(args).subList(3, args.length)) {
                if (getGen(arg) != null) {
                    if (arg.contains("/")) {
                        player.sendMessage(formatError("You can't export an external TerrainGenerator"));
                        continue;
                    }
                    list.add(arg);
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", arg));
                }
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain external export <fileName> [TerrainGenerators...]"));
            return;
        }
        
        Files externalData = new Files("/etf", args[2]);
        
        for (String name : list) {
            TerrainGenData genData = getGen(name);
            genData.getModes().forEach(terrainMode -> terrainMode.remapTerrainGenerators(args[2]));
            genData.remapGenerators(args[2]);
            
            player.sendMessage(formatInfo("Saving %s", name));
            saveGenerator(externalData, genData);
            
            genData.getModes().forEach(TerrainMode::demapTerrainGenerators);
            genData.demapGenerators();
        }
        externalData.saveConfig();
        
        if (externalData.getConfig().contains("terrainGenData")) {
            for (String name : externalData.getKeys("terrainGenData")) {
                try {
                    initGenerator(args[2] + "/", externalData.getConfig().getConfigurationSection("terrainGenData." + name));
                } catch (Exception e) {
                    Main.log(Level.WARNING, "The TerrainGenerator " + name + " could not be fully loaded. " + e.getMessage());
                }
            }
        }
    }
}
