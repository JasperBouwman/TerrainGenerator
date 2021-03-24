package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainModeWaterLoggable;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.terrainGenData;

public class WaterLog extends SubCommand {

    public WaterLog() {
        EmptyCommand emptyCommand3 = new EmptyCommand();
        emptyCommand3.setTabRunnable((args, player) -> {
            //terrain mode edit <name> <TerrainMode name> <set> <data...>
        
            TerrainGenData genData = getGen(args[2]);
            if (genData.getMode(args[3]) instanceof TerrainModeWaterLoggable) {
                if (args[4].equalsIgnoreCase("set")) {
                    return ((TerrainModeWaterLoggable) genData.getMode(args[3])).tabListWaterLog(args, player);
                }
            }
            return new ArrayList<>();
        });
        emptyCommand3.setLooped(true);
        emptyCommand3.setCommandName("data...", ArgumentType.REQUIRED);
        emptyCommand3.setCommandDescription(TextComponent.textComponent("This command is used to set different objects in TerrainModes water logged. " +
                "This only works if the given TerrainMode is WaterLoggable, you can check this using: ", infoColor),
                TextComponent.textComponent("/terrain mode description <TerrainMode name>", varInfoColor));
        
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setTabRunnable(emptyCommand3.getTabRunnable());
        emptyCommand2.setCommandName("set", ArgumentType.FIXED);
        emptyCommand2.addAction(emptyCommand3);

        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> {
            //terrain mode edit <name> <TerrainMode name> <set>
            TerrainGenData genData = getGen(args[2]);
            if (genData.getMode(args[3]) instanceof TerrainModeWaterLoggable) {
                return Collections.singletonList("set");
            }
            return new ArrayList<>();
        });
        emptyCommand1.addAction(emptyCommand2);
        emptyCommand1.setCommandName("TerrainMode name", ArgumentType.REQUIRED);

        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) ->
                getGen(args[2]).getModes().stream().filter(mode -> mode instanceof TerrainModeWaterLoggable).map(TerrainMode::getModeName).collect(Collectors.toCollection(ArrayList::new)));
        emptyCommand.addAction(emptyCommand1);
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return ownGenerators().stream().filter(gen -> terrainGenData.get(gen).getModes().stream().anyMatch(mode -> mode instanceof TerrainModeWaterLoggable))
                .collect(Collectors.toList());
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain mode waterLogged <name> <TerrainMode name> set <data...> {only for WaterLoggable TerrainModes}
        if (args.length > 5) {
            TerrainGenData data = getGen(args[2]);

            if (data != null) {
                if (data.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainMode mode = data.getMode(args[3]);
                if (mode != null) {
                    if (mode instanceof TerrainModeWaterLoggable) {
                        ((TerrainModeWaterLoggable) mode).setWaterLogged(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                    } else {
                        player.sendMessage(formatError("This TerrainMode is not able to be waterLogged"));
                    }
                } else {
                    player.sendMessage(formatError("Could not find TerrainMode %s", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s was not found", args[2]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain mode waterLogged <name> <TerrainMode name> set <data...>"));
        }
    }
}
