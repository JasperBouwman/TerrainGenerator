package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class GetData extends CmdHandler {

    @Override
    public String alias() {
        return "d";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain getData <name>

        if (args.length > 1) {
            TerrainGenData data = getGen(args[1]);

            if (data != null) {

                Message message = new Message();

                //TerrainGenData properties
                message.addText(textComponent("TerrainGenerator data of ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getName(), ChatColor.BLUE));
                message.addText(textComponent(": ", ChatColor.DARK_AQUA));
                message.addText(textComponent("\nFrequency: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getFrequency() + "", ChatColor.BLUE));
                message.addText(textComponent("\nAmplitude: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getAmplitude() + "", ChatColor.BLUE));
                message.addText(textComponent("\nMultitude: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getMultitude() + "", ChatColor.BLUE));
                message.addText(textComponent("\nScale: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getScale() + "", ChatColor.BLUE));
                message.addText(textComponent("\nOctaves: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getOctaves() + "", ChatColor.BLUE));
                message.addText(textComponent("\nHeight: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getHeight() + "", ChatColor.BLUE));
                message.addText(textComponent("\nSeed: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getSeed() + "", ChatColor.BLUE));
                message.addText(textComponent("\nFromTop: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getFromTop() + "", ChatColor.BLUE));
                message.addText(textComponent("\nStart: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getStart(), ChatColor.BLUE));
                message.addText(textComponent("\nBlock: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getTerrainBlockData().toString(), ChatColor.BLUE));
                message.addText(textComponent("\nBiome: ", ChatColor.DARK_AQUA));
                message.addText(textComponent(data.getBiome() + "", ChatColor.BLUE));
                message.addText(textComponent("\nGenerators: ", ChatColor.DARK_AQUA));
                boolean b1 = false;
                for (String gen : data.getGenerators()) {
                    message.addText(textComponent(gen, ChatColor.BLUE));
                    message.addText(textComponent(", ", ChatColor.DARK_AQUA));
                    b1 = true;
                }
                if (b1)
                    message.removeLast();
                message.addText(textComponent("\nModes: ", ChatColor.DARK_AQUA));
                boolean b2 = false;
                for (TerrainMode mode : data.getModes()) {

                    HoverEvent hEvent = HoverEvent.hoverEvent(textComponent("Data: ", ChatColor.DARK_AQUA));

                    if (mode instanceof TerrainMode.DataBased) {
                        hEvent.addText(textComponent(((TerrainMode.DataBased) mode).getModeData() + "", ChatColor.BLUE));
                    } else if (mode instanceof TerrainMode.MapBased) {
                        hEvent.addText(textComponent(((TerrainMode.MapBased) mode).getModeData() + "", ChatColor.BLUE));
                    } else if (mode instanceof TerrainMode.ArrayBased) {
                        hEvent.addText(textComponent(((TerrainMode.ArrayBased) mode).getModeData() + "", ChatColor.BLUE));
                    }

                    message.addText(textComponent(mode.getModeName(), ChatColor.BLUE, hEvent));
                    message.addText(textComponent(", ", ChatColor.DARK_AQUA));
                    b2 = true;
                }
                if (b2)
                    message.removeLast();

                message.sendMessage(player);
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " does not exist");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain getData <name>");
        }

    }
}
