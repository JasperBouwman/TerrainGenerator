package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeInverse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.infoColor;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class GetData extends SubCommand {

    public GetData() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("TerrainMode name", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(textComponent("This command is used to get the data of the given TerrainMode", infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            //terrain mode getData <name> ...
            return getGen(args[2]).getModes().stream().map(TerrainMode::getModeName).collect(Collectors.toList());
        });
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode getData <name> <TerrainMode name>

        if (args.length == 4) {

            TerrainGenData genData = getGen(args[2]);

            if (genData != null) {

                TerrainMode mode = genData.getMode(args[3]);

                if (mode != null) {
                    Message message = new Message();

                    message.addText(textComponent("TerrainMode data of ", infoColor));
                    message.addText(textComponent(mode.getModeName(), ColorFormatter.varInfoColor));
                    message.addText(textComponent(" is: \n", infoColor));
                    if (mode instanceof TerrainModeInverse) {
                        message.addText(textComponent("Inversed: ", ColorFormatter.varInfoColor));
                        if (((TerrainModeInverse) mode).isInverse()) {
                            message.addText(textComponent("True\n", ChatColor.GREEN));
                        } else {
                            message.addText(textComponent("False\n", ChatColor.RED));
                        }
                    }

                    message.addMessage(mode.dataAsStringWithHover());
                    message.sendMessage(player);
                } else {
                    player.sendMessage(formatError("TerrainMode %s was not found", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }


        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain mode getData <name> <TerrainMode name>"));
        }

    }
}
