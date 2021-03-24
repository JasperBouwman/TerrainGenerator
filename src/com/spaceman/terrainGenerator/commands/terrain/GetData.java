package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.commands.terrain.SafeToEdit.SafeToEditClass.safeToEdit;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class GetData extends SubCommand {
    
    public GetData() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(textComponent("This command is used to get the data from the given TerrainGenerator", infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain getData <name>
        
        if (args.length == 2) {
            TerrainGenData data = getGen(args[1]);
            
            if (data != null) {
                
                Message message = new Message();
                
                //TerrainGenData properties
                message.addText(textComponent("TerrainGenerator data of ", infoColor));
                message.addText(textComponent(data.getName(), varInfoColor));
                message.addText(textComponent(": ", infoColor));
                message.addText(textComponent("\nCreator: ", infoColor));
                message.addText(textComponent(data.getCreator() + "", varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nFastRender: ", infoColor));
                message.addText(textComponent(data.isFastRender() + "", varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nHeight: ", infoColor));
                message.addText(textComponent(data.getHeight() + "", varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nSeed: ", infoColor));
                message.addText(textComponent(data.getSeed() + "", varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nFromTop: ", infoColor));
                message.addText(textComponent(data.getFromTop() + "", varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nStart: ", infoColor));
                message.addText(textComponent(data.getStart(), varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nMaterial: ", infoColor));
                message.addText(textComponent(data.getTerrainBlockData().getMaterial().name(), varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nDirection: ", infoColor));
                message.addText(textComponent(data.getTerrainBlockData().getBlockFace().name(), varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nWaterLogged: ", infoColor));
                message.addText(textComponent(data.getTerrainBlockData().isWaterLogged() + "", varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nMax: ", infoColor));
                message.addText(textComponent(data.getMax(), varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nMin: ", infoColor));
                message.addText(textComponent(data.getMin(), varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nBiome: ", infoColor));
                message.addText(textComponent(data.getBiome() + "", varInfoColor).setTextAsInsertion());
                message.addText(textComponent("\nNoiseGenerator: ", infoColor));
                message.addMessage(data.getTerrainNoise().dataAsStringWithHover());
                message.addText(textComponent("\nSafeToEdit: ", infoColor));
                SafeToEdit.SafeToEditClass safeToEdit = safeToEdit(data.getName());
                if (safeToEdit.isSafeToEdit()) {
                    message.addText(textComponent("yes", ChatColor.GREEN, "yes"));
                } else {
                    message.addText(textComponent("no: ", ChatColor.RED, "no"));
                    for (String world : safeToEdit.getWorlds()) {
                        message.addText(textComponent(world, ColorFormatter.varErrorColor).setInsertion(world));
                        message.addText(textComponent(", ", ColorFormatter.errorColor));
                    }
                    message.removeLast();
                }
                message.addText(textComponent("\nGenerators: ", infoColor));
                message.addText("");
                for (String gen : data.getGenerators()) {
                    message.addText(textComponent(gen, varInfoColor).setTextAsInsertion());
                    message.addText(textComponent(", ", infoColor));
                }
                message.removeLast();
                message.addText(textComponent("\nModes: ", infoColor));
                message.addText("");
                for (TerrainMode mode : data.getModes()) {
                    
                    HoverEvent hEvent = HoverEvent.hoverEvent(textComponent("Data: ", infoColor));
                    hEvent.addMessage(mode.dataAsString());
                    
                    message.addText(textComponent(mode.getModeName(), varInfoColor, hEvent));
                    message.addText(textComponent(", ", infoColor));
                }
                message.removeLast();
                
                HoverEvent biomeHover = new HoverEvent();
                biomeHover.addText(textComponent("Section size: ", infoColor));
                biomeHover.addText(textComponent(String.valueOf(data.getBiomeSettings().getSectionSize()), varInfoColor));
                biomeHover.addText(textComponent("\nBiome scale: ", infoColor));
                biomeHover.addText(textComponent(String.valueOf(data.getBiomeSettings().getBiomeScale()), varInfoColor));
                biomeHover.addText(textComponent("\nBiome seed: ", infoColor));
                biomeHover.addText(textComponent(String.valueOf(data.getBiomeSettings().getBiomeSeed()), varInfoColor));
                message.addText(textComponent("\nBiomeSettings: ", infoColor, biomeHover));
                for (String biome : data.getBiomeSettings().getBiomes()) {
                    HoverEvent hEvent = new HoverEvent();
                    hEvent.addText(textComponent("Weight: ", infoColor));
                    hEvent.addText(textComponent(String.valueOf(data.getBiomeSettings().getBiomeWeight(biome)), varInfoColor));
                    hEvent.addText(textComponent("\nSize: ", infoColor));
                    hEvent.addText(textComponent(String.valueOf(data.getBiomeSettings().getBiomeSize(biome)), varInfoColor));
                    message.addText(textComponent(biome, varInfoColor, hEvent).setTextAsInsertion());
                    message.addText(", ", infoColor);
                }
                message.removeLast();
                
                message.sendMessage(player);
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[1]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain getData <name>"));
        }
        
    }
}
