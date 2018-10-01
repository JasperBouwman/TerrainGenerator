package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;

public class Description extends CmdHandler {
    @Override
    public String alias() {
        return "des";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode description <TerrainMode name>

        if (args.length > 2) {

            TerrainMode mode = TerrainMode.getNewMode(args[2]);
            if (mode != null) {
                Message message = new Message();

                message.addText(textComponent("Description of ", ChatColor.DARK_AQUA));
                message.addText(textComponent(mode.getModeName(), ChatColor.BLUE));
                message.addText(textComponent(":\n", ChatColor.DARK_AQUA));
                message.addText(textComponent(ChatColor.BLUE + mode.getModeDescription() + ChatColor.BLUE, ChatColor.BLUE));
                message.addText(textComponent("\nIs final mode: ", ChatColor.DARK_AQUA));
                if (mode.isFinalMode()) {
                    message.addText(textComponent("True", ChatColor.GREEN));
                } else {
                    message.addText(textComponent("False", ChatColor.RED));
                }
                message.addText(textComponent("\nModeType: ", ChatColor.DARK_AQUA));
                if (mode instanceof TerrainMode.DataBased) {
                    message.addText(textComponent("DataBased", ChatColor.BLUE));
                    message.addText(textComponent("\nDefault value: ", ChatColor.DARK_AQUA));

                    Object o = ((TerrainMode.DataBased) mode).getModeData();
                    if (o != null) {
                        message.addText(textComponent(o.toString(), ChatColor.BLUE));
                    } else {
                        message.addText(textComponent("null", ChatColor.BLUE));
                    }
                } else if (mode instanceof TerrainMode.MapBased) {
                    message.addText(textComponent("MapBased", ChatColor.BLUE));
                    message.addText(textComponent("\nDefault value: ", ChatColor.DARK_AQUA));

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
                                hEvent.addText(textComponent(map.get(o).toString(), ChatColor.BLUE));
                            }

                            message.addText(textComponent(o.toString(), ChatColor.BLUE, hEvent));

                            message.addText(textComponent(", ", ChatColor.DARK_AQUA));
                            b = true;
                        }
                        if (b) message.removeLast();
                    } else {
                        message.addText(textComponent("null", ChatColor.BLUE));
                    }
                } else if (mode instanceof TerrainMode.ArrayBased) {
                    message.addText(textComponent("ArrayBased", ChatColor.BLUE));
                    message.addText(textComponent("\nDefault value: ", ChatColor.DARK_AQUA));

                    LinkedList<?> list = ((TerrainMode.ArrayBased) mode).getModeData();
                    if (list != null) {
                        boolean b = false;
                        for (Object o : list) {
                            message.addText(textComponent(o + "", ChatColor.BLUE));
                            message.addText(textComponent(", ", ChatColor.DARK_AQUA));
                            b = true;
                        }
                        if (b) message.removeLast();
                    } else {
                        message.addText(textComponent("null", ChatColor.BLUE));
                    }
                }


                message.sendMessage(player);
            } else {
                player.sendMessage(ChatColor.RED + "TerrainMode was not found");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain mode description <TerrainMode name>");
        }
    }
}
