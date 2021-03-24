package com.spaceman.terrainGenerator.commands.terrain.noise;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.terrainNoise.TerrainNoise;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class Description extends SubCommand {
    
    public Description() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("noise", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(textComponent("This command is used to get information and default values of the given NoiseGenerator", ColorFormatter.infoColor));
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return TerrainNoise.getNoises();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain noise description <noise>

        if (args.length == 3) {
            TerrainNoise noise = TerrainNoise.getNewNoise(args[2]);
            if (noise != null) {
                Message message = new Message();

                message.addText(textComponent("Description of ", ColorFormatter.infoColor));
                message.addText(textComponent(noise.getName(), ColorFormatter.varInfoColor));
                message.addText(textComponent(": ", ColorFormatter.infoColor));
                message.addText(textComponent(ColorFormatter.varInfoColor + noise.getDescription() + ColorFormatter.varInfoColor, ColorFormatter.varInfoColor));
                message.addText(textComponent("\nDefault data:\n", ColorFormatter.infoColor));
                message.addMessage(noise.dataAsString());

                message.sendMessage(player);
            } else {
                player.sendMessage(formatError("TerrainNoise %s was not found", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain noise description <noise>"));
        }
    }
}
