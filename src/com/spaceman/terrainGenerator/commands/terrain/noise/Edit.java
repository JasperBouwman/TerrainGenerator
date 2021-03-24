package com.spaceman.terrainGenerator.commands.terrain.noise;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Edit extends SubCommand {

    public Edit() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setLooped(true);
        emptyCommand1.setTabRunnable((args, player) -> {
            if (ownGenerators().contains(args[2])) {
                return getGen(args[2]).getTerrainNoise().tabList(args, player);
            } else {
                return Collections.emptyList();
            }
        });
        emptyCommand1.setCommandName("data...", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(textComponent("This command is used to edit the NoiseGenerator of the given TerrainGenerator. " +
                "The data must be in the correct format of the NoiseGenerator", ColorFormatter.infoColor));
    
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            if (ownGenerators().contains(args[2])) {
                return getGen(args[2]).getTerrainNoise().tabList(args, player);
            } else {
                return Collections.emptyList();
            }
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
        //terrain noise edit <name> <data...>
        if (args.length >= 4) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                if (genData.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainNoise terrainNoise = genData.getTerrainNoise();
                terrainNoise.editNoiseSettings(Arrays.asList(args).subList(3, args.length), player);
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain noise edit <name> <data...>"));
        }
    }
}
