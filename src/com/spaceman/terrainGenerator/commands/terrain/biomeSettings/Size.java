package com.spaceman.terrainGenerator.commands.terrain.biomeSettings;

import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.terrain.biomeSettings.size.Get;
import com.spaceman.terrainGenerator.commands.terrain.biomeSettings.size.Own;
import com.spaceman.terrainGenerator.commands.terrain.biomeSettings.size.Set;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commandHander.CommandTemplate.runCommands;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;

public class Size extends SubCommand {
    
    public Size() {
        
        EmptyCommand emptyCommand = new EmptyCommand() {
            @Override
            public String getName(String argument) {
                return argument;
            }
        };
        emptyCommand.setRunnable((args, player) -> {
            //terrain biomeSettings size <name> <get|own|set>
            if (args.length > 3) {
                if (runCommands(emptyCommand.getActions(), args[3], args, player)) {
                    return;
                }
            }
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings size <name> <get|own|set>"));
            
        });
        emptyCommand.addAction(new Get());
        emptyCommand.addAction(new Own());
        emptyCommand.addAction(new Set());
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain biomeSettings size <name> own [size]
        //terrain biomeSettings size <name> set <name> <size>
        //terrain biomeSettings size <name> get <name>
        if (args.length > 2) {
            getActions().get(0).run(args, player);
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain biomeSettings size <name> <get|own|set>"));
        }
    }
}
