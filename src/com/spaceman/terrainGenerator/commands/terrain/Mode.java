package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.terrain.mode.*;
import com.spaceman.terrainGenerator.commands.terrain.mode.Clone;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commandHander.CommandTemplate.runCommands;

public class Mode extends SubCommand {

    public Mode() {
        addAction(new Add());
        addAction(new Description());
        addAction(new com.spaceman.terrainGenerator.commands.terrain.mode.Edit());
        addAction(new com.spaceman.terrainGenerator.commands.terrain.mode.GetData());
        addAction(new com.spaceman.terrainGenerator.commands.terrain.mode.List());
        addAction(new Remove());
        addAction(new Set());
        addAction(new Inverse());
        addAction(new WaterLog());
        addAction(new Clone());
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode add <name> <TerrainMode name> [data]
        //terrain mode edit <name> <TerrainMode name> add <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> remove <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> set <number> <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> set <data...> {for DataBased only}
        //terrain mode getData <name> <TerrainMode name>
        //terrain mode remove <name> <TerrainMode name>
        //terrain mode clone <name> <name> <TerrainMode name>
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //terrain mode description <TerrainMode name>
        //terrain mode list
        //terrain mode inverse <name> <TerrainMode name> <boolean>
        //terrain mode inverse <name> <TerrainMode name>
        //terrain mode waterLog <name> <TerrainMode name> set <data...>
        //terrain mode waterLog <name> <TerrainMode name>

        if (args.length > 1) {
            if (!runCommands(getActions(), args[1], args, player)) {
                player.sendMessage(formatError("You must use valid arguments to use this command"));
            }
        } else {
            player.sendMessage(formatError("You must use valid arguments to use this command"));
        }
    }
}
