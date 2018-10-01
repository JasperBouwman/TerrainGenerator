package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.commands.terrain.mode.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Mode extends CmdHandler {

    public final static ArrayList<CmdHandler> actions = new ArrayList<>();

    public Mode() {
        actions.add(new Add());
        actions.add(new Copy());
        actions.add(new Description());
        actions.add(new com.spaceman.terrainGenerator.commands.terrain.mode.Edit());
        actions.add(new com.spaceman.terrainGenerator.commands.terrain.mode.GetData());
        actions.add(new com.spaceman.terrainGenerator.commands.terrain.mode.List());
        actions.add(new Remove());
        actions.add(new Set());
    }

    @Override
    public String alias() {
        return "m";
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
        //terrain mode copy <name> <name> <TerrainMode name>
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //terrain mode description <TerrainMode name>
        //terrain mode List

        if (args.length > 1) {
            for (CmdHandler action : actions) {
                if (args[1].equalsIgnoreCase(action.getClass().getSimpleName()) || action.alias().equalsIgnoreCase(args[1])) {
                    action.run(args, player);
                    return;
                }
            }
        }

        player.sendMessage(ChatColor.RED + "You must use valid arguments to use this command");

    }
}
