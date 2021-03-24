package com.spaceman.terrainGenerator.commands.terrain.mode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.commandHander.ArgumentType;
import com.spaceman.terrainGenerator.commandHander.EmptyCommand;
import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.terrainMode.ArrayMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.MapMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;
import static com.spaceman.terrainGenerator.commands.TabCompletes.ownGenerators;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.getGen;

public class Edit extends SubCommand {

    @SuppressWarnings("unchecked")
    public Edit() {
        EmptyCommand emptyCommandSetNumberData = new EmptyCommand();
        emptyCommandSetNumberData.setTabRunnable((args, player) -> {
            TerrainGenData genData = getGen(args[2]);
            if (genData.getMode(args[3]) instanceof ArrayMode) {
                ArrayMode mode = (ArrayMode) genData.getMode(args[3]);
                return mode.tabListSet(args, player);
            } else if (genData.getMode(args[3]) instanceof MapMode) {
                MapMode mode = (MapMode) genData.getMode(args[3]);
                return mode.tabListSet(args, player);
            }
            return new ArrayList<>();
        });
        emptyCommandSetNumberData.setLooped(true);
        emptyCommandSetNumberData.setCommandName("data...", ArgumentType.REQUIRED);
        emptyCommandSetNumberData.setCommandDescription(textComponent("This command is used to add data to the given TerrainMode, " +
                "the number argument is the place the data will get in the data list of that TerrainMode. " +
                "This command can only be used if the TerrainMode is a MapMode or an ArrayMode. " +
                "If the TerrainMode is a DataMode you need the command ", ColorFormatter.infoColor),
                textComponent("/terrain mode edit <name> <TerrainMode> set <data...>", ColorFormatter.varInfoColor));
        EmptyCommand emptyCommandSetNumber = new EmptyCommand();
        emptyCommandSetNumber.setTabRunnable((args, player) -> {
            TerrainGenData genData = getGen(args[2]);
            if (genData.getMode(args[3]) instanceof ArrayMode) {
                ArrayMode mode = (ArrayMode) genData.getMode(args[3]);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i <= mode.getModeData().size(); i++) {
                    list.add(String.valueOf(i + 1));
                }
                return list;
            } else if (genData.getMode(args[3]) instanceof MapMode) {
                MapMode mode = (MapMode) genData.getMode(args[3]);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i <= mode.getModeData().size(); i++) {
                    list.add(String.valueOf(i + 1));
                }
                return list;
            }
            return Collections.emptyList();
        });
        emptyCommandSetNumber.setCommandName("number", ArgumentType.REQUIRED);
        emptyCommandSetNumber.addAction(emptyCommandSetNumberData);
        
        EmptyCommand emptyCommandSetData = new EmptyCommand();
        emptyCommandSetData.setTabRunnable((args, player) -> getGen(args[2]).getMode(args[3]).tabListSet(args, player));
        emptyCommandSetData.setLooped(true);
        emptyCommandSetData.setCommandName("data...", ArgumentType.REQUIRED);
        emptyCommandSetData.setCommandDescription(textComponent("This command is used to set the data of the given TerrainMode, " +
                "this command can only be used if the TerrainMode is a DataMode. " +
                "If the TerrainMode is not a DataMode you need the command ", ColorFormatter.infoColor),
                textComponent("/terrain mode edit <name> <TerrainMode name> set <number> <data...>", ColorFormatter.varInfoColor),
                textComponent(" or ", ColorFormatter.infoColor),
                textComponent("/terrain mode edit <name> <TerrainMode name> add <data...>", ColorFormatter.varInfoColor));
    
        EmptyCommand emptyCommandSet = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return "set";
            }
        };
        emptyCommandSet.setTabRunnable((args, player) -> {
            TerrainGenData genData = getGen(args[2]);
            TerrainMode mode = genData.getMode(args[3]);
            if (mode instanceof DataMode) {
                return emptyCommandSetData.getTabRunnable().run(args, player);
            } else if (mode instanceof ArrayMode || mode instanceof MapMode){
                return emptyCommandSetNumber.getTabRunnable().run(args, player);
            }
            return Collections.emptyList();
        });
        emptyCommandSet.setCommandName(emptyCommandSet.getName(null), ArgumentType.FIXED);
        emptyCommandSet.addAction(emptyCommandSetNumber);
        emptyCommandSet.addAction(emptyCommandSetData);
        
        
        EmptyCommand emptyCommandAdd1 = new EmptyCommand();
        emptyCommandAdd1.setTabRunnable((args, player) -> {
            TerrainGenData genData = getGen(args[2]);
            if (genData.getMode(args[3]) instanceof ArrayMode) {
                ArrayMode mode = (ArrayMode) genData.getMode(args[3]);
                return mode.tabListAdd(args, player);
            } else if (genData.getMode(args[3]) instanceof MapMode) {
                MapMode mode = (MapMode) genData.getMode(args[3]);
                return mode.tabListAdd(args, player);
            }
            return Collections.emptyList();
        });
        emptyCommandAdd1.setLooped(true);
        emptyCommandAdd1.setCommandName("data...", ArgumentType.REQUIRED);
        emptyCommandAdd1.setCommandDescription(textComponent("This command is used to add data to the given TerrainMode, " +
                "this command can only be used if the TerrainMode is a MapMode or an ArrayMode. It will set the given data to the end of the data list", ColorFormatter.infoColor));
        EmptyCommand emptyCommandAdd = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return "add";
            }
        };
        emptyCommandAdd.setTabRunnable(emptyCommandAdd1.getTabRunnable());
        emptyCommandAdd.setCommandName(emptyCommandAdd.getName(null), ArgumentType.FIXED);
        emptyCommandAdd.addAction(emptyCommandAdd1);
        
        
        EmptyCommand emptyCommandRemove1 = new EmptyCommand();
        emptyCommandRemove1.setTabRunnable((args, player) -> {
            TerrainGenData genData = getGen(args[2]);
            if (genData.getMode(args[3]) instanceof ArrayMode) {
                ArrayMode mode = (ArrayMode) genData.getMode(args[3]);
                return mode.tabListRemove(args, player);
            } else if (genData.getMode(args[3]) instanceof MapMode) {
                MapMode mode = (MapMode) genData.getMode(args[3]);
                return mode.tabListRemove(args, player);
            }
            return new ArrayList<>();
        });
        emptyCommandRemove1.setLooped(true);
        emptyCommandRemove1.setCommandName("data...", ArgumentType.REQUIRED);
        emptyCommandRemove1.setCommandDescription(textComponent("This command is used to remove data to the given TerrainMode, " +
                "this command can only be used if the TerrainMode is a MapMode or an ArrayMode", ColorFormatter.infoColor));
        EmptyCommand emptyCommandRemove = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return "remove";
            }
        };
        emptyCommandRemove.setTabRunnable(emptyCommandRemove1.getTabRunnable());
        emptyCommandRemove.setCommandName(emptyCommandRemove.getName(null), ArgumentType.FIXED);
        emptyCommandRemove.addAction(emptyCommandRemove1);
        
        EmptyCommand emptyCommand1 = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return argument;
            }
        };
        emptyCommand1.setTabRunnable((args, player) -> {
            //terrain mode edit <name> <TerrainMode name> <add|remove|set>
            TerrainGenData genData = getGen(args[2]);
            if (genData.getMode(args[3]) instanceof DataMode) {
                return Collections.singletonList("set");
            } else if (genData.getMode(args[3]) instanceof ArrayMode || genData.getMode(args[3]) instanceof MapMode) {
                return Arrays.asList("set", "add", "remove");
            }
        
            return new ArrayList<>();
        });
        emptyCommand1.addAction(emptyCommandRemove);
        emptyCommand1.addAction(emptyCommandAdd);
        emptyCommand1.addAction(emptyCommandSet);
        emptyCommand1.setCommandName("TerrainMode name", ArgumentType.REQUIRED);
        
        EmptyCommand emptyCommand = new EmptyCommand(){
            @Override
            public String getName(String argument) {
                return argument;
            }
        };
        emptyCommand.setTabRunnable((args, player) -> {
            //terrain mode edit <name> <TerrainMode name>
            return getGen(args[2]).getModes().stream().map(TerrainMode::getModeName).collect(Collectors.toList());
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
        //terrain mode edit <name> <TerrainMode name> add <data...> {for ArrayMode and MapMode only}
        //terrain mode edit <name> <TerrainMode name> remove <data...> {for ArrayMode and MapMode only}
        //terrain mode edit <name> <TerrainMode name> set <number> <data...> {for ArrayMode and MapMode only}
        //terrain mode edit <name> <TerrainMode name> set <data...> {for DataMode only}

        if (args.length > 5) {
            TerrainGenData data = getGen(args[2]);
            if (data != null) {
                if (data.getName().contains("/")) {
                    player.sendMessage(formatError("You can't edit an external TerrainGenerator"));
                    return;
                }
                TerrainMode mode = data.getMode(args[3]);
                if (mode != null) {
                    if (args[4].equalsIgnoreCase("set")) {
                        if (mode instanceof DataMode) {
                            ((DataMode) mode).setData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                            return;
                        }
                        if (args.length > 6) {
                            if (mode instanceof MapMode) {
                                int i;
                                try {
                                    i = Integer.parseInt(args[5]);
                                } catch (NumberFormatException nfe) {
                                    player.sendMessage(formatError("%s is not a valid number", args[5]));
                                    return;
                                }
                                ((MapMode) mode).setData(new LinkedList<>(Arrays.asList(args).subList(6, args.length)), i, player);
                            } else if (mode instanceof ArrayMode) {
                                int i;
                                try {
                                    i = Integer.parseInt(args[5]);
                                } catch (NumberFormatException nfe) {
                                    player.sendMessage(formatError("%s is not a valid number", args[5]));
                                    return;
                                }
                                ((ArrayMode) mode).setData(new LinkedList<>(Arrays.asList(args).subList(6, args.length)), i, player);
                            }
                        } else {
                            Message message = new Message();
                            message.addText(textComponent("Usage: ", ColorFormatter.errorColor));
                            HoverEvent hEvent = HoverEvent.hoverEvent("");
                            hEvent.addText(textComponent("This argument is only used for ", ColorFormatter.infoColor));
                            hEvent.addText(textComponent("set", ColorFormatter.varInfoColor));
                            hEvent.addText(textComponent(" and for the TerrainModes that are an ", ColorFormatter.infoColor));
                            hEvent.addText(textComponent("ArrayMode", ColorFormatter.varInfoColor));
                            hEvent.addText(textComponent(" or a ", ColorFormatter.infoColor));
                            hEvent.addText(textComponent("MapMode", ColorFormatter.varInfoColor));
                            message.addText(textComponent("/terrain mode edit <name> <TerrainMode name> set", ColorFormatter.varErrorColor));
                            message.addText(textComponent(" [<number>]", ColorFormatter.varErrorColor, hEvent));
                            message.addText(textComponent(" <data...>", ColorFormatter.varErrorColor));
                            message.sendMessage(player);
                        }
                    } else if (args[4].equalsIgnoreCase("add")) {

                        if (mode instanceof MapMode) {
                            ((MapMode) mode).addData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                        } else if (mode instanceof ArrayMode) {
                            ((ArrayMode) mode).addData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                        } else if (mode instanceof DataMode) {
                            player.sendMessage(formatError("This command can not be used for a DataMode TerrainMode"));
                        }

                    } else if (args[4].equalsIgnoreCase("remove")) {

                        if (mode instanceof MapMode) {
                            ((MapMode) mode).removeData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                        } else if (mode instanceof ArrayMode) {
                            ((ArrayMode) mode).removeData(new LinkedList<>(Arrays.asList(args).subList(5, args.length)), player);
                        } else if (mode instanceof DataMode) {
                            player.sendMessage(formatError("This command can not be used for a DataMode TerrainMode"));
                        }
                    }

                } else {
                    player.sendMessage(formatError("Could not find TerrainMode %s", args[3]));
                }
            } else {
                player.sendMessage(formatError("TerrainGenerator %s does not exist", args[2]));
            }
        } else {
            Message message = new Message();
            message.addText(textComponent("Usage: ", ColorFormatter.errorColor));
            message.addText(textComponent("/terrain mode edit <name> <TerrainMode name> <add|remove|set> ", ColorFormatter.varErrorColor));
            HoverEvent hEvent = HoverEvent.hoverEvent("");
            hEvent.addText(textComponent("This argument is only used for command ", ColorFormatter.infoColor));
            hEvent.addText(textComponent("set", ColorFormatter.varInfoColor));
            hEvent.addText(textComponent(" and for the TerrainModes that are an ", ColorFormatter.infoColor));
            hEvent.addText(textComponent("ArrayMode", ColorFormatter.varInfoColor));
            hEvent.addText(textComponent(" or a ", ColorFormatter.infoColor));
            hEvent.addText(textComponent("MapMode", ColorFormatter.varInfoColor));
            message.addText(textComponent("[<number>]", ColorFormatter.varErrorColor, hEvent));
            message.addText(textComponent(" <data...>", ColorFormatter.varErrorColor));

            message.sendMessage(player);
        }

    }
}
