package com.spaceman.terrainGenerator.commands.terrain.world;

import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

import static com.spaceman.terrainGenerator.ColorFormatter.*;

public class Regenerate extends SubCommand {
    
    public Regenerate() {
        EmptyCommand emptyCommand3 = new EmptyCommand();
        emptyCommand3.setCommandName("z2", ArgumentType.REQUIRED);
        emptyCommand3.setCommandDescription(TextComponent.textComponent("This command is used to regenerate the selected chunks the same way as ", infoColor),
                TextComponent.textComponent("/terrain world regenerate", varInfoColor));
        emptyCommand3.setLinked();
        
        EmptyCommand emptyCommand2 = new EmptyCommand();
        emptyCommand2.setTabRunnable((args, player) -> Arrays.asList("~", String.valueOf(player.getLocation().getChunk().getZ())));
        emptyCommand2.setCommandName("x2", ArgumentType.REQUIRED);
        emptyCommand2.setLinked();
        emptyCommand2.addAction(emptyCommand3);
        
        EmptyCommand emptyCommand1 = new EmptyCommand();
        emptyCommand1.setTabRunnable((args, player) -> Arrays.asList("~", String.valueOf(player.getLocation().getChunk().getX())));
        emptyCommand1.addAction(emptyCommand2);
        emptyCommand1.setCommandName("z1", ArgumentType.REQUIRED);
        emptyCommand1.setLinked();
        
        EmptyCommand emptyCommand = new EmptyCommand();
        emptyCommand.setTabRunnable((args, player) -> Arrays.asList("~", String.valueOf(player.getLocation().getChunk().getZ())));
        emptyCommand.addAction(emptyCommand1);
        emptyCommand.setCommandName("x1", ArgumentType.REQUIRED);
        emptyCommand.setLinked();
        
        addAction(emptyCommand);
    }
    
    @Override
    public Message getCommandDescription() {
        return new Message(TextComponent.textComponent("This command is used to regenerate the chunk which you are in. " +
                "It clears the chunk and generate the new chunk using the ", infoColor),
                TextComponent.textComponent("/terrain generate", varInfoColor),
                TextComponent.textComponent(" command", infoColor));
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return Arrays.asList("~", String.valueOf(player.getLocation().getChunk().getX()));
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain world regenerate
        //terrain world regenerate <x1> <z1> <x2> <z2>
        
        if (args.length == 2) {
            regenerate(player, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        } else if (args.length == 6) {
            int x1 = 0;
            int z1 = 0;
            int x2 = 0;
            int z2 = 0;
            boolean b = false;
            try {
                x1 = Integer.parseInt(args[2]);
            } catch (NumberFormatException nfe) {
                if (args[2].startsWith("~")) {
                    x1 = player.getLocation().getChunk().getX();
                    if (!args[2].equals("~")) {
                        String tmp = args[2].split("~")[1];
                        try {
                            x1 += Integer.parseInt(tmp);
                        } catch (NumberFormatException nfe1) {
                            player.sendMessage(formatError("%s isn't a number", tmp));
                            b = true;
                        }
                    }
                } else {
                    player.sendMessage(formatError("%s isn't a number", args[2]));
                    b = true;
                }
            }
            try {
                z1 = Integer.parseInt(args[3]);
            } catch (NumberFormatException nfe) {
                if (args[3].startsWith("~")) {
                    z1 = player.getLocation().getChunk().getZ();
                    if (!args[3].equals("~")) {
                        String tmp = args[3].split("~")[1];
                        try {
                            z1 += Integer.parseInt(tmp);
                        } catch (NumberFormatException nfe1) {
                            player.sendMessage(formatError("%s isn't a number", tmp));
                            b = true;
                        }
                    }
                } else {
                    player.sendMessage(formatError("%s isn't a number", args[3]));
                    b = true;
                }
            }
            try {
                x2 = Integer.parseInt(args[4]);
            } catch (NumberFormatException nfe) {
                if (args[4].startsWith("~")) {
                    x2 = player.getLocation().getChunk().getX();
                    if (!args[4].equals("~")) {
                        String tmp = args[4].split("~")[1];
                        try {
                            x2 += Integer.parseInt(tmp);
                        } catch (NumberFormatException nfe1) {
                            player.sendMessage(formatError("%s isn't a number", tmp));
                            b = true;
                        }
                    }
                } else {
                    player.sendMessage(formatError("%s isn't a number", args[4]));
                    b = true;
                }
            }
            try {
                z2 = Integer.parseInt(args[5]);
            } catch (NumberFormatException nfe) {
                if (args[5].startsWith("~")) {
                    z2 = player.getLocation().getChunk().getZ();
                    if (!args[5].equals("~")) {
                        String tmp = args[5].split("~")[1];
                        try {
                            z2 += Integer.parseInt(tmp);
                        } catch (NumberFormatException nfe1) {
                            player.sendMessage(formatError("%s isn't a number", tmp));
                            b = true;
                        }
                    }
                } else {
                    player.sendMessage(formatError("%s isn't a number", args[5]));
                    b = true;
                }
            }
            if (b) return;
            
            for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                for (int j = Math.min(z1, z2); j <= Math.max(z1, z2); j++) {
                    regenerate(player, i, j);
                }
            }
            
        } else {
            player.sendMessage(formatError("Usage: %s", "/terrain world regenerate [<x1> <z1> <x2> <z2>]"));
        }
    }
    
    private void regenerate(Player player, int x, int z) {
        if (player.getWorld().getGenerator() instanceof WorldGenerator) {
            WorldGenerator gen = (WorldGenerator) player.getWorld().getGenerator();
            long time = System.currentTimeMillis();
//            gen.addChunk(x, z);
//            player.getWorld().regenerateChunk(x, z);
//            player.getWorld().unloadChunkRequest(x, z);
            for (int i = x << 4; i < (x << 4) + 16; i++) {
                for (int j = z << 4; j < (z << 4) + 16; j++) {
                    for (int k = 0; k < player.getWorld().getMaxHeight(); k++) {
                        player.getWorld().getBlockAt(i, k, j).setType(Material.AIR);
                    }
                }
            }
            TerrainGenerator.generate(gen.getTerrainGeneratorName(), 16, 16, new Location(player.getWorld(), x << 4, 0, z << 4));
            player.sendMessage(formatInfo("Generated chunk (%s, %s) in %sms", String.valueOf(x), String.valueOf(z), String.valueOf((System.currentTimeMillis() - time))));
        } else {
            player.sendMessage(formatError("The world you are in must be a TerrainWorld"));
        }
    }
}
