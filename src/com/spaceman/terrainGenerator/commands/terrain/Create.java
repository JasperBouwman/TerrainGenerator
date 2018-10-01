package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.worldSeedValue;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;
import static org.bukkit.ChatColor.*;

public class Create extends CmdHandler {

    static void setData(TerrainGenData data, Player player, String[] args, int startSubList) {
        for (String arg : Arrays.asList(args).subList(startSubList, args.length)) {

            String d = arg.toLowerCase();

            //TerrainGenData properties
            if (d.startsWith("frequency=")) {
                try {
                    data.setFrequency(Double.parseDouble(d.replace("frequency=", "")));
                } catch (NumberFormatException nfe) {
                    player.sendMessage(DARK_RED + d.replace("frequency=", "") + RED + " is not a valid frequency number");
                    continue;
                }
                player.sendMessage(GREEN + "Successfully set the frequency to " + DARK_GREEN + d.replace("frequency=", ""));
            } else if (d.startsWith("amplitude=")) {
                try {
                    data.setAmplitude(Double.parseDouble(d.replace("amplitude=", "")));
                } catch (NumberFormatException nfe) {
                    player.sendMessage(DARK_RED + d.replace("amplitude=", "") + RED + " is not a valid amplitude number");
                    continue;
                }
                player.sendMessage(GREEN + "Successfully set the amplitude to " + DARK_GREEN + d.replace("amplitude=", ""));
            } else if (d.startsWith("multitude=")) {
                try {
                    data.setMultitude(Double.parseDouble(d.replace("multitude=", "")));
                } catch (NumberFormatException nfe) {
                    player.sendMessage(DARK_RED + d.replace("multitude=", "") + RED + " is not a valid multitude number");
                    continue;
                }
                player.sendMessage(GREEN + "Successfully set the multitude to " + DARK_GREEN + d.replace("multitude=", ""));
            } else if (d.startsWith("scale=")) {
                try {
                    data.setScale(Double.parseDouble(d.replace("scale=", "")));
                } catch (NumberFormatException nfe) {
                    player.sendMessage(DARK_RED + d.replace("scale=", "") + RED + " is not a valid scale number");
                    continue;
                }
                player.sendMessage(GREEN + "Successfully set the scale to " + DARK_GREEN + d.replace("scale=", ""));
            } else if (d.startsWith("octaves=")) {
                try {
                    data.setOctaves(Integer.parseInt(d.replace("octaves=", "")));
                } catch (NumberFormatException nfe) {
                    player.sendMessage(DARK_RED + d.replace("octaves=", "") + RED + " is not a valid octaves number");
                    continue;
                }
                player.sendMessage(GREEN + "Successfully set the octaves to " + DARK_GREEN + d.replace("octaves=", ""));
            } else if (d.startsWith("height=")) {
                try {
                    data.setHeight(Integer.parseInt(d.replace("height=", "")));
                } catch (NumberFormatException nfe) {
                    player.sendMessage(DARK_RED + d.replace("height=", "") + RED + " is not a valid height number");
                    continue;
                }
                player.sendMessage(GREEN + "Successfully set the height to " + DARK_GREEN + d.replace("height=", ""));
            } else if (d.startsWith("seed=")) {
                if (d.replace("seed=", "").equalsIgnoreCase("world")) {
                    data.setSeed(worldSeedValue);
                    player.sendMessage(GREEN + "Successfully set the seed of " + DARK_GREEN + " world");
                } else {
                    try {
                        data.setSeed(Long.parseLong(d.replace("seed=", "")));
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(DARK_RED + d.replace("seed=", "") + RED + " is not a valid seed number");
                        continue;
                    }
                    player.sendMessage(GREEN + "Successfully set the seed to " + DARK_GREEN + d.replace("seed=", ""));
                }
            } else if (d.startsWith("fromTop=".toLowerCase())) {
                data.setFromTop(Boolean.parseBoolean(d.replace("fromTop=".toLowerCase(), "")));
                player.sendMessage(GREEN + "Successfully set the fromTop to" + DARK_GREEN + Boolean.parseBoolean(d.replace("height=", "")));
            } else if (d.startsWith("start=")) {
                try {
                    Integer.parseInt(d.replace("start=", ""));
                    data.setStart(d.replace("start=", ""));
                    player.sendMessage(GREEN + "Successfully set the start to" + DARK_GREEN + d.replace("start=", ""));
                } catch (NumberFormatException ignore) {
                    if (getGen(d.replace("start=", "")) == null) {
                        player.sendMessage(RED + "TerrainGenerator " + DARK_RED + d.replace("start=", "") + RED + " does not exist");
                    } else {
                        data.setStart(d.replace("start=", ""));
                        player.sendMessage(GREEN + "Successfully set the start to " + DARK_GREEN + d.replace("start=", ""));
                    }
                }
            } else if (d.startsWith("material=")) {
                TerrainBlockData is = data.getTerrainBlockData();
                Material m = Material.getMaterial(d.replace("material=", "").toUpperCase());
                if (m == null) {
                    player.sendMessage(RED + "Material " + DARK_RED + d.replace("material=", "").toUpperCase() + RED + " was not found");
                    continue;
                }
                if (!m.isBlock()) {
                    player.sendMessage(RED + "Material " + DARK_RED + d.replace("material=", "").toUpperCase() + RED + " is not a block");
                    continue;
                }
                is.setMaterial(m);
                player.sendMessage(GREEN + "Successfully set the material to " + DARK_GREEN + d.replace("material=", ""));
                data.setTerrainBlockData(data.getTerrainBlockData());
            } else if (d.startsWith("direction=")) {
                TerrainBlockData is = data.getTerrainBlockData();
                try {
                    BlockFace face = BlockFace.valueOf(d.replace("direction=", "").toUpperCase());
                    is.setBlockFace(face);
                    player.sendMessage(GREEN + "Successfully set the direction to " + DARK_GREEN + d.replace("direction=", "").toUpperCase());
                } catch (IllegalArgumentException iae) {
                    player.sendMessage(RED + "Direction " + DARK_RED + d.replace("direction=", "").toUpperCase() + RED + " was not found");
                    continue;
                }
                data.setTerrainBlockData(data.getTerrainBlockData());
            } else if (d.startsWith("biome=")) {
                if (d.equalsIgnoreCase("biome=null")) {
                    data.setBiome(null);
                } else {
                    try {
                        data.setBiome(Biome.valueOf(d.replace("biome=", "")));
                        player.sendMessage(GREEN + "Successfully set the biome to " + DARK_GREEN + d.replace("biome=", ""));
                    } catch (Throwable ignore) {
                        player.sendMessage(DARK_RED + d.replace("biome=", "") + RED + " is not a existing biome");
                    }
                }
            } else {
                Message message = new Message();

                message.addText(textComponent(d, DARK_RED));
                message.addText(textComponent(" is not in a valid format, hover ", RED));
                HoverEvent hEvent = new HoverEvent(textComponent(""));
                //TerrainGenData properties
                for (String s : Arrays.asList("frequency", "amplitude", "multitude", "scale", "octaves", "height", "seed", "fromTop", "height", "start", "material", "direction", "biome")) {
                    hEvent.addText(textComponent(s, ChatColor.BLUE));
                    hEvent.addText(textComponent(", ", ChatColor.DARK_AQUA));
                }
                hEvent.removeLast();
                message.addText(textComponent("here", DARK_RED, hEvent));
                message.addText(textComponent(" for the available formats", RED));

                message.sendMessage(player);
            }
        }
    }

    @Override
    public String alias() {
        return "c";
    }

    @Override
    public void run(String[] args, Player player) {
        //terrain create <name> [data...]

        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("null")) {
                player.sendMessage(RED + "Can't use the name null");
                return;
            }
            if (args[1].toLowerCase().startsWith("iExamplesTG-".toLowerCase())) {
                player.sendMessage(RED + "Name can begin with 'iExamplesTG-'");
                return;
            }

            TerrainGenData data = terrainGenData(args[1], false);

            if (data != null) {
                player.sendMessage(GREEN + "TerrainGenerator successfully added");

                if (args.length > 2) {
                    setData(data, player, args, 2);
                }

            } else {
                player.sendMessage(RED + "TerrainGenerator already exist");
            }
        } else {
            player.sendMessage(RED + "Usage: " + DARK_RED + " /terrain create <name> [data...]");
        }
    }
}
