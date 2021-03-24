package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.terrain.noise.Clone;
import com.spaceman.terrainGenerator.commands.terrain.noise.Edit;
import com.spaceman.terrainGenerator.commands.terrain.noise.GetData;
import com.spaceman.terrainGenerator.commands.terrain.noise.List;
import com.spaceman.terrainGenerator.commands.terrain.noise.*;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatInfo;
import static com.spaceman.terrainGenerator.commandHander.CommandTemplate.runCommands;

public class Noise extends SubCommand {
    
    public Noise() {
        addAction(new Set());
        addAction(new GetData());
        addAction(new Edit());
        addAction(new Clone());
        addAction(new Description());
        addAction(new List());
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain noise set <name> <noise> [data...]
        //terrain noise edit <name> <data...>
        //terrain noise getData <name>
        //terrain noise clone <name from> <name to>
        //terrain noise description <noise>
        //terrain noise list
        
        if (args.length > 1) {
            if (!runCommands(getActions(), args[1], args, player)) {
                player.sendMessage(formatError("Usage: %s", "/terrain noise <getData|set|edit|clone|description|list>"));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain noise <getData|set|edit|clone|description|list>"));
        }
    }
}
