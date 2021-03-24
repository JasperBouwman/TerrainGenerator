package com.spaceman.terrainGenerator.commands.terrain.world;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.keyValueHelper.Key;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueError;
import com.spaceman.terrainGenerator.keyValueHelper.KeyValueHelper;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.worldPrefix;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.containsSpecialCharacter;

public class Create extends SubCommand {

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        List<String> list = new ArrayList<>();
        File file = new File(Main.getInstance().getDataFolder() + "/worlds");
        list.add("<worldName>");
        if (file.isDirectory()) {
            list.addAll(Arrays.stream(Objects.requireNonNull(file.listFiles(File::isDirectory))).map(File::getName).collect(Collectors.toList()));
            list.removeAll(Bukkit.getWorlds().stream().filter(world -> world.getName().startsWith(worldPrefix)).map(world -> world.getName().replace(worldPrefix, "")).collect(Collectors.toList()));
        }
        return list;
    }

    public Create() {
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setCommandName("data...", ArgumentType.OPTIONAL);
        emptyCommand2.setCommandDescription(textComponent("If data is given this will be applied to the newly created TerrainWorld", infoColor));
        
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> Arrays.asList("autoLoad=true", "autoLoad=false"));
        emptyCommand1.setCommandName("TerrainGenerator", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(textComponent("This command is used to create a new TerrainWorld with the given TerrainGenerator as chunk generator. " +
                "The tab list is a collection of all available TerrainWorlds who are not loaded, but have been created before", infoColor));
        emptyCommand1.addAction(emptyCommand2);
        
        EmptyCommand emptyCommand = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return argument;
            }
        };
        emptyCommand.setTabRunnable((args, player) -> availableGenerators());
        emptyCommand.addAction(emptyCommand1);
        emptyCommand.setCommandName("world name", ArgumentType.REQUIRED);
        addAction(emptyCommand);
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain world create <world name> <TerrainGenerator> [data...]
        boolean autoLoad = true;
        
        if (args.length == 5) {
            try {
                HashMap<String, Object> map = KeyValueHelper.constructObject(args[4], new Key("autoLoad", Boolean::valueOf,false));
                autoLoad = (boolean) map.get("autoLoad");
            } catch (KeyValueError keyValueError) {
                keyValueError.sendMessage(player);
            }
        }
        if (args.length >= 4 && args.length < 6) {
            try {
                if (Bukkit.getWorld(worldPrefix + args[2]) != null) {
                    player.sendMessage(formatError("World %s already exist", args[2]));
                    return;
                }
                
                if (containsSpecialCharacter(args[2])) {
                    player.sendMessage(formatError("The name can't contain any special characters"));
                    return;
                }
                
                File folder = new File(Main.getInstance().getDataFolder().getPath() + "/worlds", args[2]);
                if (folder.exists()) {
                    player.sendMessage(formatError("The folder %s already exist, reusing it...", args[2]));
                }
                try {
                    World world = WorldGenerator.generateWorld(args[2], args[3], autoLoad);
                    player.teleport(world.getSpawnLocation());
                    player.sendMessage(formatSuccess("Teleported to worlds %s spawn location", args[2]));
                } catch (NullPointerException npe) {
                    player.sendMessage(formatError("Some TerrainGenerators do not exist anymore, but are used in other TerrainGenerators"));
                } catch (IllegalArgumentException iae) {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[3]));
                }

            } catch (NullPointerException npe) {
                player.sendMessage(formatError("The world %s could not be created...", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain world create <world name> <TerrainGenerator>"));
        }
    }
}
