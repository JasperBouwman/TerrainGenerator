package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.Main;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.ColorFormatter.formatSuccess;
import static com.spaceman.terrainGenerator.commands.TabCompletes.dataTabList;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.worldSeedValue;
import static com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode.containsSpecialCharacter;

public class Create extends SubCommand {

    public Create() {
        EmptyCommand emptyCommand1 = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return argument;
            }
        };
        emptyCommand1.setTabRunnable((args, player) -> dataTabList(2, args));
        emptyCommand1.setLooped(true);
        emptyCommand1.setCommandName("data...", ArgumentType.OPTIONAL);
        emptyCommand1.setCommandDescription(new Message(textComponent("When data is given, the given data will be set to that newly created TerrainGenerator. " +
                "When its not given, its going to be its default value", ColorFormatter.infoColor)));
    
        EmptyCommand emptyCommand = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return argument;
            }
        };
        emptyCommand.setTabRunnable(emptyCommand1.getTabRunnable());
        emptyCommand.setCommandName("name", ArgumentType.REQUIRED);
        emptyCommand.setCommandDescription(new Message(textComponent("This command is used to create new TerrainGenerators, " +
                "you can't use any special characters in the name", ColorFormatter.infoColor)));
        emptyCommand.addAction(emptyCommand1);
        
        addAction(emptyCommand);
    }

    public static void sendSuccess(Player player, String stringMessage, Object data) {
        Message message = new Message();
        message.addText(textComponent(stringMessage, ColorFormatter.successColor));
        message.addText(textComponent(String.valueOf(data), ColorFormatter.varSuccessColor).setInsertion(String.valueOf(data)));
        message.sendMessage(player);
    }

    static void setData(TerrainGenData data, Player player, String[] args, int startSubList) {
        for (String arg : Arrays.asList(args).subList(startSubList, args.length)) {

            //TerrainGenData properties
            if (arg.toLowerCase().startsWith("height=")) {
                try {
                    data.setHeight(Integer.parseInt(arg.toLowerCase().replace("height=", "")));
                    sendSuccess(player, "Successfully set the height to ", data.getHeight());
                } catch (NumberFormatException nfe) {
                    player.sendMessage(formatError("%s is not a valid height number", arg.toLowerCase().replace("height=", "")));
                }
            }
            else if (arg.toLowerCase().startsWith("seed=")) {
                if (arg.toLowerCase().replace("seed=", "").equalsIgnoreCase("random")) {
                    data.setSeed(new Random().nextLong());
                    sendSuccess(player, "Successfully set the seed to ", data.getSeed());
                } else if (arg.toLowerCase().replace("seed=", "").equalsIgnoreCase("world")) {
                    data.setSeed(worldSeedValue);
                    sendSuccess(player, "Successfully set the seed to ", "world");
                } else {
                    try {
                        long seed = Long.parseLong(arg.toLowerCase().replace("seed=", ""));
                        if (seed >= 9223336036854775807L) {// Long.MAX_VALUE - 6.000.000L*5.999.999L+6.000.000L = 9223336036854775807L
                            player.sendMessage(formatError("%s is not a valid seed number", arg.toLowerCase().replace("seed=", "")));
                        } else {
                            data.setSeed(seed);
                            sendSuccess(player, "Successfully set the seed to ", data.getSeed());
                        }
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(formatError("%s is not a valid seed number", arg.toLowerCase().replace("seed=", "")));
                    }
                }
            }
            else if (arg.toLowerCase().startsWith("fromTop=".toLowerCase())) {
                data.setFromTop(Boolean.parseBoolean(arg.toLowerCase().replace("fromTop=".toLowerCase(), "")));
                sendSuccess(player, "Successfully set the fromTop to ", data.getFromTop());
            }
            else if (arg.toLowerCase().startsWith("fastRender=".toLowerCase())) {
                data.setFastRender(Boolean.parseBoolean(arg.toLowerCase().replace("fastRender=".toLowerCase(), "")));
                sendSuccess(player, "Successfully set the fastRender to ", data.getFromTop());
            }
            else if (arg.toLowerCase().startsWith("start=")) {
                try {
                    data.setStart(Integer.parseInt(arg.toLowerCase().replaceAll("(?i:start=)", "")));
                    sendSuccess(player, "Successfully set the start to ", data.getStart());
                } catch (NumberFormatException ignore) {
                    if (getGen(arg.replaceAll("(?i:start=)", "")) == null) {
                        player.sendMessage(formatError("TerrainGenerator %s does not exist", arg.replaceAll("(?i:start=)", "") ));
                    } else {
                        data.setStart(arg.replaceAll("(?i:start=)", ""));
                        sendSuccess(player, "Successfully set the start to ", data.getStart());
                    }
                }
            }
            else if (arg.toLowerCase().startsWith("min=")) {
                try {
                    data.setMin(Integer.parseInt(arg.replaceAll("(?i:min=)", "")));
                    sendSuccess(player, "Successfully set the min to ", data.getMin());
                } catch (NumberFormatException ignore) {
                    if (getGen(arg.replaceAll("(?i:min=)", "")) == null) {
                        player.sendMessage(formatError("TerrainGenerator %s does not exist", arg.replaceAll("(?i:min=)", "")));
                    } else {
                        data.setMin(arg.replaceAll("(?i:min=)", ""));
                        sendSuccess(player, "Successfully set the min to ", data.getMin());
                    }
                }
            }
            else if (arg.toLowerCase().startsWith("max=")) {
                try {
                    data.setMax(Integer.parseInt(arg.replaceAll("(?i:max=)", "")));
                    sendSuccess(player, "Successfully set the max to ", data.getMax());
                } catch (NumberFormatException ignore) {
                    if (getGen(arg.replaceAll("(?i:max=)", "")) == null) {
                        player.sendMessage(formatError("TerrainGenerator %s does not exist", arg.replaceAll("(?i:max=)", "")));
                    } else {
                        data.setMax(arg.replaceAll("(?i:max=)", ""));
                        sendSuccess(player, "Successfully set the max to ", data.getMax());
                    }
                }
            }
            else if (arg.toLowerCase().startsWith("material=")) {
                TerrainBlockData is = data.getTerrainBlockData();
                String material = arg.toLowerCase().replace("material=", "").toUpperCase();
                Material m = Material.getMaterial(material);
                if (m == null) {
                    player.sendMessage(formatError("Material %s was not found", material));
                    continue;
                }
                if (!m.isBlock()) {
                    player.sendMessage(formatError("Material %s is not a block", m.name()));
                    continue;
                }
                is.setMaterial(m);
                sendSuccess(player, "Successfully set the material to ", data.getTerrainBlockData().getMaterial().name());
            }
            else if (arg.toLowerCase().startsWith("direction=")) {
                TerrainBlockData is = data.getTerrainBlockData();
                String direction = arg.toLowerCase().replace("direction=", "").toUpperCase();
                try {
                    BlockFace face = BlockFace.valueOf(direction);
                    is.setBlockFace(face);
                    sendSuccess(player, "Successfully set the direction to ", data.getTerrainBlockData().getBlockFace().name());
                } catch (IllegalArgumentException iae) {
                    player.sendMessage(formatError("Direction %s was not found", direction));
                }
            }
            else if (arg.toLowerCase().startsWith("waterLogged=".toLowerCase())) {
                TerrainBlockData is = data.getTerrainBlockData();
                is.setWaterLogged(Main.parseBoolean(arg.toLowerCase().replace("waterLogged=".toLowerCase(), "")));
                sendSuccess(player, "Successfully set waterLogged to ", data.getTerrainBlockData().isWaterLogged());
            }
            else if (arg.toLowerCase().startsWith("biome=")) {
                if (arg.equalsIgnoreCase("biome=null")) {
                    data.setBiome(null);
                    sendSuccess(player, "Successfully set the biome to ", "null");
                } else {
                    String biome = arg.toLowerCase().replace("biome=", "").toUpperCase();
                    try {
                        data.setBiome(Biome.valueOf(biome));
                        sendSuccess(player, "Successfully set the biome to ", data.getBiome().name());
                    } catch (IllegalArgumentException iae) {
                        player.sendMessage(formatError("%s is not a existing biome", biome));
                    }
                }
            }
            else {
                Message message = new Message();

                message.addText(textComponent(arg, ColorFormatter.varErrorColor));
                message.addText(textComponent(" is not in a valid format, hover ", ColorFormatter.errorColor));
                HoverEvent hEvent = new HoverEvent(textComponent(""));
                //TerrainGenData properties
                for (String s : Arrays.asList("height", "seed", "fromTop", "start", "material", "direction", "biome", "waterLogged",
                        "max", "min", "fastRender")) {
                    hEvent.addText(textComponent(s, ColorFormatter.varInfoColor));
                    hEvent.addText(textComponent(", ", ColorFormatter.infoColor));
                }
                hEvent.removeLast();
                message.addText(textComponent("here", ColorFormatter.varErrorColor, hEvent));
                message.addText(textComponent(" for the available formats", ColorFormatter.errorColor));

                message.sendMessage(player);
            }
        }
    }
    
    @Override
    public Collection<String> tabList(Player player, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain create <name> [data...]

        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("null")) {
                player.sendMessage(formatError("Can't use the name %s", "null"));
                return;
            }
            if (containsSpecialCharacter(args[1])) {
                player.sendMessage(formatError("Name can't contain any special characters"));
                return;
            }
            

            TerrainGenData data = terrainGenData(args[1], false);

            if (data != null) {
                player.sendMessage(formatSuccess("TerrainGenerator successfully created"));
                data.setCreator(player.getName());
                
                
                if (args.length > 2) {
                    setData(data, player, args, 2);
                }

            } else {
                player.sendMessage(formatError("TerrainGenerator already exist"));
            }
        } else {
            player.sendMessage(formatError("Usage: %s","/terrain create <name> [data...]"));
        }
    }
}
