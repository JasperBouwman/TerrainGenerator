package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.terrain.biomeSettings.GetData;
import com.spaceman.terrainGenerator.commands.terrain.biomeSettings.*;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commandHander.CommandTemplate.runCommands;

public class BiomeSettings extends SubCommand {
    
    public BiomeSettings() {
        addAction(new Add());
        addAction(new BiomeScale());
        addAction(new GetData());
        addAction(new Remove());
        addAction(new SectionSize());
        addAction(new Seed());
        addAction(new Weight());
        addAction(new Size());
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain biomeSettings sectionSize <name> [size]
        //terrain biomeSettings biomeScale <name> [size]
        //terrain biomeSettings seed <name> [seed]
        //terrain biomeSettings add <name> <name> [data...]
        //terrain biomeSettings remove <name> <name>
        //terrain biomeSettings getData <name> [name]
        //terrain biomeSettings weight <name> own [weight]
        //terrain biomeSettings weight <name> set <name> <weight>
        //terrain biomeSettings weight <name> get <name>
        //terrain biomeSettings size <name> own [size]
        //terrain biomeSettings size <name> set <name> <size>
        //terrain biomeSettings size <name> get <name>
        
        if (args.length > 1) {
    
            if (!runCommands(getActions(), args[1], args, player)) {
                player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings <add|biomeScale|getData|remove|sectionSize|seed|weight|size>"));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings <add|biomeScale|getData|remove|sectionSize|seed|weight|size>"));
        }
    }
}
