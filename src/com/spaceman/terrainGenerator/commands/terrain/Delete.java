package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGeneratorUsedException;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.removeGen;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.customWorlds;

public class Delete extends SubCommand {

    public Delete() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(TextComponent.textComponent("This command is used to remove a TerrainGenerator. " +
                "You can't remove a TerrainGenerator that is used in a TerrainWorld, or when its an external TerrainGenerator", ColorFormatter.infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        List<String> list = ownGenerators();
        list.removeAll(customWorlds.values().stream().map(WorldGenerator::getTerrainGeneratorName).collect(Collectors.toList()));
        return list;
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain delete <name>

        if (args.length == 2) {
            try {
                removeGen(args[1]);
                player.sendMessage(formatSuccess("Successfully deleted TerrainGenerator %s", args[1]));
            } catch (TerrainGeneratorUsedException | IllegalArgumentException e) {
                player.sendMessage(e.getMessage());
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain delete <name>"));
        }
    }
}
