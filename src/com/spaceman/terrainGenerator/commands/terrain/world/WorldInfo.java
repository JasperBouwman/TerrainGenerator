package com.spaceman.terrainGenerator.commands.terrain.world;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.ColorFormatter.formatInfo;
import static com.spaceman.terrainGenerator.ColorFormatter.infoColor;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.generators.WorldGenerator.worldPrefix;

public class WorldInfo extends SubCommand {
    
    @Override
    public Message getCommandDescription() {
        return new Message(textComponent("This command is used the get some information about the world you are in, " +
                "like the name and if its a TerrainWorld the TerrainGenerator", infoColor));
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain world worldInfo
        
        World world = player.getWorld();
        if (world.getGenerator() instanceof WorldGenerator) {
            Message message = new Message();
    
            message.addText(textComponent("You are in the TerrainWorld: ", infoColor));
            message.addText(textComponent(world.getName().replace(worldPrefix, ""), ColorFormatter.varInfoColor));
            message.addText(textComponent(". This TerrainWorld uses the TerrainGenerator: ", ColorFormatter.infoColor));
            
            String genName = ((WorldGenerator) world.getGenerator()).getTerrainGeneratorName();
            message.addText(textComponent(genName, ColorFormatter.varInfoColor, TerrainGenData.toHoverEvent(genName)));
            
            message.sendMessage(player);
        } else {
            player.sendMessage(formatInfo("You are in the world: %s, this is not a TerrainWorld", world.getName()));
        }
    }
}
