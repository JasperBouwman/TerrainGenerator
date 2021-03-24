package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.terrain.external.Export;
import com.spaceman.terrainGenerator.commands.terrain.external.Import;
import com.spaceman.terrainGenerator.commands.terrain.external.List;
import com.spaceman.terrainGenerator.commands.terrain.external.Load;
import com.spaceman.terrainGenerator.commands.terrain.external.Unload;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commandHander.CommandTemplate.runCommands;

public class External extends SubCommand {

    public External() {
        addAction(new Export());
        addAction(new Import());
        addAction(new Unload());
        addAction(new Load());
        addAction(new List());
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain external export <fileName> [TerrainGenerator...]
        //terrain external import <fileName> [TerrainGenerator...]
        //terrain external unload <fileName> [delete]
        //terrain external load <file>
        //terrain external list [file...]
        
        if (args.length > 1) {
            if (!runCommands(getActions(), args[1], args, player)) {
                player.sendMessage(formatError("Usage: %s", "/terrain external <export|import|unload|load|list>"));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain external <export|import|unload|load|list>"));
        }
    }
}
