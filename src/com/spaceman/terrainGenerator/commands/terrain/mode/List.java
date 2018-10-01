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
import static com.spaceman.terrainGenerator.terrain.TerrainMode.getModes;
import static com.spaceman.terrainGenerator.terrain.TerrainMode.getNewMode;

public class List extends CmdHandler {
    @Override
    public String alias() {
        return "l";
    }

    @Override
    public void run(String[] args, Player player) {

        Message message = new Message();
        message.addText(textComponent("Available TerrainModes are: ", ChatColor.DARK_AQUA));

        for (String mode : getModes()) {

            TerrainMode terrainMode = getNewMode(mode);
            if (terrainMode == null) {
                continue;
            }

            HoverEvent hEvent = hoverEvent(textComponent("Description: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(ChatColor.BLUE + terrainMode.getModeDescription() + ChatColor.BLUE, ChatColor.BLUE));
            hEvent.addText(textComponent("\nIs final mode: ", ChatColor.DARK_AQUA));
            if (terrainMode.isFinalMode()) {
                hEvent.addText(textComponent("True", ChatColor.GREEN));
            } else {
                hEvent.addText(textComponent("False", ChatColor.RED));
            }
            hEvent.addText(textComponent("\nModeType: ", ChatColor.DARK_AQUA));
            if (terrainMode instanceof TerrainMode.DataBased) {
                hEvent.addText(textComponent("DataBased", ChatColor.BLUE));

                hEvent.addText(textComponent("\nDefault value: ", ChatColor.DARK_AQUA));

                Object o = ((TerrainMode.DataBased) terrainMode).getModeData();
                if (o != null) {
                    hEvent.addText(textComponent(o.toString(), ChatColor.BLUE));
                } else {
                    hEvent.addText(textComponent("null", ChatColor.BLUE));
                }
            } else if (terrainMode instanceof TerrainMode.MapBased) {
                hEvent.addText(textComponent("MapBased", ChatColor.BLUE));

                hEvent.addText(textComponent("\nDefault value: ", ChatColor.DARK_AQUA));

                HashMap<?, ?> map = ((TerrainMode.MapBased) terrainMode).getModeData();
                if (map != null) {
                    boolean b = false;

                    for (Object o : map.keySet()) {

                        StringBuilder str = new StringBuilder();

                        if (map.get(o) instanceof Collection) {
                            Collection c = (Collection) map.get(o);
                            boolean b1 = false;
                            for (Object v : c) {
                                str.append(v.toString()).append(", ");

                                b1 = true;
                            }
                            String s = str.toString();
                            if (b1) {
                                s = s.substring(0, s.length() - 2);
                            }

                            String k = o.toString();
                            String v = s;

                            str.append("{").append(k).append("=").append(v).append("}");

                            hEvent.addText(textComponent(str.toString(), ChatColor.BLUE));
                            hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                            b = true;

                        } else {
                            Object oV = map.get(o);

                            String k = o.toString();
                            String v = oV.toString();

                            str.append("{").append(k).append("=").append(v).append("}");

                            hEvent.addText(textComponent(str.toString(), ChatColor.BLUE));
                            hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                            b = true;
                        }

                    }
                    if (b) hEvent.removeLast();
                } else {
                    hEvent.addText(textComponent("null", ChatColor.BLUE));
                }
            } else if (terrainMode instanceof TerrainMode.ArrayBased) {
                hEvent.addText(textComponent("ArrayBased", ChatColor.BLUE));

                hEvent.addText(textComponent("\nDefault value: ", ChatColor.DARK_AQUA));

                LinkedList<?> list = ((TerrainMode.ArrayBased) terrainMode).getModeData();
                if (list != null) {
                    boolean b = false;
                    for (Object o : list) {
                        hEvent.addText(textComponent(o.toString(), ChatColor.BLUE));
                        hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                        b = true;
                    }
                    if (b) hEvent.removeLast();
                } else {
                    hEvent.addText(textComponent("null", ChatColor.BLUE));
                }
            }

            message.addText(textComponent(terrainMode.getModeName(), ChatColor.BLUE, hEvent));
            message.addText(textComponent(", ", ChatColor.DARK_BLUE));
        }

        message.removeLast();
        message.sendMessage(player);

    }
}
