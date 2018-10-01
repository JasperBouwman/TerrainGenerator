package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.commands.terrain.example.ExampleHandler;
import com.spaceman.terrainGenerator.commands.terrain.example.Hell;
import com.spaceman.terrainGenerator.commands.terrain.example.Meadow;
import com.spaceman.terrainGenerator.commands.terrain.example.SnowMountain;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.commands.terrain.example.ExampleHandler.getExamples;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class Example extends CmdHandler {

    public Example() {

        ExampleHandler.addExample(new SnowMountain());
        ExampleHandler.addExample(new Meadow());
        ExampleHandler.addExample(new Hell());
    }

    @Override
    public String alias() {
        return "";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain example
        //terrain example <example name>
        //terrain example <example name> <x> <z>

        if (args.length > 3) {
            for (ExampleHandler action : getExamples()) {
                if (action.name() != null && action.name().equals(args[1])) {

                    int x = 0, z = 0;
                    boolean b = false;
                    try {
                        x = Integer.parseInt(args[2]);
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(ChatColor.DARK_RED + args[2] + ChatColor.RED + " is not a valid number");
                        b = true;
                    }
                    try {
                        z = Integer.parseInt(args[3]);
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(ChatColor.DARK_RED + args[3] + ChatColor.RED + " is not a valid number");
                        b = true;
                    }
                    if (b) return;

                    action.generate(x, z, player);
                    return;
                }
            }
            player.sendMessage(ChatColor.DARK_RED + args[1] + ChatColor.RED + " is not an existing example");
        } else if (args.length == 3) {
            player.sendMessage(ChatColor.RED + "Usage:" + ChatColor.DARK_RED + " /terrain example <example name> [<x> <z>]");
        } else if (args.length == 2) {
            for (ExampleHandler action : getExamples()) {
                if (action.name() != null && action.name().equals(args[1])) {
                    action.sendData(player);
                }
            }
        } else {
            Message message = new Message();

            message.addText(textComponent("Available examples: ", ChatColor.DARK_AQUA));

            boolean b = true;
            for (ExampleHandler action : getExamples()) {
                if (b) {
                    message.addText(textComponent(action.name(), ChatColor.BLUE));
                } else {
                    message.addText(textComponent(action.name(), ChatColor.DARK_GREEN));
                }
                message.addText(textComponent(", ", ChatColor.DARK_AQUA));
                b = !b;
            }
            message.removeLast();

            message.sendMessage(player);
        }

    }
}
