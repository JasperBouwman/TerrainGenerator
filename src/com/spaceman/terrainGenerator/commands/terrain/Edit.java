package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.commands.TabCompletes;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.dataTabList;
import static com.spaceman.terrainGenerator.commands.terrain.Create.setData;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Edit extends SubCommand {

    public Edit() {
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> dataTabList(2, args));
        emptyCommand1.setCommandName("data...", ArgumentType.REQUIRED);
        emptyCommand1.setCommandDescription(
                textComponent("This command is used to edit an existing ", infoColor),
                textComponent("internal", varInfoColor),
                textComponent(" TerrainGenerator, if you use ", infoColor),
                textComponent("/terrain edit", varInfoColor),
                textComponent(" you get all the available data to edit", infoColor));
        emptyCommand1.setLooped(true);
    
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable(emptyCommand1.getTabRunnable());
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }

    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return TabCompletes.ownGenerators();
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain edit <name> <data...>

        if (args.length > 2) {
            TerrainGenData data = getGen(args[1]);

            if (data != null) {
                if (data.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                setData(data, player, args, 2);
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[1]));
            }
        } else {
            if (args.length == 1) {
                Message message = new Message();

                message.addText(textComponent("Hover ", ColorFormatter.errorColor));
                HoverEvent hEvent = new HoverEvent(textComponent(""));
                for (String s : Arrays.asList("height", "seed", "fromTop", "start", "material", "direction", "biome", "waterLogged",
                        "max", "min", "fastRender")) {
                    hEvent.addText(textComponent(s, varInfoColor));
                    hEvent.addText(textComponent(", ", infoColor));
                }
                hEvent.removeLast();
                message.addText(textComponent("here", ColorFormatter.varErrorColor, hEvent));
                message.addText(textComponent(" for the available formats, add '=X' behind the data you want to edit and change the X in your new value", ColorFormatter.errorColor));

                message.sendMessage(player);
            } else {
                player.sendMessage(formatError("Usage: %s", "/terrain edit <name> <data...>"));
            }
        }

    }
}
