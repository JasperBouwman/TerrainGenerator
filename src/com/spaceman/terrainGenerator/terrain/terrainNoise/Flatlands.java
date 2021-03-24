package com.spaceman.terrainGenerator.terrain.terrainNoise;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatInfo;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class Flatlands extends TerrainNoise {

    @Override
    public Message dataAsString() {
        return new Message(TextComponent.textComponent(formatInfo("noise: %s", String.valueOf(0))));
    }
    
    @Override
    public Message dataAsStringWithHover() {
        Message message = new Message();
        HoverEvent hoverEvent = HoverEvent.hoverEvent("");
        hoverEvent.addMessage(dataAsString());
        message.addText(textComponent(getName(), ColorFormatter.varInfoColor, hoverEvent));
        return message;
    }
    
    @Override
    public String getDescription() {
        return getName() + " is a noise generator which generates a flat terrain";
    }

    @Override
    public void editNoiseSettings(List<String> data, Player player) {
        if (data != null && data.size() > 0) {
            player.sendMessage(formatError("This TerrainNoise does not support any settings"));
        }
    }

    @Override
    public void saveNoise(ConfigurationSection section) {

    }

    @Override
    public TerrainNoise loadNoise(ConfigurationSection section) {
        return new Flatlands();
    }

    @Override
    public int noise(int x, int z, long seed) {
        return 0;
    }
}
