package com.spaceman.terrainGenerator.commands.terrain.gens;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.TabCompletes;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class List extends SubCommand {
    
    public List() {
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(textComponent("This command is used to list all TerrainGenerators in the given TerrainGenerator", ColorFormatter.infoColor));
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return TabCompletes.availableGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain gens list <name>

        if (args.length == 3) {
            TerrainGenData data = getGen(args[2]);

            if (data != null) {

                Message message = new Message();

                message.addText(textComponent("Used TerrainGenerators in ", ColorFormatter.infoColor));
                message.addText(textComponent(data.getName(), ColorFormatter.varInfoColor, data.toHoverEvent()));
                message.addText(textComponent(": ", ColorFormatter.infoColor));
                message.addText("");
                boolean color = true;
                for (String s : data.getGenerators()) {
                    if (color) {
                        message.addText(textComponent(s, ColorFormatter.varInfoColor, TerrainGenData.toHoverEvent(s)));
                    } else {
                        message.addText(textComponent(s, ColorFormatter.varInfo2Color, TerrainGenData.toHoverEvent(s)));
                    }
                    color = !color;
                    message.addText(textComponent(", ", ColorFormatter.infoColor));
                }
                message.removeLast();

                message.sendMessage(player);
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain gens list <name>"));
        }
    }
}
