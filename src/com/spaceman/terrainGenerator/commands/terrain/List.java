package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGens;

public class List extends CmdHandler {

    @Override
    public String alias() {
        return "l";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain list

        Message message = new Message();

        message.addText(textComponent("Available TerrainGenerators: ", ChatColor.DARK_AQUA));
        boolean b1 = false;
        for (String s : getGens()) {

            TerrainGenData data = getGen(s);
            HoverEvent hEvent = hoverEvent("");
            hEvent.addText(textComponent("Frequency: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getFrequency() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nAmplitude: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getAmplitude() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nMultitude: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getMultitude() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nScale: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getScale() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nOctaves: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getOctaves() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nHeight: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getHeight() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nSeed: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getSeed() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nFromTop: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getFromTop() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nStart: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getStart() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nMaterial: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getTerrainBlockData().getMaterial() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nDirection: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getTerrainBlockData().getBlockFace() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nBiome: ", ChatColor.DARK_AQUA));
            hEvent.addText(textComponent(data.getBiome() + "", ChatColor.BLUE));
            hEvent.addText(textComponent("\nGenerators: ", ChatColor.DARK_AQUA));
            boolean b = false;
            for (String gen : data.getGenerators()) {
                hEvent.addText(textComponent(gen, ChatColor.BLUE));
                hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                b = true;
            }
            if (b)
                hEvent.removeLast();

            hEvent.addText(textComponent("\nModes: ", ChatColor.DARK_AQUA));
            boolean b2 = false;
            for (TerrainMode mode : data.getModes()) {
                hEvent.addText(textComponent(mode.getModeName(), ChatColor.BLUE));
                hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                b2 = true;
            }
            if (b2) hEvent.removeLast();

            message.addText(textComponent(s, ChatColor.BLUE, hEvent));
            message.addText(textComponent(", ", ChatColor.DARK_AQUA));
            b1 = true;
        }
        if (b1)
            message.removeLast();

        message.sendMessage(player);
    }
}
