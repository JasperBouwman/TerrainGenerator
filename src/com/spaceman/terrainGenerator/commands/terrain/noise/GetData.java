package com.spaceman.terrainGenerator.commands.terrain.noise;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class GetData extends SubCommand {

    public GetData() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(textComponent("This command is used to get the data of the given NoiseGenerator from the given TerrainGenerator", ColorFormatter.infoColor));
        addAction(emptyCommand);
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return ownGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain noise get <name>

        if (args.length == 3) {
            TerrainGenData genData = getGen(args[2]);
            if (genData != null) {
                TerrainNoise terrainNoise = genData.getTerrainNoise();
                Message message = new Message();
                message.addText(textComponent("TerrainNoise data of ", ColorFormatter.infoColor));
                message.addText(textComponent(terrainNoise.getName(), ColorFormatter.varInfoColor));
                message.addText(textComponent(" in ", ColorFormatter.infoColor));
                message.addText(textComponent(genData.getName(), ColorFormatter.varInfoColor, genData.toHoverEvent()));
                message.addText(textComponent(" is:\n", ColorFormatter.infoColor));
                
                message.addMessage(terrainNoise.dataAsString());
                message.sendMessage(player);
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain noise getData <name>"));
        }
    }
}
