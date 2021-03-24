package com.spaceman.terrainGenerator.commands.terrain.noise;

import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Set extends SubCommand {

    public Set() {
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setTabRunnable((args, player) -> {
            if (ownGenerators().contains(args[2])) {
                return TerrainNoise.getNewNoise(args[3]).tabList(args, player);
            } else {
                return Collections.emptyList();
            }
        });
        emptyCommand2.setLooped(true);
        emptyCommand2.setCommandName("data...", ArgumentType.OPTIONAL);
        emptyCommand2.setCommandDescription(textComponent("When given data, the given data will be set to that NoiseGenerator. If its not given it will fallback to its default value",
                infoColor));
    
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable(emptyCommand2.getTabRunnable());
        emptyCommand1.setCommandName("noise", ArgumentType.REQUIRED);
        emptyCommand1.addAction(emptyCommand2);
        emptyCommand1.setCommandDescription(textComponent("This command is used to set the noise type to the given TerrainGenerator", infoColor));

        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            if (ownGenerators().contains(args[2])) {
                return TerrainNoise.getNoises();
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
        //terrain noise set <name> <noise> [data...]
        if (args.length >= 4) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                if (genData.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainNoise terrainNoise = TerrainNoise.getNewNoise(args[3], (args.length == 4 ? null : new LinkedList<>(Arrays.asList(args).subList(4, args.length))), player);
                if (terrainNoise != null) {
                    genData.setTerrainNoise(terrainNoise);
                    player.sendMessage(formatSuccess("Successfully edited the TerrainNoise in TerrainGenerator %s to %s", genData.getName(), terrainNoise.getName()));
                } else {
                    player.sendMessage(formatError("TerrainNoise %s does not exist", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain noise set <name> <noise> [data...]"));
        }

    }
}
