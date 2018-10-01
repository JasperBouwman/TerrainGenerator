package com.spaceman.terrainGenerator.commands;

import com.spaceman.terrainGenerator.commands.terrain.*;
import com.spaceman.terrainGenerator.commands.terrain.example.ExampleHandler;
import com.spaceman.terrainGenerator.modes.AddGrass;
import com.spaceman.terrainGenerator.modes.Bedrock;
import com.spaceman.terrainGenerator.modes.Layers;
import com.spaceman.terrainGenerator.modes.StoneGen;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.TerrainMode;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.spaceman.terrainGenerator.Permissions.hasPermission;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.getGen;
import static com.spaceman.terrainGenerator.terrain.TerrainMode.getModes;
import static com.spaceman.terrainGenerator.terrain.WorldGenerator.generateWorld;

public class Terrain implements CommandExecutor, TabCompleter {

    private final ArrayList<CmdHandler> actions = new ArrayList<>();

    public Terrain() {
        actions.add(new AddGen());
        actions.add(new Create());
        actions.add(new Delete());
        actions.add(new Edit());
        actions.add(new Generate());
        actions.add(new GetData());
        actions.add(new Help());
        actions.add(new Mode());
        actions.add(new com.spaceman.terrainGenerator.commands.terrain.List());
        actions.add(new RemoveGen());
        actions.add(new SetGen());
        actions.add(new Example());
        actions.add(new com.spaceman.terrainGenerator.commands.terrain.World());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You muse be a player to use this plugin/command");
            return false;
        }

        Player player = (Player) commandSender;


        if (!hasPermission(player, "TerrainGenerator.All")) {
            return false;
        }

        //terrain
        //terrain lock <name>
        //terrain create <name> [data...]
        //terrain delete <name>
        //terrain getData <name>
        //terrain generate <name> <x> <z>
        //terrain list
        //terrain edit <name> <data...>
        //terrain addGen <name> <name>
        //terrain removeGen <name> <name>
        //terrain setGen <name> <name> <number>
        //terrain mode add <name> <TerrainMode name> [data...]
        //terrain mode remove <name> <TerrainMode name>
        //terrain mode set <name> <TerrainMode name> <number> [data...]
        //terrain mode copy <name> <name> <TerrainMode name>
        //terrain mode edit <name> <TerrainMode name> add <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> remove <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> set <number> <data...> {for ArrayBased and MapBased only}
        //terrain mode edit <name> <TerrainMode name> set <data...> {for DataBased only}
        //terrain mode getData <name> <TerrainMode name>
        //terrain mode description <TerrainMode name>
        //terrain mode list
        //terrain help
        //terrain example <example name>

        //terrain world create <world name> <name>
        //terrain world delete <world name>


        if (args.length == 0) {
            actions.get(6).run(args, player);

//            int a = player.getLocation().getBlockX() - 5;
//            int b = player.getLocation().getBlockY() + 1;
//            int c = player.getLocation().getBlockZ() - 5;
//            TerrainGenerator.LocData locData = new TerrainGenerator.LocData(player.getLocation(), 1, 1);
//            int startHouse = player.getLocation().getBlockY();
//
//            wallLog(startHouse, locData, a, c);
//            wallLogSide(startHouse, locData, a, c);
//            wallSide(locData, a, b, c);
//            ceilingStairs(locData, a, b, c);
//            ceilingSupport(locData, a, b, c);
//            setDoor(locData, a, b, c);


            return false;
        }

        if (args.length == 2 && args[1].equalsIgnoreCase("world")) {

            TerrainGenData d3 = terrainGenData("d3");
            d3.setStart(60);
            d3.setHeight(110);
            d3.setTerrainBlockData(new TerrainBlockData(Material.SNOW_BLOCK));
            d3.setSeed(435678);
            Layers newLayers4 = new Layers();
            LinkedHashMap<TerrainBlockData, Integer> layers4 = new LinkedHashMap<>();
            layers4.put(new TerrainBlockData(Material.SNOW_BLOCK), 5);
            layers4.put(new TerrainBlockData(Material.STONE), 100);
            newLayers4.setModeData(layers4);
            d3.addMode(newLayers4);
            Bedrock bedrock = new Bedrock();
            bedrock.setModeData(true);
            d3.addMode(bedrock);

            TerrainGenData d1 = terrainGenData("d1");
            d1.setSeed(34856732);
            d1.setTerrainBlockData(new TerrainBlockData(Material.STONE));
            d1.setStart(60);
            d1.setHeight(100);
            d1.setMultitude(30);
            LinkedHashMap<TerrainBlockData, Integer> layers1 = new LinkedHashMap<>();
            layers1.put((new TerrainBlockData(Material.COBBLESTONE)), 4);
            layers1.put((new TerrainBlockData(Material.DIRT)), 3);
            Layers newLayers1 = new Layers();
            newLayers1.setModeData(layers1);
            d1.addMode(newLayers1);
            StoneGen newStoneGen1 = new StoneGen();
            newStoneGen1.setModeData(d3.getName());
            d1.addMode(newStoneGen1);
//
            TerrainGenData d2 = terrainGenData("d2");
            d2.setSeed(58736785);
            d2.setTerrainBlockData(new TerrainBlockData(Material.ICE));
            d2.setStart(d1.getStart());
            d2.setMultitude(10);
            d2.setHeight(d1.getHeight() - 10);
//
            TerrainGenData floor = terrainGenData("floor");
            floor.setStart(60);
            floor.setHeight(d1.getHeight());
            floor.setAmplitude(0.1);
            floor.setMultitude(d1.getMultitude());
//            floor.setTerrainBlockData(new TerrainBlockData(Material.DIRT));

            LinkedHashMap<TerrainBlockData, Integer> layers3 = new LinkedHashMap<>();
            layers3.put((new TerrainBlockData(Material.GRASS_BLOCK)), 1);
            layers3.put((new TerrainBlockData(Material.DIRT)), 3);
            layers3.put((new TerrainBlockData(Material.STONE)), 100);
            Layers newLayers3 = new Layers();
            newLayers3.setModeData(layers3);
            floor.addMode(newLayers3);

            LinkedHashMap<TerrainBlockData, Integer> addGrass = new LinkedHashMap<>();
            addGrass.put((new TerrainBlockData(Material.TALL_GRASS)), 1);
            addGrass.put((new TerrainBlockData(Material.STRUCTURE_VOID)), 3);
            AddGrass newAddGrass = new AddGrass();
            newAddGrass.setModeData(addGrass);
            floor.addMode(newAddGrass);

            floor.addMode(new Bedrock());

            StoneGen newStoneGen = new StoneGen();
            newStoneGen.setModeData(d1.getName());
            floor.addMode(newStoneGen);
            floor.addGenerator(d2.getName());

            org.bukkit.World newWorld = generateWorld("tmpWorld_" + args[0], floor.getName());

            player.teleport(new Location(newWorld, 0, 100, 0));

            return false;
        }

        for (CmdHandler action : actions) {
            if (args[0].equalsIgnoreCase(action.getClass().getSimpleName()) ||
                    (action.alias() != null && action.alias().equalsIgnoreCase(args[0]))) {
                action.run(args, player);
                return false;
            }
        }
        player.sendMessage(ChatColor.RED + args[0] + " is not a sub-command");

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        ArrayList<String> list = new ArrayList<>();

        //terrain
        if (args.length == 1) {
            for (CmdHandler cmd : actions) list.add(cmd.getClass().getSimpleName().toLowerCase());
            return StringUtil.copyPartialMatches(args[0], list, new ArrayList<>(list.size()));
        }
        if (args.length == 2) {
            //terrain delete
            //terrain getData
            //terrain generate
            //terrain edit
            //terrain addGen
            //terrain removeGen
            //terrain setGen
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("getData") || args[0].equalsIgnoreCase("generate") ||
                    args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("addGen") || args[0].equalsIgnoreCase("removeGen") ||
                    args[0].equalsIgnoreCase("setGen")) {
                return StringUtil.copyPartialMatches(args[1], TerrainGenerator.terrainGenData.keySet(), new ArrayList<>(TerrainGenerator.terrainGenData.keySet().size()));
            }
            //terrain mode
            if (args[0].equalsIgnoreCase("mode")) {
                list.addAll(Arrays.asList("add", "remove", "set", "copy", "edit", "getData", "description", "list"));
                return StringUtil.copyPartialMatches(args[1], list, new ArrayList<>(list.size()));
            }
            //terrain example
            if (args[0].equalsIgnoreCase("example")) {
                for (ExampleHandler cmd : ExampleHandler.getExamples()) {
                    list.add(cmd.name());
                }
                return StringUtil.copyPartialMatches(args[1], list, new ArrayList<>(list.size()));
            }
        }
        if (args.length == 3) {
            //terrain create <name>
            //terrain edit <name>
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("edit")) {
                //TerrainGenData properties
                list.addAll(Arrays.asList("frequency=", "amplitude=", "multitude=", "scale=", "octaves=", "height=", "seed=", "fromTop=", "start=", "material=", "direction=", "biome="));
                return StringUtil.copyPartialMatches(args[2], list, new ArrayList<>(list.size()));
            }
            //terrain addGen <name>
            //terrain setGen <name>
            else if (args[0].equalsIgnoreCase("addGen") || args[0].equalsIgnoreCase("setGen")) {
                return StringUtil.copyPartialMatches(args[2], TerrainGenerator.terrainGenData.keySet(), new ArrayList<>(TerrainGenerator.terrainGenData.keySet().size()));
            }
            //terrain removeGen <name>
            else if (args[0].equalsIgnoreCase("removeGen")) {
                TerrainGenData data = getGen(args[1]);
                if (data != null) {
                    list.addAll(data.getGenerators());
                    return StringUtil.copyPartialMatches(args[2], list, new ArrayList<>(list.size()));
                }
            }
            //terrain mode add
            //terrain mode remove
            //terrain mode set
            //terrain mode copy
            //terrain mode edit
            //terrain mode getData
            if (args[0].equalsIgnoreCase("mode")) {
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set") ||
                        args[1].equalsIgnoreCase("edit") || args[1].equalsIgnoreCase("getData") || args[1].equalsIgnoreCase("copy")) {
                    return StringUtil.copyPartialMatches(args[2], TerrainGenerator.terrainGenData.keySet(), new ArrayList<>(TerrainGenerator.terrainGenData.keySet().size()));
                }
            }
            //terrain mode description
            if (args[0].equalsIgnoreCase("mode")) {
                if (args[1].equalsIgnoreCase("description")) {
                    list.addAll(getModes());
                    return StringUtil.copyPartialMatches(args[2], list, new ArrayList<>(list.size()));
                }
            }
        }
        if (args.length == 4) {
            //terrain mode add <name>
            //terrain mode remove <name>
            //terrain mode set <name>
            //terrain mode edit <name>
            if (args[0].equalsIgnoreCase("mode")) {
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set") ||
                        args[1].equalsIgnoreCase("edit")) {
                    list.addAll(getModes());
                    return StringUtil.copyPartialMatches(args[3], list, new ArrayList<>(list.size()));
                }
            }
            //terrain mode copy <name>
            if (args[0].equalsIgnoreCase("mode")) {
                if (args[1].equalsIgnoreCase("copy")) {
                    return StringUtil.copyPartialMatches(args[3], TerrainGenerator.terrainGenData.keySet(), new ArrayList<>(TerrainGenerator.terrainGenData.keySet().size()));
                }
            }
            //terrain mode getData <name>
            if (args[0].equalsIgnoreCase("mode")) {
                if (args[1].equalsIgnoreCase("getData")) {
                    TerrainGenData data = getGen(args[2]);
                    if (data != null) {
                        for (TerrainMode o : data.getModes()) {
                            list.add(o.getModeName());
                        }
                        return StringUtil.copyPartialMatches(args[3], list, new ArrayList<>(list.size()));
                    }
                }
            }
        }
        if (args.length == 5) {
            //terrain mode copy <name> <name>
            if (args[0].equalsIgnoreCase("mode")) {
                if (args[1].equalsIgnoreCase("copy")) {
                    TerrainGenData data = getGen(args[3]);
                    if (data != null) {
                        for (TerrainMode o : data.getModes()) {
                            list.add(o.getModeName());
                        }
                        return StringUtil.copyPartialMatches(args[4], list, new ArrayList<>(list.size()));
                    }
                }
            }
            //terrain mode edit <name> <TerrainMode name>
            if (args[0].equalsIgnoreCase("mode")) {
                if (args[1].equalsIgnoreCase("edit")) {
                    list.addAll(Arrays.asList("add", "remove", "set"));
                    return StringUtil.copyPartialMatches(args[4], list, new ArrayList<>(list.size()));
                }
            }
        }

        return list;
    }
}
