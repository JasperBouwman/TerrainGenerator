package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.removeGen;

public class Delete extends CmdHandler {

    @Override
    public String alias() {
        return "del";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain delete <name>

        if (args.length > 1) {
            int removeState = removeGen(args[1]);
            if (removeState == 1) {
                player.sendMessage(ChatColor.GREEN + "Successfully deleted TerrainGenerator " + ChatColor.DARK_GREEN + args[1]);
            } else if (removeState == 2) {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " does not exist");
            } else if (removeState == 3) {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " is locked, unlock using" + ChatColor.DARK_RED + "/terrain lock <name>");//todo
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain delete <name>");
        }
    }
}
