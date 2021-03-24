package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.terrain.world.*;
import com.spaceman.terrainGenerator.commands.terrain.world.Create;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commandHander.CommandTemplate.runCommands;

public class World extends SubCommand {

    public World() {
        addAction(new Create());
        addAction(new Unload());
        addAction(new com.spaceman.terrainGenerator.commands.terrain.world.List());
        addAction(new TP());
        addAction(new Regenerate());
        addAction(new AutoLoad());
        addAction(new WorldInfo());
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain world create <world name> <name> [data...]
        //terrain world unload <TerrainWorld> [boolean]
        //terrain world list
        //terrain world tp [world name]
        //terrain world regenerate [<x1> <z1> <x2> <z2>]
        //terrain world autoLoad <world name> [autoLoad]
        //terrain world worldInfo

        if (args.length > 1) {
            if (!runCommands(getActions(), args[1], args, player)) {
                player.sendMessage(formatError("Usage: %s", "/terrain world <create|unload|list|tp|regenerate|worldInfo>"));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain world <create|unload|list|tp|regenerate|worldInfo>"));
        }
    }

}
