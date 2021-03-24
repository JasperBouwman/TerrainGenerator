package com.spaceman.terrainGenerator.commands.terrain.noise;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;
import static com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise.getNewNoise;
import static com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise.getNoises;

public class List extends SubCommand {
    
    @Override
    public Message getCommandDescription() {
        return new Message(textComponent("This command is used to list all available NoiseGenerators", ColorFormatter.infoColor));
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain noise list

        if (args.length == 2) {
            Message message = new Message();
            message.addText(textComponent("Available TerrainNoises are: ", ColorFormatter.infoColor));
            message.addText(textComponent(""));
            boolean b = true;
            for (String noise : getNoises()) {

                TerrainNoise terrainNoise = getNewNoise(noise);
                if (terrainNoise == null) {
                    continue;
                }

                HoverEvent hEvent = terrainNoiseToHEvent(terrainNoise);

                if (b) {
                    message.addText(textComponent(terrainNoise.getName(), ColorFormatter.varInfoColor, hEvent));
                } else {
                    message.addText(textComponent(terrainNoise.getName(), ColorFormatter.varInfo2Color, hEvent));
                }
                b = !b;
                message.addText(textComponent(", ", ColorFormatter.infoColor));
            }

            message.removeLast();
            message.sendMessage(player);
        }

    }

    private HoverEvent terrainNoiseToHEvent(TerrainNoise noise) {
        HoverEvent hEvent = hoverEvent(textComponent("Description: ", ColorFormatter.infoColor));
        hEvent.addText(textComponent(ColorFormatter.varInfoColor + noise.getDescription() + ColorFormatter.varInfoColor, ColorFormatter.varInfoColor));

        hEvent.addText(textComponent("\nDefault data:\n", ColorFormatter.infoColor));
        hEvent.addMessage(noise.dataAsString());

        return hEvent;
    }
}
