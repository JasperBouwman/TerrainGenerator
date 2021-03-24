package com.spaceman.terrainGenerator.commands.terrain.gens;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Remove extends SubCommand {

    public Remove() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(textComponent("This command is used to remove a TerrainGenerator from the first given TerrainGenerator", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> getGen(args[2]).getGenerators());
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return ownGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain gens remove <name> <name>

        if (args.length == 4) {
            TerrainGenData data = getGen(args[2]);

            if (data != null) {
                if (data.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }

                TerrainGenData addData = getGen(args[3]);

                if (addData != null) {
                    if (data.getGenerators().contains(addData.getName())) {
                        data.removeGenerator(addData.getName());
                        Message message = new Message();
                        message.addText(textComponent("TerrainGenerator ", ColorFormatter.successColor));
                        message.addText(textComponent(addData.getName(), ColorFormatter.varSuccessColor, addData.toHoverEvent()));
                        message.addText(textComponent(" successfully removed from TerrainGenerator ", ColorFormatter.successColor));
                        message.addText(textComponent(data.getName(), ColorFormatter.varSuccessColor, data.toHoverEvent()));
                        message.sendMessage(player);
                    } else {
                        Message message = new Message();
                        message.addText(textComponent("TerrainGenerator ", ColorFormatter.errorColor));
                        message.addText(textComponent(addData.getName(), ColorFormatter.varErrorColor, addData.toHoverEvent()));
                        message.addText(textComponent(" is not in TerrainGenerator ", ColorFormatter.errorColor));
                        message.addText(textComponent(data.getName(), ColorFormatter.varErrorColor, data.toHoverEvent()));
                        message.sendMessage(player);
                    }
                } else {
                    player.sendMessage(formatError("TerrainGenerator %s does not exist", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain removeGen <name> <name>"));
        }
    }
}
