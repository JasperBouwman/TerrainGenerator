package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static com.spaceman.terrainGenerator.commands.terrain.Create.setData;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.RED;

public class Edit extends CmdHandler {

    @Override
    public String alias() {
        return "e";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain edit <name> <data...>

        if (args.length > 2) {
            TerrainGenData data = getGen(args[1]);

            if (data != null) {

                setData(data, player, args, 2);
            } else {
                player.sendMessage(ChatColor.RED + "TerrainGenerator " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " does not exist");
            }
        } else {
            if (args.length == 2) {
                Message message = new Message();

                message.addText(textComponent("Hover ", RED));
                HoverEvent hEvent = new HoverEvent(textComponent(""));
                for (String s : Arrays.asList("frequency", "amplitude", "multitude", "scale", "octaves", "height", "seed", "fromTop", "height", "start", "material", "damage", "biome")) {
                    hEvent.addText(textComponent(s, ChatColor.BLUE));
                    hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                }
                hEvent.removeLast();
                message.addText(textComponent("here", DARK_RED, hEvent));
                message.addText(textComponent(" for the available formats, add '=X' behind the data you want to edit and change the X in your new value", RED));

                message.sendMessage(player);
            } else {
                player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.DARK_RED + " /terrain edit <name> <data...>");
            }
        }

    }
}
