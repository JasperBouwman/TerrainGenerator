package com.spaceman.terrainGenerator.commands;

import com.spaceman.terrainGenerator.commandHander.CommandTemplate;
import com.spaceman.terrainGenerator.commandHander.HelpCommand;
import com.spaceman.terrainGenerator.commands.terrain.*;
import com.spaceman.terrainGenerator.fancyMessage.Attribute;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.ClickEvent;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.spaceman.terrainGenerator.ColorFormatter.*;
import static com.spaceman.terrainGenerator.Permissions.hasPermission;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;

public class Terrain extends CommandTemplate {
    
    public Terrain() {
        super(true, new CommandDescription(null, "The command for creating custom terrain/worlds", null));
    }
    
    @Override
    public void registerActions() {
        this.actions = new ArrayList<>();
        addAction(new Create());
        addAction(new Delete());
        addAction(new Gens());
        addAction(new Edit());
        addAction(new Generate());
        addAction(new GetData());
        addAction(new Mode());
        addAction(new com.spaceman.terrainGenerator.commands.terrain.List());
        addAction(new com.spaceman.terrainGenerator.commands.terrain.World());
        addAction(new SafeToEdit());
        addAction(new External());
        addAction(new Reload());
        addAction(new Clone());
        addAction(new Noise());
        addAction(new BiomeSettings());
        HelpCommand help = new HelpCommand(this);
        
        Message helpTerrainMode = new Message();
        helpTerrainMode.addText(textComponent("A TerrainMode is a function that is used to decorate your terrain, " +
                "such as tree generation or adding grass/flowers to your terrain. A ", infoColor));
        helpTerrainMode.addText(textComponent("final TerrainMode", varInfoColor));
        helpTerrainMode.addText(textComponent(" is a TerrainMode that is used when all the terrain is generated so it can use the final outcome. " +
                "The TerrainMode ", infoColor));
        helpTerrainMode.addText(textComponent("top", varInfoColor));
        helpTerrainMode.addText(textComponent(" edits the top layer of the terrain, this does not use the final outcome and is therefore not a ", infoColor));
        helpTerrainMode.addText(textComponent("final TerrainMode", varInfoColor));
        helpTerrainMode.addText(textComponent(". You have different ", infoColor));
        helpTerrainMode.addText(textComponent("ModeTypes", varInfoColor));
        helpTerrainMode.addText(textComponent(" (", infoColor));
        helpTerrainMode.addText(textComponent("DateMode", varInfoColor,
                new HoverEvent(textComponent("A DataMode stores only 1 object, however this object can make use of multiple sub-objects", infoColor))));
        helpTerrainMode.addText(textComponent(", ", infoColor));
        helpTerrainMode.addText(textComponent("ArrayMode", varInfoColor,
                new HoverEvent(textComponent("A ArrayMode stores multiple objects in a list, each object can make use of multiple sub-objects", infoColor))));
        helpTerrainMode.addText(textComponent(" and ", infoColor));
        helpTerrainMode.addText(textComponent("MapMode", varInfoColor,
                hoverEvent(textComponent("A MapMode stores multiple objects in a map. A map stores its values in a list using keys, example:\n", infoColor),
                        textComponent("1", varInfoColor), textComponent(" : ", infoColor), textComponent("one,\n", varInfoColor),
                        textComponent("2", varInfoColor), textComponent(" : ", infoColor), textComponent("two,\n", varInfoColor),
                        textComponent("3", varInfoColor), textComponent(" : ", infoColor), textComponent("three", varInfoColor)
                )));
        helpTerrainMode.addText(textComponent("). Some TerrainModes have extras, like ", infoColor));
        helpTerrainMode.addText(textComponent("Inverse", varInfoColor));
        helpTerrainMode.addText(textComponent(" or ", infoColor));
        helpTerrainMode.addText(textComponent("WaterLoggable", infoColor));
        helpTerrainMode.addText(textComponent(". Inverse usually means that the mode is flipped upside down, hover ", infoColor));
        helpTerrainMode.addText(textComponent("here", infoColor, hoverEvent(textComponent("Top", varInfoColor),
                textComponent(" is a TerrainMode that changes the top layer of the TerrainGenerator, " +
                        "when inverted it changes the bottom layer of the TerrainGenerator", infoColor))));
        helpTerrainMode.addText(textComponent(" for example.", infoColor));
        helpTerrainMode.addText(textComponent(" The WaterLoggable extra is more as a handy extra, " +
                "this is used to the block in the TerrainMode waterlogged or not. " +
                "There are 3 types of water logged, ", infoColor));
        helpTerrainMode.addText(textComponent("true", varInfoColor,
                new HoverEvent(textComponent("Block is water logged (of possible)", infoColor))));
        helpTerrainMode.addText(textComponent(", ", infoColor));
        helpTerrainMode.addText(textComponent("false", varInfoColor,
                new HoverEvent(textComponent("Block is not water logged", infoColor))));
        helpTerrainMode.addText(textComponent(" and ", infoColor));
        helpTerrainMode.addText(textComponent("null", varInfoColor,
                new HoverEvent(textComponent("Block used its default state, this is the default state", infoColor))));
        helpTerrainMode.addText(textComponent(". There is a way to create your own TerrainMode, click ", infoColor));
        String terrainModeTutorialURL = "https://github.com/JasperBouwman/TerrainGenerator/tree/master/src/com/spaceman/terrainGenerator/terrain/terrainMode/Tutorial.md";
        helpTerrainMode.addText(textComponent("here", varInfoColor,
                new HoverEvent(textComponent(terrainModeTutorialURL, infoColor)), ClickEvent.openUrl(terrainModeTutorialURL)).setInsertion(terrainModeTutorialURL));
        helpTerrainMode.addText(textComponent(" to go to the tutorial", infoColor));
        help.addExtraHelp("TerrainMode", helpTerrainMode);
        
        Message helpTerrainGenerator = new Message();
        helpTerrainGenerator.addText(textComponent("A TerrainGenerator is the object that creates the terrain that you have configured, ", infoColor));
        helpTerrainGenerator.addText(textComponent("this contains the properties of the main block for the terrain and some terrain settings. ", infoColor));
        helpTerrainGenerator.addText(textComponent("The shape of the terrain can be edited with the ", infoColor));
        helpTerrainGenerator.addText(textComponent("NoiseGenerator", varInfoColor, new HoverEvent(textComponent("/terrain help NoiseGenerator")), ClickEvent.runCommand("/terrain help NoiseGenerator")));
        helpTerrainGenerator.addText(textComponent(". For extra population or modulation you can add ", infoColor));
        helpTerrainGenerator.addText(textComponent("TerrainModes", varInfoColor, new HoverEvent(textComponent("/terrain help TerrainMode")), ClickEvent.runCommand("/terrain help TerrainMode")));
        helpTerrainGenerator.addText(textComponent(". You can also add additional TerrainGenerators to your main generator, the '", infoColor));
        helpTerrainGenerator.addText(textComponent("gens", varInfoColor, new HoverEvent(textComponent("/terrain gens list <name>"))));
        helpTerrainGenerator.addText(textComponent("' list is the list of TerrainGenerators that are also generated when the main TerrainGenerator is generated. " +
                "The first TerrainGenerator on the list is the first runner up to generate, " +
                "when the current one is done the first runner up is then generated, and than the second, ect. " +
                "When the 'fromTop' date type is true the next generator will start at the highest point, " +
                "when false it will generate around all the previous generated terrain. ", infoColor));
        helpTerrainGenerator.addText(textComponent("One important thing about multiple generated is that in the generation of the terrain " +
                "the TerrainGenerators in the gens list wont overwrite previous generated TerrainGenerators in the gens list", infoColor, Attribute.BOLD));
        help.addExtraHelp("TerrainGenerator", helpTerrainGenerator);
        
        Message helpTerrainWorld = new Message();
        helpTerrainWorld.addText(textComponent("A TerrainWorld is a world generated by a TerrainGenerator. " +
                "When a TerrainGenerator is used to generate a TerrainWorld it is not 'safe to edit'. " +
                "You can Still edit it, but at the next chunk generation the chunks may not compliment each other. " +
                "The tablist when you create a new TerrainWorld shows you the worlds that exist but are not loaded in, " +
                "its faster to recycle a world, but the chunk are not regenerated. " +
                "When creating a new world the server may crash during generation of the spawn chunks. " +
                "But when the server is restarting it will continue the generation. " +
                "When the server reloads all players are teleported out of all TerrainWorlds, and when the server loads back up " +
                "those players are teleported back. This is to reload the world inside the server", infoColor));
        help.addExtraHelp("TerrainWorld", helpTerrainWorld);
        
        Message helpNoiseGenerator = new Message();
        helpNoiseGenerator.addText(textComponent("The NoiseGenerator is the generator that creates the surface for the TerrainGenerator. " +
                "You can edit the TerrainNoise with the command ", infoColor));
        helpNoiseGenerator.addText(textComponent("/terrain noise", varInfoColor));
        helpNoiseGenerator.addText(textComponent(", Flatlands is faster to generate when you need a flat surface. " +
                "There is a way to create your own noise generator, click ", infoColor));
        String noiseGeneratorTutorialURL = "https://github.com/JasperBouwman/TerrainGenerator/tree/master/src/com/spaceman/terrainGenerator/terrain/terrainNoise/Tutorial.md";
        helpNoiseGenerator.addText(textComponent("here", varInfoColor,//todo create tutorial
                new HoverEvent(textComponent(noiseGeneratorTutorialURL, infoColor)), ClickEvent.openUrl(noiseGeneratorTutorialURL)).setInsertion(noiseGeneratorTutorialURL));
        helpNoiseGenerator.addText(textComponent(" to go to the tutorial", infoColor));
        help.addExtraHelp("NoiseGenerator", helpNoiseGenerator);
        
        Message helpExternal = new Message();
        helpExternal.addText(textComponent("Files in the folder '", infoColor));
        helpExternal.addText(textComponent(".../plugins/TerrainGenerator/etf", varInfoColor));
        helpExternal.addText(textComponent("' should be all .yml files. These files are External Terrain Files. " +
                "Here are the External TerrainGenerators saved by using: ", infoColor));
        helpExternal.addText(textComponent("/terrain external export <file name> [TerrainGenerator...]", varInfoColor));
        helpExternal.addText(textComponent(". These TerrainGenerators can't be edited, but are still fully usable. " +
                "External Terrain Files are mostly used to back-up TerrainGenerators, or to send TerrainGenerators to other people", infoColor));
        help.addExtraHelp("External", helpExternal);
        
        Message helpBiome = new Message();
        helpBiome.addText(textComponent("BiomeSettings are the settings that are used to add biomes to your terrain/world. " +
                "The biomes are calculated within squares, each of these squares is a section, the size can be edited with the ", infoColor));
        helpBiome.addText(textComponent("section size", varInfoColor));
        helpBiome.addText(textComponent(". In each of these sections are biomes randomly generated. " +
                "The amount of biomes per section is set by the ", infoColor));
        helpBiome.addText(textComponent("biome scale", varInfoColor));
        helpBiome.addText(textComponent(", the smaller the biome scale the larger the biomes are. " +
                "The biome size can be edited for each biome in the biome setting of the TerrainGenerator. " +
                "The larger the size of the biome the more chances the biome is selected in the random biome generation. " +
                "Just like the biome size the biome weight can be edited for each biome. " +
                "The biome size is sort of inverted multiplied by the weight (the smaller the weight, the larger the biome), " +
                "if all biome weights are different the smoother and natural the biomes intersect.", infoColor));
        help.addExtraHelp("BiomeSettings", helpBiome);
        
        addAction(help);
    }
    
    @Override
    public boolean execute(CommandSender commandSender, String command, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this plugin/command");
            return false;
        }
    
        Player player = (Player) commandSender;
    
        if (!hasPermission(player, "TerrainGenerator.All")) {
            return false;
        }
    
        //terrain create <name> [data...]
        //terrain delete <name>
        //terrain generate <name> <x> <z>
        //terrain getData <name>
        //terrain list [own|all]
        //terrain edit <name> <data...>
        //terrain reload
        //terrain safeToEdit [TerrainGenerator]
        //terrain clone <old name> <new name>
        //terrain gens add <name> <name>
        //terrain gens remove <name> <name>
        //terrain gens set <name> <name> <number>
        //terrain gens list <name>
        //terrain noise set <name> <noise> [data...]
        //terrain noise edit <name> <data...>
        //terrain noise list
        //terrain noise getData <name>
        //terrain noise description <noise>
        //terrain noise clone <name from> <name to>
        //terrain mode add <name> <TerrainMode name> [data...]
        //terrain mode remove <name> <TerrainMode name>
        //terrain mode set <name> <TerrainMode name> <number> [data... (only for new TerrainModes)]
        //terrain mode edit <name> <TerrainMode name> add <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> remove <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> set <number> <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> set <data...> {for DataBased only}
        //terrain mode getData <name> <TerrainMode name>
        //terrain mode description <TerrainMode name>
        //terrain mode inverse <name> <TerrainMode name> [boolean]
        //terrain mode waterLog <name> <TerrainMode name> set <data...>
        //terrain mode clone <name from> <mode> <name to>
        //terrain mode list [name]
        //terrain biomeSettings sectionSize <name> [size]
        //terrain biomeSettings biomeScale <name> [size]
        //terrain biomeSettings seed <name> [seed]
        //terrain biomeSettings add <name> <name> [data...]
        //terrain biomeSettings remove <name> <name>
        //terrain biomeSettings getData <name> [name]
        //terrain biomeSettings weight <name> own [weight]
        //terrain biomeSettings weight <name> get <name>
        //terrain biomeSettings weight <name> set <name> <weight>
        //terrain biomeSettings size <name> own [size]
        //terrain biomeSettings size <name> get <name>
        //terrain biomeSettings size <name> set <name> <size>
        //terrain world create <world name> <name> [data...]
        //terrain world unload <TerrainWorld> [delete]
        //terrain world list
        //terrain world tp [world name]
        //terrain world regenerate [<x1> <z1> <x2> <z2>]
        //terrain world autoLoad <world name> [autoLoad]
        //terrain world worldInfo
        //terrain external export <fileName> [TerrainGenerator...]
        //terrain external import <fileName> [TerrainGenerator...]
        //terrain external unload <fileName> [delete]
        //terrain external load <fileName>
        //terrain external list [file...]
        //terrain help
        //terrain help <page>
        //terrain help terrain <sub-command...>
        //terrain help <extra data>
        
        /*
        todo
          fix randomness
        todo create biome transition
          terrain biomeSettings transition <name> set <gen name> <transition type>
          terrain biomeSettings transition <name> edit <gen name> <data...>
          terrain biomeSettings transition <name> get <gen name>
        */
    
        if (args.length == 0) {
            getAction("help").run(args, player);
            return false;
        }
    
        if (!this.runCommands(args[0], args, player)) {
            player.sendMessage(formatError("%s is not a sub-command", args[0]));
        }
        return false;
    }
}
