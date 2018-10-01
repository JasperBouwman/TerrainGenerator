package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fancyMessage.events.ClickEvent;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public class SetGen extends CmdHandler {

    @Override
    public String alias() {
        return "set";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain setGen <name> <name> <number>

        if (args.length > 3) {
            TerrainGenData data = getGen(args[1]);

            if (data != null) {


                TerrainGenData addData = getGen(args[2]);

                if (addData != null) {

                    int number;
                    try {
                        number = Integer.parseInt(args[3]);
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(ChatColor.DARK_RED + args[3] + ChatColor.RED + " is not a valid number");
                        return;
                    }
                    if (number <= 0) {
                        player.sendMessage(ChatColor.RED + "Your given place must be higher than 0");
                        return;
                    }

                    LinkedList<String> list = new LinkedList<>(data.getGenerators());
                    list.remove(addData.getName());

                    LinkedList<String> newList = new LinkedList<>();
                    if (number > list.size() + 1) {
                        player.sendMessage(ChatColor.RED + "Your given place must be lower than the amount of TerrainGenerators (" + ChatColor.DARK_RED + (list.size() + 1) + ChatColor.RED + ")");
                        return;
                    }
                    int i = 1;
                    boolean b = true;
                    for (String gen : list) {
                        if (i == number) {
                            newList.add(addData.getName());
                            b = false;
                        }
                        newList.add(gen);
                        i++;
                    }
                    if (b) {
                        newList.add(addData.getName());
                    }

                    data.setGenerators(newList);

                    player.sendMessage(ChatColor.GREEN + "Successfully set TerrainGenerator to place " + ChatColor.DARK_GREEN + number);

                } else {
                    player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[2] + ChatColor.RED + " does not exist");
                }
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " does not exist");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain setGen <name> <name> <number>");
        }
    }
}
