package com.spaceman.terrainGenerator.commands.terrain.external;

import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fileHander.Files;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.initGenerator;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;

public class Load extends SubCommand {

    public Load() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("file", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to load an External Terrain File", infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return Arrays.stream(Objects.requireNonNull(new File(Main.getInstance().getDataFolder() + "/etf").listFiles(File::isFile)))
                .map(File::getName)
                .map((s) -> s.replace(".yml", ""))
                .filter(s -> terrainGenData.keySet().stream().noneMatch(a -> a.startsWith(s)))
                .collect(Collectors.toList());
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain external load <file>

        if (args.length == 3) {
            File file = new File(Main.getInstance().getDataFolder() + "/etf", args[2] + ".yml");
            if (file.isFile()) {
                if (file.getName().contains(" ")) {
                    Main.log(Level.WARNING, "External Terrain File '" + file.getName() + "' could not be loaded, these files can't contain any spaces");
                } else {
                    Files externalData = new Files("/etf", file.getName());

                    if (externalData.getConfig().contains("terrainGenData")) {
                        for (String name : externalData.getConfig().getConfigurationSection("terrainGenData").getKeys(false)) {
                            try {
                                initGenerator(file.getName().replaceAll("\\.yml.{0}", "") + "/",
                                        externalData.getConfig().getConfigurationSection("terrainGenData." + name));
                                player.sendMessage(formatSuccess("Loaded %s", name));
                            } catch (Exception e) {
                                Main.log(Level.WARNING, "The TerrainGenerator " + name + " could not be fully loaded. " + e.getMessage());
                            }
                        }
                        return;
                    }
                }
            }
            player.sendMessage(formatError("Some unexpected error occurred. The chosen file might not be loaded"));
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain external load <file>"));
        }

    }
}
