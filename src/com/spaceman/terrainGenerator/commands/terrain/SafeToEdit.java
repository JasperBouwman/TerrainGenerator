package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.TabCompletes;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.terrain.SafeToEdit.SafeToEditClass.safeToEdit;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.customWorlds;

public class SafeToEdit extends SubCommand {

    @SuppressWarnings("WeakerAccess")
    public static class SafeToEditClass {

        private boolean safeToEdit;
        private ArrayList<String> worlds;

        private SafeToEditClass(boolean safeToEdit, ArrayList<String> worlds) {
            this.safeToEdit = safeToEdit;
            this.worlds = worlds;
        }

        public static SafeToEditClass safeToEdit(String terrainGenerator) {
            ArrayList<String> worlds = new ArrayList<>();
            boolean safeToEdit = true;
            for (String world : customWorlds.keySet()) {
                if (customWorlds.get(world).getTerrainGeneratorName().equals(terrainGenerator)) {
                    safeToEdit = false;
                    worlds.add(world);
                }
            }
            return new SafeToEditClass(safeToEdit, (safeToEdit ? null : worlds));
        }

        public ArrayList<String> getWorlds() {
            return worlds;
        }

        public boolean isSafeToEdit() {
            return safeToEdit;
        }
    }

    public SafeToEdit() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("TerrainGenerator", ArgumentType.OPTIONAL);
        emptyCommand.setCommandDescription(textComponent("This command only shows if the given TerrainGenerator is safe to edit", infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Message getCommandDescription() {
        return new Message(textComponent("This command tells you which TerrainGenerators are used in TerrainWorlds, and so if they are safe to edit. " +
                "What is means is that when a TerrainGenerator is used in a TerrainWorld the modifications on the TerrainGenerator are instantly applied to " +
                "chunk generation of those worlds. When a TerrainGenerator is safe to edit its not actively in use. A TerrainGenerator is always able to be edited " +
                "(but only if they are internal)", infoColor));
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return TabCompletes.availableGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain safeToEdit [TerrainGenerator]

        if (args.length  == 2) {
            SafeToEditClass safeToEdit = safeToEdit(args[1]);

            if (safeToEdit.isSafeToEdit()) {
                player.sendMessage(formatInfo("The TerrainGenerator is safe to edit"));
            } else {
                player.sendMessage(formatInfo("The TerrainGenerator is not safe to edit"));
            }
        } else if (args.length == 1) {
            Message message = new Message();
            message.addText(textComponent("SafeToEdit TerrainGenerators: ", infoColor));
            message.addText(textComponent());
            for (String s : terrainGenData.keySet()) {
                SafeToEditClass safeToEdit = safeToEdit(s);
                if (safeToEdit.isSafeToEdit()) {
                    message.addText(textComponent(s, ChatColor.GREEN));
                } else {
                    HoverEvent hEvent = hoverEvent(textComponent("Worlds: ", infoColor));

                    for (String world : safeToEdit.getWorlds()) {
                        hEvent.addText(textComponent(world, ColorFormatter.varInfoColor));
                        hEvent.addText(textComponent(", ", infoColor));
                    }
                    hEvent.removeLast();
                    message.addText(textComponent(s, ChatColor.RED, hEvent));
                }
                message.addText(textComponent(", ", infoColor));

            }
            message.removeLast();

            message.sendMessage(player);
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain safeToEdit [name]"));
        }
    }
}
