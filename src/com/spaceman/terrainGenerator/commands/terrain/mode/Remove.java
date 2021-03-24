package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Remove extends SubCommand {

    public Remove() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("TerrainMode name", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(TextComponent.textComponent("This command is used to remove the TerrainMode from the given TerrainGenerator", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> {
            //terrain mode remove <name>
            ArrayList<String> list;
            TerrainGenData genData = getGen(args[2]);
            list = genData.getModes().stream().map(TerrainMode::getModeName).collect(Collectors.toCollection(ArrayList::new));
            return list;
        });
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
        //terrain mode remove <name> <TerrainMode name>

        if (args.length == 4) {

            TerrainGenData data = getGen(args[2]);

            if (data != null) {
                if (data.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }

                if (data.removeMode(args[3])) {
                    player.sendMessage(formatSuccess("Successfully removed the TerrainMode %s", args[3]));
                } else {
                    player.sendMessage(formatError("Could not find a TerrainMode called %s, therefore it could not be removed", args[3]));
                }

            } else {
                player.sendMessage(formatError("TerrainGenerator %s was not found", args[2]));
            }

        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain mode <name> <TerrainMode name>"));
        }
    }
}
