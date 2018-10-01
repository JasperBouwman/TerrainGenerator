package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public class GetData extends CmdHandler {

    @Override
    public String alias() {
        return "d";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode getData <name> <TerrainMode name>

        if (args.length >= 4) {

            TerrainGenData genData = getGen(args[2]);

            if (genData != null) {

                TerrainMode mode = genData.getMode(args[3]);

                if (mode != null) {
                    Message message = new Message();

                    message.addText(textComponent("TerrainMode data of ", ChatColor.DARK_AQUA));
                    message.addText(textComponent(mode.getModeName(), ChatColor.BLUE));
                    message.addText(textComponent(" is: \n", ChatColor.DARK_AQUA));

                    if (mode instanceof TerrainMode.DataBased) {
                        Object o = ((TerrainMode.DataBased) mode).getModeData();
                        message.addText(textComponent(o.toString() + "", ChatColor.BLUE));

                    } else if (mode instanceof TerrainMode.MapBased) {

                        HashMap<?, ?> map = ((TerrainMode.MapBased) mode).getModeData();

                        if (map != null) {
                            boolean b = false;
                            for (Object o : map.keySet()) {
                                HoverEvent hEvent = hoverEvent("");

                                if (map.get(o) instanceof Collection) {
                                    Collection c = (Collection) map.get(o);
                                    boolean b1 = false;
                                    for (Object v : c) {
                                        hEvent.addText(textComponent(v.toString(), ChatColor.BLUE));
                                        hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                                        b1 = true;
                                    }
                                    if (b1) hEvent.removeLast();
                                } else {
                                    hEvent.addText(textComponent((map.get(o)).toString(), ChatColor.BLUE));

                                }
                                message.addText(textComponent(o.toString(), ChatColor.BLUE, hEvent));
                                message.addText(textComponent(", ", ChatColor.DARK_AQUA));
                                b = true;
                            }
                            if (b) message.removeLast();
                        }

                    } else if (mode instanceof TerrainMode.ArrayBased) {
                        LinkedList<?> list = ((TerrainMode.ArrayBased) mode).getModeData();
                        if (list != null) {
                            boolean b = false;
                            for (Object o : list) {
                                message.addText(textComponent(o + "", ChatColor.BLUE));
                                message.addText(textComponent(", ", ChatColor.DARK_AQUA));
                                b = true;
                            }
                            if (b) message.removeLast();
                        }
                    }

                    message.sendMessage(player);
                } else {
                    player.sendMessage(ChatColor.RED + "TerrainMode " + ChatColor.DARK_RED + args[3] + ChatColor.RED + " was not found");
                }
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[2] + ChatColor.RED + " does not exist");
            }


        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain mode getData <name> <TerrainMode name>");
        }

    }
}
