package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.biome.TerrainGrid;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatInfo;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.initGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.saveGenerators;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.initTerrainWorlds;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.saveTerrainWorlds;

public class Reload extends SubCommand {
    
    @Override
    public Message getCommandDescription() {
        return new Message(TextComponent.textComponent("This command is mostly used to save the TerrainGenerators, " +
                "and if possible in a future update it will stop loading terrain creation", ColorFormatter.infoColor));
    }
    
    @Override
    public void run(String[] args, Player player) {
        if (args.length == 1) {
            //terrain reload
            player.sendMessage(formatInfo("Saving TerrainGenerators and TerrainWorlds"));
            saveGenerators();
            saveTerrainWorlds();
            TerrainGrid.clearSectionCache();
            
            //todo cancel all loading of terrains

            player.sendMessage(formatInfo("Loading TerrainGenerators and TerrainWorlds"));
            initGenerators();
            initTerrainWorlds();
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain reload"));
        }

    }
}
