package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.terrain.gens.Add;
import com.spaceman.terrainGenerator.commands.terrain.gens.List;
import com.spaceman.terrainGenerator.commands.terrain.gens.Remove;
import com.spaceman.terrainGenerator.commands.terrain.gens.Set;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commandHander.CommandTemplate.runCommands;

public class Gens extends SubCommand {

    public Gens() {
        addAction(new Add());
        addAction(new Remove());
        addAction(new Set());
        addAction(new List());
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain gens add <name> <name>
        //terrain gens remove <name> <name>
        //terrain gens set <name> <name> <number>
        //terrain gens list <name>
        if (args.length > 1) {
            if (!runCommands(getActions(), args[1], args, player)) {
                player.sendMessage(formatError("Usage: %s", "/terrain gens <add|remove|set>"));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain gens <add|remove|set>"));
        }
    }
}
