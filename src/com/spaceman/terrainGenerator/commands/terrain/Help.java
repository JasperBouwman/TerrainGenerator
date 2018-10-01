package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commands.CmdHandler;
import com.spaceman.terrainGenerator.fancyMessage.book.Book;
import com.spaceman.terrainGenerator.fancyMessage.book.BookPage;
import com.spaceman.terrainGenerator.fancyMessage.events.ClickEvent;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;

public class Help extends CmdHandler {
    @Override
    public String alias() {
        return "h";
    }

    @Override
    public void run(String[] args, Player player) {

        Book book = new Book("TerrainGenerator", "The_Spaceman");

        BookPage page = book.createPage(textComponent("TerrainGenerator", ChatColor.BLUE));
        page.addText(textComponent("\n\n\nIn this book you can find a description of the plugin and all of it usages and commands", ChatColor.DARK_AQUA));

        BookPage indexP1 = book.createPage();
        indexP1.addText(textComponent("Index", ChatColor.DARK_AQUA));
        indexP1.addText(textComponent("\n\nAbout the plugin", ChatColor.BLUE, ClickEvent.changePage("3"), hoverEvent(textComponent("page: 3", ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nAbout TerrainModes", ChatColor.BLUE, ClickEvent.changePage("5"), hoverEvent(textComponent("page: 5", ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nCommands", ChatColor.BLUE, ClickEvent.changePage("8"), hoverEvent(textComponent("page: 8", ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nAbout the author", ChatColor.BLUE, ClickEvent.changePage("34"), hoverEvent(textComponent("page: 34", ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nMaking your own TerrainMode", ChatColor.BLUE, ClickEvent.changePage("35"), hoverEvent(textComponent("page: 35", ChatColor.DARK_GREEN))));

        BookPage introP1 = book.createPage();
        introP1.addText(textComponent("About the plugin", ChatColor.DARK_AQUA));
        introP1.addText(textComponent("\n\nThis plugin generates custom made terrain", ChatColor.BLUE));
        introP1.addText(textComponent("\nYou can change all the data of a TerrainGenerator, you can even add multiple TerrainGenerators to a single TerrainGenerator. How this works is:" +
                " When generating it will first generate the selected", ChatColor.BLUE));
        BookPage introP2 = book.createPage();
        introP2.addText(textComponent("TerrainGenerator, when it is done with that it will go on to the next one. The new TerrainGenerator won't override the" +
                " previous generated TerrainGenerators, this can be used to generate multiple mountains or a flat landscape surrounded by a mountain landscape", ChatColor.BLUE));
        BookPage introP3 = book.createPage();
        introP3.addText(textComponent("About TerrainModes", ChatColor.DARK_AQUA));
        introP3.addText(textComponent("\n\nA TerrainMode is a extra setting that you can include/exclude in the TerrainGenerator. These TerrainModes can be made separably from this plugin." +
                " What a TerrainMode does depends on the TerrainMode. What it can do it chance the ", ChatColor.BLUE));
        BookPage introP4 = book.createPage();
        introP4.addText(textComponent("generated outcome of the owning TerrainGenerator (hover ", ChatColor.BLUE));
        introP4.addText(textComponent("here", ChatColor.DARK_GREEN, hoverEvent(textComponent("layers, top, topR, stoneGen, etc", ChatColor.DARK_GREEN))));
        introP4.addText(textComponent(" for some examples). It can also place some blocks on top of the TerrainGenerator (hover ", ChatColor.BLUE));
        introP4.addText(textComponent("here", ChatColor.DARK_GREEN, hoverEvent(textComponent("addWater, addLava, addGrass, etc", ChatColor.DARK_GREEN))));
        introP4.addText(textComponent(" for some examples),", ChatColor.BLUE));
        BookPage introP5 = book.createPage();
        introP5.addText(textComponent("when some can place whole features to make it look populated (hover ", ChatColor.BLUE));
        introP5.addText(textComponent("here", ChatColor.DARK_GREEN, hoverEvent(textComponent("addHouse, addTrees", ChatColor.DARK_GREEN))));
        introP5.addText(textComponent(" for some examples), there are lots of ways to use and make a TerrainGenerator.\nIt is possible to make your own TerrainMode click ", ChatColor.BLUE));
        introP5.addText(textComponent("here", ChatColor.DARK_GREEN, ClickEvent.changePage("35"), hoverEvent(textComponent("page: 35", ChatColor.DARK_GREEN))));
        introP5.addText(textComponent(" to go to the page", ChatColor.BLUE));

        BookPage commandPage1 = book.createPage();
        commandPage1.addText(textComponent("Commands:", ChatColor.DARK_AQUA));
        commandPage1.addText(textComponent("\n/terrain", ChatColor.BLUE,
                ClickEvent.changePage("33"), hoverEvent(textComponent("page: 33", ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain create <name> [data...]", ChatColor.DARK_GREEN,
                ClickEvent.changePage("13"), hoverEvent(textComponent("page: 13", ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain delete <name>", ChatColor.BLUE,
                ClickEvent.changePage("14"), hoverEvent(textComponent("page: 14", ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain getData <name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage("14"), hoverEvent(textComponent("page: 14", ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain generate <name> <x> <z>", ChatColor.BLUE,
                ClickEvent.changePage("15"), hoverEvent(textComponent("page: 15", ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain list", ChatColor.DARK_GREEN,
                ClickEvent.changePage("16"), hoverEvent(textComponent("page: 16", ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain edit <name> <data...>", ChatColor.BLUE,
                ClickEvent.changePage("17"), hoverEvent(textComponent("page: 17", ChatColor.DARK_GREEN))));
        BookPage commandPage2 = book.createPage();
        commandPage2.addText(textComponent("Commands:", ChatColor.DARK_AQUA));
        commandPage2.addText(textComponent("\n/terrain addGen <name> <name>", ChatColor.BLUE,
                ClickEvent.changePage("18"), hoverEvent(textComponent("page: 18", ChatColor.DARK_GREEN))));
        commandPage2.addText(textComponent("\n/terrain removeGen <name> <name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage("19"), hoverEvent(textComponent("page: 19", ChatColor.DARK_GREEN))));
        commandPage2.addText(textComponent("\n/terrain setGen <name> <name> <number>", ChatColor.BLUE,
                ClickEvent.changePage("20"), hoverEvent(textComponent("page: 20", ChatColor.DARK_GREEN))));
        BookPage commandPage3 = book.createPage();
        commandPage3.addText(textComponent("Commands:", ChatColor.DARK_AQUA));
        commandPage3.addText(textComponent("\n/terrain mode add <name> <TerrainMode name>", ChatColor.BLUE,
                ClickEvent.changePage("21"), hoverEvent(textComponent("page: 21", ChatColor.DARK_GREEN))));
        commandPage3.addText(textComponent("\n/terrain mode remove <name> <TerrainMode name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage("23"), hoverEvent(textComponent("page: 23", ChatColor.DARK_GREEN))));
        commandPage3.addText(textComponent("\n/terrain mode set <name> <TerrainMode name> <number>", ChatColor.BLUE,
                ClickEvent.changePage("24"), hoverEvent(textComponent("page: 24", ChatColor.DARK_GREEN))));
        commandPage3.addText(textComponent("\n/terrain mode copy <name> <name> <TerrainMode name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage("25"), hoverEvent(textComponent("page: 25", ChatColor.DARK_GREEN))));
        BookPage commandPage4 = book.createPage();
        commandPage4.addText(textComponent("Commands:", ChatColor.DARK_AQUA));
        commandPage4.addText(textComponent("\n/terrain mode edit <name> <TerrainMode name> add <data...>", ChatColor.BLUE,
                ClickEvent.changePage("26"), hoverEvent(textComponent("page: 26", ChatColor.DARK_GREEN))));
        commandPage4.addText(textComponent("\n/terrain mode edit <name> <TerrainMode name> remove <data...>", ChatColor.DARK_GREEN,
                ClickEvent.changePage("27"), hoverEvent(textComponent("page: 27", ChatColor.DARK_GREEN))));
        commandPage4.addText(textComponent("\n/terrain mode edit <name> <TerrainMode name> set <number> <data...>", ChatColor.BLUE,
                ClickEvent.changePage("28"), hoverEvent(textComponent("page: 28", ChatColor.DARK_GREEN))));
        commandPage4.addText(textComponent("\n/terrain mode edit <name> <TerrainMode name> set <data...>", ChatColor.DARK_GREEN,
                ClickEvent.changePage("29"), hoverEvent(textComponent("page: 29", ChatColor.DARK_GREEN))));
        BookPage commandPage5 = book.createPage();
        commandPage5.addText(textComponent("Commands:", ChatColor.DARK_AQUA));
        commandPage5.addText(textComponent("\n/terrain mode getData <name> <TerrainMode name>", ChatColor.BLUE,
                ClickEvent.changePage("30"), hoverEvent(textComponent("page: 30", ChatColor.DARK_GREEN))));
        commandPage5.addText(textComponent("\n/terrain mode description <TerrainMode name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage("31"), hoverEvent(textComponent("page: 31", ChatColor.DARK_GREEN))));
        commandPage5.addText(textComponent("\n/terrain mode list", ChatColor.BLUE,
                ClickEvent.changePage("32"), hoverEvent(textComponent("page: 32", ChatColor.DARK_GREEN))));
        commandPage5.addText(textComponent("\n/terrain example <example name> <x> <z>", ChatColor.DARK_GREEN,
                ClickEvent.changePage("32"), hoverEvent(textComponent("page: 32", ChatColor.DARK_GREEN))));
        commandPage5.addText(textComponent("\n/terrain help", ChatColor.BLUE,
                ClickEvent.changePage("33"), hoverEvent(textComponent("page: 33", ChatColor.DARK_GREEN))));

        BookPage cp1 = book.createPage();
        cp1.addText(textComponent("/terrain create <name> [data...]", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain c <name> [data...]", ChatColor.DARK_GREEN))));
        cp1.addText(textComponent("\n\nThis command creates a new TerrainGenerator. The data arg is optional," +
                " when not given the data it is going to be its default value", ChatColor.BLUE));
        BookPage cp2 = book.createPage();
        cp2.addText(textComponent("/terrain delete <name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain del <name>", ChatColor.DARK_GREEN))));
        cp2.addText(textComponent("\n\nThis command deletes a TerrainGenerator", ChatColor.BLUE));
        cp2.addText(textComponent("\n\n/terrain getData <name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain d <name>", ChatColor.DARK_GREEN))));
        cp2.addText(textComponent("\n\nThis command sends the TerrainGenerator data to the player", ChatColor.BLUE));
        BookPage cp3 = book.createPage();
        cp3.addText(textComponent("/terrain generate <name> <x> <z>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain g <name> <x> <z>", ChatColor.DARK_GREEN))));
        cp3.addText(textComponent("\n\nThis command generated the TerrainGenerator. When using large numbers (x/z values) it can take a while to process it", ChatColor.BLUE));
        BookPage cp4 = book.createPage();
        cp4.addText(textComponent("When the server stops responding you can reload the server if possible, this stops generating the TerrainGenerator", ChatColor.BLUE));
        cp4.addText(textComponent("\n\n/terrain list", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain l", ChatColor.DARK_GREEN))));
        cp4.addText(textComponent("\n\nThis command sends the player all the available TerrainGenerators", ChatColor.BLUE));
        BookPage cp5 = book.createPage();
        cp5.addText(textComponent("/terrain edit <name> <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain e <data...>", ChatColor.DARK_GREEN))));
        cp5.addText(textComponent("\n\nThis command edits the data of the given TerrainGenerator, use /terrain edit <name> to get all the available values to edit", ChatColor.BLUE));
        BookPage cp6 = book.createPage();
        cp6.addText(textComponent("/terrain addGen <name> <name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain add <name> <name>", ChatColor.DARK_GREEN))));
        cp6.addText(textComponent("\n\nThis command adds the second given TerrainGenerator to the first given TerrainGenerator", ChatColor.BLUE));
        BookPage cp7 = book.createPage();
        cp7.addText(textComponent("/terrain removeGen <name> <name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain remove <name> <name>", ChatColor.DARK_GREEN))));
        cp7.addText(textComponent("\n\nThis command removes the second given TerrainGenerator from the first given TerrainGenerator", ChatColor.BLUE));
        BookPage cp8 = book.createPage();
        cp8.addText(textComponent("/terrain setGen <name> <name> <number>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain set <name> <name> <number>", ChatColor.DARK_GREEN))));
        cp8.addText(textComponent("\n\nThis command adds the second given TerrainGenerator to the first given TerrainGenerator and set it to the given number place", ChatColor.BLUE));
        BookPage cp9 = book.createPage();
        cp9.addText(textComponent("/terrain mode add <name> <TerrainMode name> [data...]", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m a <name> <TerrainMode name> [data...]", ChatColor.DARK_GREEN))));
        cp9.addText(textComponent("\n\nThis command creates a new TerrainMode and adds it to the given TerrainGenerator", ChatColor.BLUE));
        BookPage cp10 = book.createPage();
        cp10.addText(textComponent("When not given the data it is going to be its default value, this can be looked up using the command /terrain mode description <TerrainMode name>", ChatColor.BLUE));
        BookPage cp12 = book.createPage();
        cp12.addText(textComponent("/terrain mode remove <name> <TerrainMode name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m r <name> <TerrainMode name>", ChatColor.DARK_GREEN))));
        cp12.addText(textComponent("\n\nThis command removes the TerrainMode from the given TerrainGenerator", ChatColor.BLUE));
        BookPage cp13 = book.createPage();
        cp13.addText(textComponent("/terrain mode set <name> <TerrainMode name> <number> [data...]", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m s <name> <TerrainMode name> <number> [data...]", ChatColor.DARK_GREEN))));
        cp13.addText(textComponent("\n\nThis command sets the TerrainMode to the given place", ChatColor.BLUE));
        BookPage cp14 = book.createPage();
        cp14.addText(textComponent("/terrain mode copy <name> <name> <TerrainMode name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m c <name> <name> <TerrainMode name>", ChatColor.DARK_GREEN))));
        cp14.addText(textComponent("\n\nThis command copies the TerrainMode from the second given TerrainGenerator to the fist TerrainGenerator", ChatColor.BLUE));
        BookPage cp15 = book.createPage();
        cp15.addText(textComponent("/terrain mode edit <name> <TerrainMode name> add <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m e <name> <TerrainMode name> a <data...>", ChatColor.DARK_GREEN))));
        cp15.addText(textComponent("\n\nThis command adds data to the TerrainMode, this command can only be used when the given TerrainMode is MapBased or ArrayBased", ChatColor.BLUE));
        BookPage cp16 = book.createPage();
        cp16.addText(textComponent("/terrain mode edit <name> <TerrainMode name> remove <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m e <name> <TerrainMode name> e <data...>", ChatColor.DARK_GREEN))));
        cp16.addText(textComponent("\n\nThis command removes data of the TerrainMode, this command can only be used when the given TerrainMode is MapBased or ArrayBased", ChatColor.BLUE));
        BookPage cp17 = book.createPage();
        cp17.addText(textComponent("/terrain mode edit <name> <TerrainMode name> set <number> <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m e <name> <TerrainMode name> s <number> <data...>", ChatColor.DARK_GREEN))));
        cp17.addText(textComponent("\n\nThis command adds the given data to the TerrainMode and set it to the given place," +
                " this command can only be used when the given TerrainMode is MapBased or ArrayBased", ChatColor.BLUE));
        BookPage cp18 = book.createPage();
        cp18.addText(textComponent("/terrain mode edit <name> <TerrainMode name> set <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m e <name> <TerrainMode name> s <data...>", ChatColor.DARK_GREEN))));
        cp18.addText(textComponent("\n\nThis command sets the TerrainMode data to the given data, this command can only be used when the given TerrainMode is DataBased", ChatColor.BLUE));
        BookPage cp19 = book.createPage();
        cp19.addText(textComponent("/terrain mode getData <name> <TerrainMode name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m d <name> <TerrainMode name>", ChatColor.DARK_GREEN))));
        cp19.addText(textComponent("\n\nThis command sends the you the TerrainMode data of the given data", ChatColor.BLUE));
        BookPage cp20 = book.createPage();
        cp20.addText(textComponent("/terrain mode description <TerrainMode name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m des <TerrainMode name>", ChatColor.DARK_GREEN))));
        cp20.addText(textComponent("\n\nThis command gives you a description of what the TerrainMode does," +
                " if it a FinalRender, if its ModeType (DataBased, MapBased or ArrayBased) and its default value", ChatColor.BLUE));
        BookPage cp21 = book.createPage();
        cp21.addText(textComponent("/terrain mode list", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m l", ChatColor.DARK_GREEN))));
        cp21.addText(textComponent("\n\nThis command sends you a list of available TerrainModes", ChatColor.BLUE));
        cp21.addText(textComponent("\n\n/terrain example <example name>", ChatColor.DARK_AQUA));
        cp21.addText(textComponent("\n\nThis command generates some examples for some of it uses", ChatColor.BLUE));
        BookPage cp22 = book.createPage();
        cp22.addText(textComponent("/terrain help", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain h", ChatColor.DARK_GREEN))));
        cp22.addText(textComponent("\n\nThis command opens this book, and /terrain does the same", ChatColor.BLUE));

        BookPage aP1 = book.createPage();
        aP1.addText(textComponent("About the author", ChatColor.DARK_AQUA));
        aP1.addText(textComponent("\n\nMy name is The_Spaceman, and I made this plugin as a project because when I wanted to make something I had to create a nice world or create one by hand," +
                "I hope you like it. Source code can be found at my ", ChatColor.BLUE));
        aP1.addText(textComponent("GitHub", ChatColor.BLACK, ClickEvent.openUrl("https://github.com/JasperBouwman"),
                hoverEvent(textComponent("https://github.com/JasperBouwman", ChatColor.DARK_GREEN))));
        aP1.addText(textComponent("\naccount", ChatColor.BLUE));

        BookPage mCP1 = book.createPage();
        mCP1.addText(textComponent("Making your own TerrainMode", ChatColor.DARK_AQUA));

        HoverEvent hEvent1 = hoverEvent("");
        hEvent1.addText(textComponent("First you create a new class, for an example I will use the name TMode and it will extend DataBased and using Integers." +
                " Let your class extend a TerrainModeType, you can choose between DataBased, MapBased or ArrayBased." +
                " So now you have: 'public class TMode extends TerrainMode.DataBased<Integer>'", ChatColor.BLUE));

        mCP1.addText(textComponent("\n\nStep 1", ChatColor.BLUE, hEvent1));
        mCP1.addText(textComponent("\nStep 2", ChatColor.BLUE, hoverEvent(textComponent("Implement all the methods. Let getModeName() return your TerrainMode name, " +
                "I will use 'TMode'. This name has to be unique or it wont be added to the TerrainModes list", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 3", ChatColor.BLUE, hoverEvent(textComponent("Let isFinalMode() return true or false, when false it will be used when the owning TerrainGenerator" +
                "is done generating. When true it will be used when all TerrainGenerators are done", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 4", ChatColor.BLUE, hoverEvent(textComponent("Let 1getModeDescription() return a description of your TerrainMode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 5", ChatColor.BLUE, hoverEvent(textComponent("setData(), addData() and removeData() will be used when a player wants to edit your TerrainMode," +
                "so be careful when making these. These has to work correctly to use it. Make sure that you send the player how to edit your TerrainMode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 6", ChatColor.BLUE, hoverEvent(textComponent("saveMode() saves the TerrainMode to the dataFile, to get the file use this:" +
                " 'Files terrainData = getFiles(\\\\\\\"terrainData\\\\\\\");'. in TerrainUtils are some pre written methods that saves your TerrainMode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 7", ChatColor.BLUE, hoverEvent(textComponent("getMode() should retrieve the data from the dataFile and sets the TerrainModeData to it. Again there are" +
                " some methods that will get the TerrainMode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 8", ChatColor.BLUE, hoverEvent(textComponent("useMode() is the method that used the TerrainGenerator to use the mode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 8", ChatColor.BLUE, hoverEvent(textComponent("and finally register the mode in your main class in onEnable(). To register:" +
                "\\\\\\\"com.spaceman.terrainGenerator.terrain.TerrainMode.registerMode(new TMode());\\\\\\\", where you put in your own TerrainMode", ChatColor.BLUE))));

        book.openBook(player);
    }
}
