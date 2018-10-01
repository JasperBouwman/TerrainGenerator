package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fancyMessage.events.ClickEvent;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public class Set extends CmdHandler {

    @Override
    public String alias() {
        return "s";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode set <name> <TerrainMode name> <number> [data...]

        if (args.length >= 5) {
            TerrainGenData data = getGen(args[2]);

            if (data != null) {


                TerrainMode mode = TerrainMode.getNewMode(args[3], (args.length > 5 ? null : new LinkedList<>(Arrays.asList(args).subList(5, args.length))), player);

                if (mode != null) {

                    int number;
                    try {
                        number = Integer.parseInt(args[4]);
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(ChatColor.DARK_RED + args[4] + ChatColor.RED + " is not a valid number");
                        return;
                    }
                    if (number <= 0) {
                        player.sendMessage(ChatColor.RED + "Your given place must be higher than 0");
                        return;
                    }

                    LinkedList<TerrainMode> list = new LinkedList<>(data.getModes());
                    list.remove(mode);

                    LinkedList<TerrainMode> newList = new LinkedList<>();
                    if (number > list.size() + 1) {
                        player.sendMessage(ChatColor.RED + "Your given place must be lower than the amount of TerrainModes (" + ChatColor.DARK_RED + (list.size() + 1) + ChatColor.RED + ")");
                        return;
                    }
                    int i = 1;
                    boolean b = true;
                    for (TerrainMode tmpMode : list) {
                        if (i == number) {
                            newList.add(mode);
                            b = false;
                        }
                        newList.add(tmpMode);
                        i++;
                    }
                    if (b) {
                        newList.add(mode);
                    }

                    data.setModes(newList);

                    player.sendMessage(ChatColor.GREEN + "Successfully set TerrainMode to place " + ChatColor.DARK_GREEN + number);

                } else {
                    player.sendMessage(ChatColor.RED + "TerrainMode " + ChatColor.DARK_RED + args[3] + ChatColor.RED + " does not exist");
                }
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[2] + ChatColor.RED + " does not exist");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain mode <name> <TerrainMode name> <number> [data]");
        }

    }
}
