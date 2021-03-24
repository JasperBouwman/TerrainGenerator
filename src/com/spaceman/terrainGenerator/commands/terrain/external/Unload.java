package com.spaceman.terrainGenerator.commands.terrain.external;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.customWorlds;

public class Unload extends SubCommand {

    public Unload() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("delete", ArgumentType.OPTIONAL);
        emptyCommand1.setCommandDescription(textComponent("This command is used to unload the given External Terrain File, when the last argument is true " +
                "it will also delete the file, when false it wont", infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> Arrays.asList("true", "false"));
        emptyCommand.setCommandName("file", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(textComponent("This command is used to unload the given External Terrain File", infoColor));
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return terrainGenData.keySet().stream().filter(gen -> gen.contains("/")).map((gen) -> gen.split("/")[0]).collect(Collectors.toList());
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain external unload <file> [delete]
        if (args.length > 2 && args.length < 5) {
            boolean delete = false;
            if (args.length == 4) {
                delete = Main.parseBool(args[3]);
            }

            if (delete) {
                File file = new File(Main.getInstance().getDataFolder() + "/etf/" + args[2] + ".yml");
                if (file.exists()) {
                    if (file.delete()) {
                        player.sendMessage(formatSuccess("Successfully deleted External Terrain File %s", file.getName()));
                    } else {
                        player.sendMessage(formatError("Could not delete file... Please try again later"));
                    }
                } else {
                    player.sendMessage(formatError("Selected file does not exist anymore, the TerrainGenerators that where in the file will be unloaded"));
                }
            }

            ArrayList<String> list = new ArrayList<>();
            for (String gen : terrainGenData.keySet()) {
                if (gen.startsWith(args[2] + "/")) {
                    boolean b = true;
                    for (String worldName : customWorlds.keySet()) {
                        if (customWorlds.get(worldName).getTerrainGeneratorName().equals(gen)) {
                            player.sendMessage(formatError("Could not unload %s right now, this TerrainGenerator is used in a TerrainWorld. When on a reload/restart the world won't load", gen));
                            b = false;
                            break;
                        }
                    }

                    for (TerrainGenData tmpData : terrainGenData.values()) {
                        tmpData.getGenerators().remove(gen);
                    }

                    if (!b) {
                        player.sendMessage(formatSuccess("Removed most traces of %s", gen));
                    } else {
                        list.add(gen);
                    }
                }
            }

            for (String s : list) {
                terrainGenData.remove(s);
                player.sendMessage(formatSuccess("Unloaded and removed most traces of %s", s));
            }



        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain external unload <file> [delete]"));
        }
    }
}
