package com.spaceman.terrainGenerator.commands.terrain.example;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;

public abstract class ExampleHandler {

    private static ArrayList<ExampleHandler> examples = new ArrayList<>();

    public static boolean addExample(ExampleHandler exampleHandler) {
        for (ExampleHandler handler : examples) {
            if (handler.name().equals(exampleHandler.name())) {
                return false;
            }
        }
        examples.add(exampleHandler);
        return true;
    }

    public static ArrayList<ExampleHandler> getExamples() {
        return examples;
    }

    public static boolean removeExample(ExampleHandler exampleHandler) {
        return examples.remove(exampleHandler);
    }

    public abstract void generate(int x, int z, Player player);

    public abstract void sendData(Player player);

    public abstract String name();

    protected void sendData(Player player, String... generators) {

        player.sendMessage("Generators: ");

        for (String generator : generators) {

            TerrainGenData data = getGen(generator);
            if (data == null) {
                continue;
            }

            Message message = new Message();

            message.addText(textComponent("TerrainGenerator data of ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getName(), ChatColor.BLUE));
            message.addText(textComponent(": ", ChatColor.DARK_AQUA));
            message.addText(textComponent("\nFrequency: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getFrequency() + "", ChatColor.BLUE));
            message.addText(textComponent(", Amplitude: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getAmplitude() + "", ChatColor.BLUE));
            message.addText(textComponent(", Multitude: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getMultitude() + "", ChatColor.BLUE));
            message.addText(textComponent(", Scale: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getScale() + "", ChatColor.BLUE));
            message.addText(textComponent(", Octaves: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getOctaves() + "", ChatColor.BLUE));
            message.addText(textComponent(", Height: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getHeight() + "", ChatColor.BLUE));
            message.addText(textComponent(", Seed: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getSeed() + "", ChatColor.BLUE));
            message.addText(textComponent(", FromTop: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getFromTop() + "", ChatColor.BLUE));
            message.addText(textComponent(", Start: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getStart() + "", ChatColor.BLUE));
            message.addText(textComponent(", Block: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getTerrainBlockData().toString(), ChatColor.BLUE));
            message.addText(textComponent(", Biome: ", ChatColor.DARK_AQUA));
            message.addText(textComponent(data.getBiome() + "", ChatColor.BLUE));
            message.addText(textComponent(", Generators: ", ChatColor.DARK_AQUA));
            boolean b1 = false;
            for (String gen : data.getGenerators()) {
                message.addText(textComponent(gen, ChatColor.BLUE));
                message.addText(textComponent(", ", ChatColor.DARK_AQUA));
                b1 = true;
            }
            if (b1) message.removeLast();


            for (TerrainMode mode : data.getModes()) {

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
                                boolean b2 = false;
                                for (Object v : c) {
                                    hEvent.addText(textComponent(v.toString(), ChatColor.BLUE));
                                    hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                                    b2 = true;
                                }
                                if (b2) hEvent.removeLast();
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
            }
            message.sendMessage(player);
        }
    }
}
