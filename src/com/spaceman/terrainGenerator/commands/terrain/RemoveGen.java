package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fancyMessage.events.ClickEvent;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public class RemoveGen extends CmdHandler {

    @Override
    public String alias() {
        return "remove";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain removeGen <name> <name>

        if (args.length > 2) {
            TerrainGenData data = getGen(args[1]);

            if (data != null) {



                TerrainGenData addData = getGen(args[2]);

                if (addData != null) {
                    data.removeGenerator(addData.getName());
                    player.sendMessage(ChatColor.GREEN + "TerrainGenerator " +
                            ChatColor.DARK_GREEN + addData.getName() + ChatColor.GREEN + " successfully removed from TerrainGenerator " + ChatColor.DARK_GREEN + data.getName());
                } else {
                    player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[2] + ChatColor.RED + " does not exist");
                }
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " does not exist");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain removeGen <name> <name>");
        }
    }
}
