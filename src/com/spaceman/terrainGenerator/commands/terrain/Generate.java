package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.commands.TabCompletes.availableGenerators;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Generate extends SubCommand {
    
    public Generate() {
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setCommandName("z", ArgumentType.REQUIRED);
        emptyCommand2.setCommandDescription(TextComponent.textComponent("This command is used to generate terrain with a TerrainGenerator. " +
                "Where the x is the width, and the z the depth. When looking at east the terrain will generate forward and to your right", ColorFormatter.infoColor));
        
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setCommandName("x", ArgumentType.REQUIRED);
        emptyCommand1.addAction(emptyCommand2);
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.addAction(emptyCommand1);
        addAction(emptyCommand);
    }
    
    public static void sendSafeMessage(UUID uuid, String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(message);
        }
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return availableGenerators();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain generate <name> <x> <z>
        if (args.length == 4) {
            
            int x = 0, z = 0;
            boolean b = false;
            
            try {
                x = Integer.parseInt(args[2]);
            } catch (NumberFormatException nfe) {
                player.sendMessage(formatError("%s isn't a valid number", args[2]));
                b = true;
            }
            try {
                z = Integer.parseInt(args[3]);
            } catch (NumberFormatException nfe) {
                player.sendMessage(formatError("%s isn't a valid number", args[3]));
                b = true;
            }
            if (b) {
                return;
            }
            
            if (getGen(args[1]) != null) {
                
                int finalX = x;
                int finalZ = z;
                UUID uuid = player.getUniqueId();
                
                new Thread(() -> {
                    player.sendMessage(formatInfo("Generating..."));
                    long time = System.currentTimeMillis();
                    try {
                        TerrainGenerator.generate(args[1], finalX, finalZ, player.getLocation());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        sendSafeMessage(uuid, formatError("An unexpected error occurred, error message: %s", exception.getMessage()));
                    }
                    sendSafeMessage(uuid, formatInfo("Generated, time taken: %s milliseconds", String.valueOf((System.currentTimeMillis() - time))));
                    long newTime = System.currentTimeMillis();
                    sendSafeMessage(uuid, formatInfo("Loading terrain..."));
                    Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () ->
                            sendSafeMessage(uuid, formatInfo("Terrain loaded, time taken: %s milliseconds\nTotal time taken: %s milliseconds",
                                    String.valueOf((System.currentTimeMillis() - newTime)), String.valueOf((System.currentTimeMillis() - time))))
                    );
                }).start();
                
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[1]));
            }
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain generate <name> <width(x)> <height(z)>"));
        }
    }
}
