package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fancyMessage.events.ClickEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class Edit extends CmdHandler {
    @Override
    public String alias() {
        return "e";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode edit <name> <TerrainMode name> add <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> remove <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> set <number> <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> set <data...> {for DataBased only}

        if (args.length > 5) {
            TerrainGenData data = getGen(args[2]);
            if (data != null) {



                TerrainMode mode = data.getMode(args[3]);
                if (mode != null) {
                    if (args[4].equalsIgnoreCase("set")) {
                        if (mode instanceof TerrainMode.DataBased) {
                            ((TerrainMode.DataBased) mode).setData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                            return;
                        }
                        if (args.length > 6) {
                            if (mode instanceof TerrainMode.MapBased) {
                                int i;
                                try {
                                    i = Integer.parseInt(args[5]);
                                } catch (NumberFormatException nfe) {
                                    player.sendMessage(ChatColor.DARK_RED + args[5] + ChatColor.RED + " is not a valid number");
                                    return;
                                }
                                ((TerrainMode.MapBased) mode).setData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), i, player);
                            } else if (mode instanceof TerrainMode.ArrayBased) {
                                int i;
                                try {
                                    i = Integer.parseInt(args[5]);
                                } catch (NumberFormatException nfe) {
                                    player.sendMessage(ChatColor.DARK_RED + args[5] + ChatColor.RED + " is not a valid number");
                                    return;
                                }
                                ((TerrainMode.ArrayBased) mode).setData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), i, player);
                            }
                        } else {
                            Message message = new Message();
                            message.addText(textComponent("Usage: ", ChatColor.RED));
                            HoverEvent hEvent = HoverEvent.hoverEvent("");
                            hEvent.addText(textComponent("This argument is only used for ", ChatColor.DARK_AQUA));
                            hEvent.addText(textComponent("set", ChatColor.BLUE));
                            hEvent.addText(textComponent(" and for the TerrainModes that are ", ChatColor.DARK_AQUA));
                            hEvent.addText(textComponent("ArrayBased", ChatColor.BLUE));
                            hEvent.addText(textComponent(" or ", ChatColor.DARK_AQUA));
                            hEvent.addText(textComponent("MapBased", ChatColor.BLUE));
                            message.addText(textComponent("/terrain mode edit <name> <TerrainMode name> set", ChatColor.DARK_RED));
                            message.addText(textComponent(" [<number>]", ChatColor.DARK_RED, hEvent));
                            message.addText(textComponent(" <data...>", ChatColor.DARK_RED));
                            message.sendMessage(player);
                        }
                    } else if (args[4].equalsIgnoreCase("add")) {

                        if (mode instanceof TerrainMode.MapBased) {
                            ((TerrainMode.MapBased) mode).addData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                        } else if (mode instanceof TerrainMode.ArrayBased) {
                            ((TerrainMode.ArrayBased) mode).addData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                        } else if (mode instanceof TerrainMode.DataBased) {
                            player.sendMessage(ChatColor.RED + "This command can not be used for a DataBased TerrainMode");
                        }

                    } else if (args[4].equalsIgnoreCase("remove")) {

                        if (mode instanceof TerrainMode.MapBased) {
                            ((TerrainMode.MapBased) mode).removeData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                        } else if (mode instanceof TerrainMode.ArrayBased) {
                            ((TerrainMode.ArrayBased) mode).removeData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                        } else if (mode instanceof TerrainMode.DataBased) {
                            player.sendMessage(ChatColor.RED + "This command can not be used for a DataBased TerrainMode");
                        }
                    }

                } else {
                    player.sendMessage(ChatColor.RED + "could not find TerrainMode " + ChatColor.DARK_RED + args[3]);
                }
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[2] + ChatColor.RED + " does not exist");
            }
        } else {
            Message message = new Message();
            message.addText(textComponent("Usage: ", ChatColor.RED));
            message.addText(textComponent("/terrain mode edit <name> <TerrainMode name> <add:remove:set> ", ChatColor.DARK_RED));
            HoverEvent hEvent = HoverEvent.hoverEvent("");
            hEvent.addText(textComponent("This argument is only used for command ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent("set", ChatColor.BLUE));
            hEvent.addText(textComponent(" and for the TerrainModes that are ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent("ArrayBased", ChatColor.BLUE));
            hEvent.addText(textComponent(" or ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent("MapBased", ChatColor.BLUE));
            message.addText(textComponent("[<number>]", ChatColor.DARK_RED, hEvent));
            message.addText(textComponent(" <data...>", ChatColor.DARK_RED));

            message.sendMessage(player);
        }

    }
}
