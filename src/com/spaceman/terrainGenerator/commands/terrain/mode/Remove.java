package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fancyMessage.events.ClickEvent;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public class Remove extends CmdHandler {
    @Override
    public String alias() {
        return "r";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode remove <name> <TerrainMode name>

        if (args.length == 4) {

            TerrainGenData data = getGen(args[2]);

            if (data != null) {


                if (data.removeMode(args[3])) {
                    player.sendMessage(ChatColor.GREEN + "Successfully removed the TerrainMode " + ChatColor.DARK_GREEN + args[3]);
                } else {
                    player.sendMessage(ChatColor.RED +"Could not find a TerrainMode called " + ChatColor.DARK_RED + args[3] +ChatColor.RED + ", therefore it could not be removed");
                }

            } else {
                player.sendMessage(ChatColor.RED +"TerrainGenerator " + ChatColor.DARK_RED + args[2] + ChatColor.RED + " was not found");
            }


        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain mode <name> <TerrainMode name>");
        }
    }
}
