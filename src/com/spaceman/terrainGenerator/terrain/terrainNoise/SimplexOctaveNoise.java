package com.spaceman.terrainGenerator.terrain.terrainNoise;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.*;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commands.terrain.Create.sendSuccess;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class SimplexOctaveNoise extends TerrainNoise implements ConfigurationSerializable {
    
    private double frequency = 5;
    private double amplitude = 0.01;
    private double multitude = 5;
    private double scale = 0.015625;
    private double xScale = 1;
    private double zScale = 1;
    private int xOffset = 0;
    private int zOffset = 0;
    private int octaves = 8;
    
    public SimplexOctaveNoise() {
    
    }
    
    public SimplexOctaveNoise(double frequency, double amplitude, double multitude, double scale, double xScale, double zScale, int xOffset, int zOffset, int octaves) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.multitude = multitude;
        this.scale = scale;
        this.xScale = xScale;
        this.zScale = zScale;
        this.xOffset = xOffset;
        this.zOffset = zOffset;
        this.octaves = octaves;
    }
    
    public static SimplexOctaveNoise deserialize(Map<String, Object> args) {
        return new SimplexOctaveNoise(
                (Double) args.getOrDefault("frequency", 5),
                (Double) args.getOrDefault("amplitude", 0.01),
                (Double) args.getOrDefault("multitude", 5),
                (Double) args.getOrDefault("scale", 0.015625),
                (Double) args.getOrDefault("xScale", 1),
                (Double) args.getOrDefault("zScale", 1),
                (Integer) args.getOrDefault("xOffset", 0),
                (Integer) args.getOrDefault("zOffset", 0),
                (Integer) args.getOrDefault("octaves", 8));
    }
    
    @Override
    public Message dataAsString() {
        Message message = new Message();
        message.addText(textComponent("Frequency: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(frequency), ColorFormatter.varInfoColor).setInsertion(String.valueOf(frequency)));
        message.addText(textComponent("\nAmplitude: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(amplitude), ColorFormatter.varInfoColor).setInsertion(String.valueOf(amplitude)));
        message.addText(textComponent("\nMultitude: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(multitude), ColorFormatter.varInfoColor).setInsertion(String.valueOf(multitude)));
        message.addText(textComponent("\nScale: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(scale), ColorFormatter.varInfoColor).setInsertion(String.valueOf(scale)));
        message.addText(textComponent("\nXScale: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(xScale), ColorFormatter.varInfoColor).setInsertion(String.valueOf(xScale)));
        message.addText(textComponent("\nZScale: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(zScale), ColorFormatter.varInfoColor).setInsertion(String.valueOf(zScale)));
        message.addText(textComponent("\nXOffset: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(xOffset), ColorFormatter.varInfoColor).setInsertion(String.valueOf(xOffset)));
        message.addText(textComponent("\nZOffset: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(zOffset), ColorFormatter.varInfoColor).setInsertion(String.valueOf(zOffset)));
        message.addText(textComponent("\nOctaves: ", ColorFormatter.infoColor));
        message.addText(textComponent(String.valueOf(octaves), ColorFormatter.varInfoColor).setInsertion(String.valueOf(octaves)));
        return message;
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
        return getName() + " is a simple noise generator which can generate a fluent mountain";
    }
    
    @Override
    public Collection<String> tabList(String[] args, Player player) {
        ArrayList<String> newList = new ArrayList<>(Arrays.asList("frequency=", "amplitude=", "multitude=", "scale=", "xScale=", "zScale=", "xOffset=", "zOffset=", "octaves="));
        
        int startArg = args[1].equalsIgnoreCase("set") ? 4 : 3;
        
        for (int i = startArg; i < args.length; i++) {
            for (String data : (ArrayList<String>) newList.clone()) {
                if (args[i].startsWith(data)) {
                    newList.remove(data);
                }
            }
        }
        
        return newList;
    }
    
    @Override
    public void editNoiseSettings(List<String> data, Player player) {
        if (data != null) {
            if (data.size() > 0) {
                for (String arg : data) {
                    if (arg.toLowerCase().startsWith("frequency=")) {
                        try {
                            this.setFrequency(Double.parseDouble(arg.toLowerCase().replace("frequency=", "")));
                            sendSuccess(player, "Successfully set the frequency to ", this.getFrequency());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid frequency number", arg.toLowerCase().replace("frequency=", "")));
                        }
                    }
                    else if (arg.toLowerCase().startsWith("amplitude=")) {
                        try {
                            this.setAmplitude(Double.parseDouble(arg.toLowerCase().replace("amplitude=", "")));
                            sendSuccess(player, "Successfully set the amplitude to ", this.getAmplitude());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid amplitude number", arg.toLowerCase().replace("amplitude=", "")));
                        }
                    }
                    else if (arg.toLowerCase().startsWith("multitude=")) {
                        try {
                            this.setMultitude(Double.parseDouble(arg.toLowerCase().replace("multitude=", "")));
                            sendSuccess(player, "Successfully set the multitude to ", this.getMultitude());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid multitude number", arg.toLowerCase().replace("multitude=", "")));
                        }
                    }
                    else if (arg.toLowerCase().startsWith("scale=")) {
                        try {
                            this.setScale(Double.parseDouble(arg.toLowerCase().replace("scale=", "")));
                            sendSuccess(player, "Successfully set the scale to ", this.getScale());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid scale number", arg.toLowerCase().replace("scale=", "")));
                        }
                    }
                    else if (arg.toLowerCase().startsWith("xScale=".toLowerCase())) {
                        try {
                            this.setXScale(Double.parseDouble(arg.toLowerCase().replace("xScale=".toLowerCase(), "")));
                            sendSuccess(player, "Successfully set the xScale to ", this.getXScale());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid xScale number", arg.toLowerCase().replace("xScale=".toLowerCase(), "")));
                        }
                    }
                    else if (arg.toLowerCase().startsWith("zScale=".toLowerCase())) {
                        try {
                            this.setZScale(Double.parseDouble(arg.toLowerCase().replace("zScale=".toLowerCase(), "")));
                            sendSuccess(player, "Successfully set the zScale to ", this.getZScale());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid zScale number", arg.toLowerCase().replace("zScale=".toLowerCase(), "")));
                        }
                    }
                    else if (arg.toLowerCase().startsWith("xOffset=".toLowerCase())) {
                        try {
                            this.setXOffset(Integer.parseInt(arg.toLowerCase().replace("xOffset=".toLowerCase(), "")));
                            sendSuccess(player, "Successfully set the xOffset to ", this.getXOffset());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid xOffset number", arg.toLowerCase().replace("xOffset=".toLowerCase(), "")));
                        }
                    }
                    else if (arg.toLowerCase().startsWith("zOffset=".toLowerCase())) {
                        try {
                            this.setZOffset(Integer.parseInt(arg.toLowerCase().replace("zOffset=".toLowerCase(), "")));
                            sendSuccess(player, "Successfully set the zOffset to ", this.getZOffset());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid zOffset number", arg.toLowerCase().replace("zOffset=".toLowerCase(), "")));
                        }
                    }
                    else if (arg.toLowerCase().startsWith("octaves=")) {
                        try {
                            this.setOctaves(Integer.parseInt(arg.toLowerCase().replace("octaves=", "")));
                            sendSuccess(player, "Successfully set the octaves to ", this.getOctaves());
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(formatError("%s is not a valid octaves number", arg.toLowerCase().replace("octaves=", "")));
                        }
                    }
                    else {
                        player.sendMessage(formatError("%s is not a valid argument", arg));
                    }
                }
            }
        }
    }
    
    @Override
    public void saveNoise(ConfigurationSection section) {
        section.set("data", this);
    }
    
    @Override
    public TerrainNoise loadNoise(ConfigurationSection section) {
        SimplexOctaveNoise data = (SimplexOctaveNoise) section.get("data");
        if (data == null) {
            return new SimplexOctaveNoise();
        }
        this.frequency = data.frequency;
        this.amplitude = data.amplitude;
        this.multitude = data.multitude;
        this.scale = data.scale;
        this.xScale = data.xScale;
        this.zScale = data.xScale;
        this.octaves = data.octaves;
        return data;
    }
    
    @Override
    public int noise(int x, int z, long seed) {
        SimplexOctaveGenerator gen = new SimplexOctaveGenerator(seed, octaves);
        gen.setScale(scale);
        return (int) (gen.noise((x + xOffset) * xScale, (z + zOffset) * zScale, frequency, amplitude) * multitude);
    }
    
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("frequency", frequency);
        map.put("amplitude", amplitude);
        map.put("multitude", multitude);
        map.put("scale", scale);
        map.put("xScale", xScale);
        map.put("zScale", zScale);
        map.put("octaves", octaves);
        return map;
        
    }
    
    public double getMultitude() {
        return multitude;
    }
    
    public void setMultitude(double multitude) {
        this.multitude = multitude;
    }
    
    public int getOctaves() {
        return octaves;
    }
    
    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }
    
    public double getScale() {
        return scale;
    }
    
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    public double getAmplitude() {
        return amplitude;
    }
    
    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }
    
    public double getFrequency() {
        return frequency;
    }
    
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
    
    public double getZScale() {
        return xScale;
    }
    
    public void setZScale(double zScale) {
        this.zScale = zScale;
    }
    
    public double getXScale() {
        return xScale;
    }
    
    public void setXScale(double xScale) {
        this.xScale = xScale;
    }
    
    public int getXOffset() {
        return xOffset;
    }
    
    public int getZOffset() {
        return zOffset;
    }
    
    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }
    
    public void setZOffset(int zOffset) {
        this.zOffset = zOffset;
    }
}
