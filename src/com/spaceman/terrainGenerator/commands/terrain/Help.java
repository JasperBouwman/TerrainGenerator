package com.spaceman.terrainGenerator.commands.terrain;

import com.spaceman.terrainGenerator.commandHander.SubCommand;
import com.spaceman.terrainGenerator.fancyMessage.book.Book;
import com.spaceman.terrainGenerator.fancyMessage.book.BookPage;
import com.spaceman.terrainGenerator.fancyMessage.events.ClickEvent;
import com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.BOOK_APOSTROPHE;
import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;

public class Help extends SubCommand {
    
    @Override
    public String getName(String arg) {
        return "helpOld";
    }
    
    @Override
    public void run(String[] args, Player player) {
        //terrain helpOld
        
        Book book = new Book("TerrainGenerator", "The_Spaceman");
        
        BookPage page = book.createPage(textComponent("TerrainGenerator", ChatColor.BLUE));
        BookPage indexP1 = book.createPage();
        BookPage introP1 = book.createPage();
        BookPage introP2 = book.createPage();
        BookPage introP3 = book.createPage();
        BookPage introP4 = book.createPage();
        BookPage introP5 = book.createPage();
        BookPage introP6 = book.createPage();
        BookPage commandPage1 = book.createPage();
        BookPage commandPage2 = book.createPage();
        BookPage commandPage3 = book.createPage();
        BookPage commandPage4 = book.createPage();
        BookPage commandPage5 = book.createPage();
        BookPage commandPage6 = book.createPage();
        BookPage cp1 = book.createPage();
        BookPage cp2 = book.createPage();
        BookPage cp3 = book.createPage();
        BookPage cp4 = book.createPage();
        BookPage cp5 = book.createPage();
        BookPage cp6 = book.createPage();
        BookPage cp7 = book.createPage();
        BookPage cp8 = book.createPage();
        BookPage cp9 = book.createPage();
        BookPage cp10 = book.createPage();
        BookPage cp11 = book.createPage();
        BookPage cp12 = book.createPage();
        BookPage cp13 = book.createPage();
        BookPage cp14 = book.createPage();
        BookPage cp15 = book.createPage();
        BookPage cp16 = book.createPage();
        BookPage cp17 = book.createPage();
        BookPage cp18 = book.createPage();
        BookPage cp19 = book.createPage();
        BookPage cp20 = book.createPage();
        BookPage cp21 = book.createPage();
        BookPage cp22 = book.createPage();
        BookPage cp23 = book.createPage();
        BookPage cp24 = book.createPage();
        BookPage aP1 = book.createPage();
        BookPage mCP1 = book.createPage();
        
        page.addText(textComponent("\n\n\nIn this book you can find a description of the plugin and all of it usages and commands", ChatColor.DARK_AQUA));
        
        HoverEvent commandTitleHoverEvent = hoverEvent("");
        commandTitleHoverEvent.addText(textComponent("Command arguments between ", ChatColor.BLUE));
        commandTitleHoverEvent.addText(textComponent("<>", ChatColor.DARK_AQUA));
        commandTitleHoverEvent.addText(textComponent(" are necessary, between ", ChatColor.BLUE));
        commandTitleHoverEvent.addText(textComponent("[]", ChatColor.DARK_AQUA));
        commandTitleHoverEvent.addText(textComponent(" are optional and '", ChatColor.BLUE));
        commandTitleHoverEvent.addText(textComponent("...", ChatColor.DARK_AQUA));
        commandTitleHoverEvent.addText(textComponent("' means that you can use more arguments for your command", ChatColor.BLUE));

        indexP1.addText(textComponent("Index", ChatColor.DARK_AQUA));
        indexP1.addText(textComponent("\n\nAbout the plugin", ChatColor.BLUE, ClickEvent.changePage(introP1.getPageNumber()), hoverEvent(textComponent("page: " + introP1.getPageNumber(), ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nAbout TerrainModes", ChatColor.BLUE, ClickEvent.changePage(introP3.getPageNumber()), hoverEvent(textComponent("page: " + introP3.getPageNumber(), ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nAbout TerrainWorlds", ChatColor.BLUE, ClickEvent.changePage(introP6.getPageNumber()), hoverEvent(textComponent("page: " + introP6.getPageNumber(), ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nCommands", ChatColor.BLUE, ClickEvent.changePage(commandPage1.getPageNumber()), hoverEvent(textComponent("page: " + commandPage1.getPageNumber(), ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nAbout the author", ChatColor.BLUE, ClickEvent.changePage(aP1.getPageNumber()), hoverEvent(textComponent("page: " + aP1.getPageNumber(), ChatColor.DARK_GREEN))));
        indexP1.addText(textComponent("\nMaking your own TerrainMode", ChatColor.BLUE, ClickEvent.changePage(mCP1.getPageNumber()), hoverEvent(textComponent("page: " + mCP1.getPageNumber(), ChatColor.DARK_GREEN))));

        introP1.addText(textComponent("About the plugin", ChatColor.DARK_AQUA));
        introP1.addText(textComponent("\n\nThis plugin generates custom made terrain", ChatColor.BLUE));
        introP1.addText(textComponent("\nYou can change all the data of a TerrainGenerator, you can even add multiple TerrainGenerators to a single TerrainGenerator. How this works is:" +
                " When generating it will first generate the selected", ChatColor.BLUE));

        introP2.addText(textComponent("TerrainGenerator, when it is done with that it will go on to the next one. The new TerrainGenerator won't override the" +
                " previous generated TerrainGenerators, this can be used to generate multiple mountains or a flat landscape surrounded by a mountain landscape", ChatColor.BLUE));
        
        introP3.addText(textComponent("About TerrainModes", ChatColor.DARK_AQUA));
        introP3.addText(textComponent("\n\nA TerrainMode is a extra setting that you can include/exclude in the TerrainGenerator. These TerrainModes can be made separably from this plugin." +
                " What a TerrainMode does depends on the TerrainMode. What it can do it change the ", ChatColor.BLUE));
        
        introP4.addText(textComponent("generated outcome of the owning TerrainGenerator (hover ", ChatColor.BLUE));
        introP4.addText(textComponent("here", ChatColor.DARK_GREEN, hoverEvent(textComponent("layers, top, topR, stoneGen, etc", ChatColor.DARK_GREEN))));
        introP4.addText(textComponent(" for some examples). It can also place some blocks on top of the TerrainGenerator (hover ", ChatColor.BLUE));
        introP4.addText(textComponent("here", ChatColor.DARK_GREEN, hoverEvent(textComponent("addWater, addLava, addGrass, etc", ChatColor.DARK_GREEN))));
        introP4.addText(textComponent(" for some examples),", ChatColor.BLUE));

        introP5.addText(textComponent("when some can place whole features to make it look populated (hover ", ChatColor.BLUE));
        introP5.addText(textComponent("here", ChatColor.DARK_GREEN, hoverEvent(textComponent("addHouse, addTrees", ChatColor.DARK_GREEN))));
        introP5.addText(textComponent(" for some examples), there are lots of ways to use and make a TerrainGenerator.\nIt is possible to make your own TerrainMode click ", ChatColor.BLUE));
        introP5.addText(textComponent("here", ChatColor.DARK_GREEN, ClickEvent.changePage(mCP1.getPageNumber()),
                hoverEvent(textComponent("page: " + mCP1.getPageNumber(), ChatColor.DARK_GREEN))));
        introP5.addText(textComponent(" to go to the page", ChatColor.BLUE));

        introP6.addText(textComponent("About TerrainWorlds", ChatColor.DARK_AQUA));
        introP6.addText(textComponent("\n\nA TerrainWorld is a custom generated world using a TerrainGenerator as ChunkGenerator. it behaves as normal, and it is possible to change a TerrainGenerator" +
                " while a TerrainWorld is loaded", ChatColor.BLUE));

        commandPage1.addText(textComponent("Commands:", ChatColor.DARK_AQUA, commandTitleHoverEvent));
        commandPage1.addText(textComponent("\n/terrain", ChatColor.BLUE,
                ClickEvent.changePage(cp21.getPageNumber()), hoverEvent(textComponent("page: " + cp21.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain create <name> [data...]", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp1.getPageNumber()), hoverEvent(textComponent("page: " + cp1.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain delete <name>", ChatColor.BLUE,
                ClickEvent.changePage(cp2.getPageNumber()), hoverEvent(textComponent("page: "+cp2.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain getData <name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp2.getPageNumber()), hoverEvent(textComponent("page: "+cp2.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain generate <name> <x> <z>", ChatColor.BLUE,
                ClickEvent.changePage(cp3.getPageNumber()), hoverEvent(textComponent("page: "+cp3.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain list", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp4.getPageNumber()), hoverEvent(textComponent("page: "+cp4.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage1.addText(textComponent("\n/terrain edit <name> <data...>", ChatColor.BLUE,
                ClickEvent.changePage(cp5.getPageNumber()), hoverEvent(textComponent("page: " + cp5.getPageNumber(), ChatColor.DARK_GREEN))));

        commandPage2.addText(textComponent("Commands:", ChatColor.DARK_AQUA, commandTitleHoverEvent));
        commandPage2.addText(textComponent("\n/terrain addGen <name> <name>", ChatColor.BLUE,
                ClickEvent.changePage(cp6.getPageNumber()), hoverEvent(textComponent("page: "+cp6.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage2.addText(textComponent("\n/terrain removeGen <name> <name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp7.getPageNumber()), hoverEvent(textComponent("page: "+cp7.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage2.addText(textComponent("\n/terrain setGen <name> <name> <number>", ChatColor.BLUE,
                ClickEvent.changePage(cp8.getPageNumber()), hoverEvent(textComponent("page: "+cp8.getPageNumber(), ChatColor.DARK_GREEN))));

        commandPage3.addText(textComponent("Commands:", ChatColor.DARK_AQUA, commandTitleHoverEvent));
        commandPage3.addText(textComponent("\n/terrain mode add <name> <TerrainMode name>", ChatColor.BLUE,
                ClickEvent.changePage(cp9.getPageNumber()), hoverEvent(textComponent("page: "+cp9.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage3.addText(textComponent("\n/terrain mode remove <name> <TerrainMode name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp11.getPageNumber()), hoverEvent(textComponent("page: "+cp11.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage3.addText(textComponent("\n/terrain mode set <name> <TerrainMode name> <number>", ChatColor.BLUE,
                ClickEvent.changePage(cp12.getPageNumber()), hoverEvent(textComponent("page: "+cp12.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage3.addText(textComponent("\n/terrain mode copy <name> <name> <TerrainMode name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp13.getPageNumber()), hoverEvent(textComponent("page: "+cp13.getPageNumber(), ChatColor.DARK_GREEN))));

        commandPage4.addText(textComponent("Commands:", ChatColor.DARK_AQUA, commandTitleHoverEvent));
        commandPage4.addText(textComponent("\n/terrain mode edit <name> <TerrainMode name> add <data...>", ChatColor.BLUE,
                ClickEvent.changePage(cp14.getPageNumber()), hoverEvent(textComponent("page: "+cp14.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage4.addText(textComponent("\n/terrain mode edit <name> <TerrainMode name> remove <data...>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp15.getPageNumber()), hoverEvent(textComponent("page: "+cp15.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage4.addText(textComponent("\n/terrain mode edit <name> <TerrainMode name> set <number> <data...>", ChatColor.BLUE,
                ClickEvent.changePage(cp16.getPageNumber()), hoverEvent(textComponent("page: "+cp16.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage4.addText(textComponent("\n/terrain mode edit <name> <TerrainMode name> set <data...>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp17.getPageNumber()), hoverEvent(textComponent("page: "+cp17.getPageNumber(), ChatColor.DARK_GREEN))));

        commandPage5.addText(textComponent("Commands:", ChatColor.DARK_AQUA, commandTitleHoverEvent));
        commandPage5.addText(textComponent("\n/terrain mode getData <name> <TerrainMode name>", ChatColor.BLUE,
                ClickEvent.changePage(cp18.getPageNumber()), hoverEvent(textComponent("page: "+cp18.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage5.addText(textComponent("\n/terrain mode description <TerrainMode name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp19.getPageNumber()), hoverEvent(textComponent("page: "+cp19.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage5.addText(textComponent("\n/terrain mode list", ChatColor.BLUE,
                ClickEvent.changePage(cp20.getPageNumber()), hoverEvent(textComponent("page: "+cp20.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage5.addText(textComponent("\n/terrain example <example name> <x> <z>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp20.getPageNumber()), hoverEvent(textComponent("page: "+cp20.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage5.addText(textComponent("\n/terrain help", ChatColor.BLUE,
                ClickEvent.changePage(cp21.getPageNumber()), hoverEvent(textComponent("page: "+cp21.getPageNumber(), ChatColor.DARK_GREEN))));

        commandPage6.addText(textComponent("Commands:", ChatColor.DARK_AQUA, commandTitleHoverEvent));
        commandPage6.addText(textComponent("\n/terrain world list", ChatColor.BLUE,
                ClickEvent.changePage(cp22.getPageNumber()), hoverEvent(textComponent("page: "+cp22.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage6.addText(textComponent("\n/terrain world create <world name> <TerrainGenerator>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp22.getPageNumber()), hoverEvent(textComponent("page: "+cp22.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage6.addText(textComponent("\n/terrain world delete <TerrainWorld name>", ChatColor.BLUE,
                ClickEvent.changePage(cp23.getPageNumber()), hoverEvent(textComponent("page: "+cp23.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage6.addText(textComponent("\n/terrain world tp <world name>", ChatColor.DARK_GREEN,
                ClickEvent.changePage(cp23.getPageNumber()), hoverEvent(textComponent("page: "+cp23.getPageNumber(), ChatColor.DARK_GREEN))));
        commandPage6.addText(textComponent("\n/terrain safeToEdit [TerrainGenerator]", ChatColor.BLUE,
                ClickEvent.changePage(cp24.getPageNumber()), hoverEvent(textComponent("page: "+cp24.getPageNumber(), ChatColor.DARK_GREEN))));


        cp1.addText(textComponent("/terrain create <name> [data...]", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain c <name> [data...]", ChatColor.DARK_GREEN))));
        cp1.addText(textComponent("\n\nThis command creates a new TerrainGenerator. The data arg is optional," +
                " when not given the data it is going to be its default value", ChatColor.BLUE));

        cp2.addText(textComponent("/terrain delete <name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain del <name>", ChatColor.DARK_GREEN))));
        cp2.addText(textComponent("\n\nThis command deletes a TerrainGenerator", ChatColor.BLUE));
        cp2.addText(textComponent("\n\n/terrain getData <name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain d <name>", ChatColor.DARK_GREEN))));
        cp2.addText(textComponent("\n\nThis command sends the TerrainGenerator data to the player", ChatColor.BLUE));

        cp3.addText(textComponent("/terrain generate <name> <x> <z>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain g <name> <x> <z>", ChatColor.DARK_GREEN))));
        cp3.addText(textComponent("\n\nThis command generated the TerrainGenerator. When using large numbers (x/z values) it can take a while to process it", ChatColor.BLUE));

        cp4.addText(textComponent("When the server stops responding you can reload the server if possible, this stops generating the TerrainGenerator", ChatColor.BLUE));
        cp4.addText(textComponent("\n\n/terrain list", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain l", ChatColor.DARK_GREEN))));
        cp4.addText(textComponent("\n\nThis command sends the player all the available TerrainGenerators", ChatColor.BLUE));

        cp5.addText(textComponent("/terrain edit <name> <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain e <data...>", ChatColor.DARK_GREEN))));
        cp5.addText(textComponent("\n\nThis command edits the data of the given TerrainGenerator, use /terrain edit <name> to get all the available values to edit", ChatColor.BLUE));

        cp6.addText(textComponent("/terrain addGen <name> <name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain add <name> <name>", ChatColor.DARK_GREEN))));
        cp6.addText(textComponent("\n\nThis command adds the second given TerrainGenerator to the first given TerrainGenerator", ChatColor.BLUE));

        cp7.addText(textComponent("/terrain removeGen <name> <name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain remove <name> <name>", ChatColor.DARK_GREEN))));
        cp7.addText(textComponent("\n\nThis command removes the second given TerrainGenerator from the first given TerrainGenerator", ChatColor.BLUE));

        cp8.addText(textComponent("/terrain setGen <name> <name> <number>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain set <name> <name> <number>", ChatColor.DARK_GREEN))));
        cp8.addText(textComponent("\n\nThis command adds the second given TerrainGenerator to the first given TerrainGenerator and set it to the given number place", ChatColor.BLUE));

        cp9.addText(textComponent("/terrain mode add <name> <TerrainMode name> [data...]", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m a <name> <TerrainMode name> [data...]", ChatColor.DARK_GREEN))));
        cp9.addText(textComponent("\n\nThis command creates a new TerrainMode and adds it to the given TerrainGenerator", ChatColor.BLUE));

        cp10.addText(textComponent("When not given the data it is going to be its default value, this can be looked up using the command /terrain mode description <TerrainMode name>", ChatColor.BLUE));

        cp11.addText(textComponent("/terrain mode remove <name> <TerrainMode name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m r <name> <TerrainMode name>", ChatColor.DARK_GREEN))));
        cp11.addText(textComponent("\n\nThis command removes the TerrainMode from the given TerrainGenerator", ChatColor.BLUE));

        cp12.addText(textComponent("/terrain mode set <name> <TerrainMode name> <number> [data...]", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m s <name> <TerrainMode name> <number> [data...]", ChatColor.DARK_GREEN))));
        cp12.addText(textComponent("\n\nThis command sets the TerrainMode to the given place", ChatColor.BLUE));

        cp13.addText(textComponent("/terrain mode copy <name> <name> <TerrainMode name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m c <name> <name> <TerrainMode name>", ChatColor.DARK_GREEN))));
        cp13.addText(textComponent("\n\nThis command copies the TerrainMode from the second given TerrainGenerator to the fist TerrainGenerator", ChatColor.BLUE));

        cp14.addText(textComponent("/terrain mode edit <name> <TerrainMode name> add <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m e <name> <TerrainMode name> a <data...>", ChatColor.DARK_GREEN))));
        cp14.addText(textComponent("\n\nThis command adds data to the TerrainMode, this command can only be used when the given TerrainMode is a MapMode or an ArrayMode", ChatColor.BLUE));

        cp15.addText(textComponent("/terrain mode edit <name> <TerrainMode name> remove <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m e <name> <TerrainMode name> e <data...>", ChatColor.DARK_GREEN))));
        cp15.addText(textComponent("\n\nThis command removes data of the TerrainMode, this command can only be used when the given TerrainMode is a MapMode or ArrayMode", ChatColor.BLUE));

        cp16.addText(textComponent("/terrain mode edit <name> <TerrainMode name> set <number> <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m e <name> <TerrainMode name> s <number> <data...>", ChatColor.DARK_GREEN))));
        cp16.addText(textComponent("\n\nThis command adds the given data to the TerrainMode and set it to the given place," +
                " this command can only be used when the given TerrainMode is a MapMode or ArrayMode", ChatColor.BLUE));

        cp17.addText(textComponent("/terrain mode edit <name> <TerrainMode name> set <data...>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m e <name> <TerrainMode name> s <data...>", ChatColor.DARK_GREEN))));
        cp17.addText(textComponent("\n\nThis command sets the TerrainMode data to the given data, this command can only be used when the given TerrainMode is DataMode", ChatColor.BLUE));

        cp18.addText(textComponent("/terrain mode getData <name> <TerrainMode name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m d <name> <TerrainMode name>", ChatColor.DARK_GREEN))));
        cp18.addText(textComponent("\n\nThis command sends the you the TerrainMode data of the given data", ChatColor.BLUE));

        cp19.addText(textComponent("/terrain mode description <TerrainMode name>", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m des <TerrainMode name>", ChatColor.DARK_GREEN))));
        cp19.addText(textComponent("\n\nThis command gives you a description of what the TerrainMode does," +
                " if it a FinalRender, if its ModeType (DataMode, MapMode or ArrayMode) and its default value", ChatColor.BLUE));

        cp20.addText(textComponent("/terrain mode list", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain m l", ChatColor.DARK_GREEN))));
        cp20.addText(textComponent("\n\nThis command sends you a list of available TerrainModes", ChatColor.BLUE));
        cp20.addText(textComponent("\n\n/terrain example <example name>", ChatColor.DARK_AQUA));
        cp20.addText(textComponent("\n\nThis command generates some examples for some of it uses", ChatColor.BLUE));

        cp21.addText(textComponent("/terrain help", ChatColor.DARK_AQUA, hoverEvent(textComponent("/terrain h", ChatColor.DARK_GREEN))));
        cp21.addText(textComponent("\n\nThis command opens this book, and /terrain does the same", ChatColor.BLUE));

        cp22.addText(textComponent("/terrain world list", ChatColor.DARK_AQUA));
        cp22.addText(textComponent("\n\nGet a list of TerrainWorlds", ChatColor.BLUE));
        cp22.addText(textComponent("\n\n/terrain world create <world name> <TerrainGenerator>", ChatColor.DARK_AQUA));
        cp22.addText(textComponent("\n\nCreate a new TerrainWorld with a given TerrainGenerator as worldGenerator", ChatColor.BLUE));

        cp23.addText(textComponent("/terrain world delete <TerrainWorld name>", ChatColor.DARK_AQUA));
        cp23.addText(textComponent("\n\nUnloads the given TerrainWorld and tries to deletes it", ChatColor.BLUE));
        cp23.addText(textComponent("\n\n/terrain world tp <world name>", ChatColor.DARK_AQUA));
        cp23.addText(textComponent("\n\nTeleports you to the spawn location of the given world", ChatColor.BLUE));

        cp24.addText(textComponent("/terrain SafeToEdit [TerrainGenerator]", ChatColor.DARK_AQUA));
        cp24.addText(textComponent("\n\nShows you if a TerrainGenerator is safe to edit, when a TerrainGenerator is used in a TerrainWorld it is flagged as unsafe to edit", ChatColor.BLUE));

        aP1.addText(textComponent("About the author", ChatColor.DARK_AQUA));
        aP1.addText(textComponent("\n\nMy name is The_Spaceman, and I made this plugin as a project because when I wanted to make something I had to create a nice world or create one by hand," +
                "I hope you like it. Source code can be found at my ", ChatColor.BLUE));
        aP1.addText(textComponent("GitHub", ChatColor.BLACK, ClickEvent.openUrl("https://github.com/JasperBouwman"),
                hoverEvent(textComponent("https://github.com/JasperBouwman", ChatColor.DARK_GREEN))));
        aP1.addText(textComponent("\naccount", ChatColor.BLUE));

        mCP1.addText(textComponent("Making your own TerrainMode", ChatColor.DARK_AQUA));

        HoverEvent hEvent1 = hoverEvent("");
        hEvent1.addText(textComponent("First you create a new class, for an example I will use the name TMode and it will extend DataMode and using Integers." +
                " Let your class extend a TerrainModeType, you can choose between DataMode, MapMode or ArrayMode." +
                " So now you have: 'public class TMode extends TerrainMode.DataMode<Integer>'", ChatColor.BLUE));

        mCP1.addText(textComponent("\n\nStep 1", ChatColor.BLUE, hEvent1));
        mCP1.addText(textComponent("\nStep 2", ChatColor.BLUE, hoverEvent(textComponent("Implement all the methods. Let getModeName() return your TerrainMode name, " +
                "I will use 'TMode'. This name has to be unique or it wont be added to the TerrainModes list", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 3", ChatColor.BLUE, hoverEvent(textComponent("Let isFinalMode() return true or false, when false it will be used when the owning TerrainGenerator" +
                "is done generating. When true it will be used when all TerrainGenerators are done", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 4", ChatColor.BLUE, hoverEvent(textComponent("Let 1getModeDescription() return a description of your TerrainMode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 5", ChatColor.BLUE, hoverEvent(textComponent("setData(), addData() and removeData() will be used when a player wants to edit your TerrainMode," +
                "so be careful when making these. These has to work correctly to use it. Make sure that you send the player how to edit your TerrainMode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 6", ChatColor.BLUE, hoverEvent(textComponent("saveMode() saves the TerrainMode to the dataFile, to get the file use this:" +
                " 'Files terrainData = getFiles(" + BOOK_APOSTROPHE + "terrainData" + BOOK_APOSTROPHE + ");'. in TerrainUtils are some pre written methods that saves your TerrainMode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 7", ChatColor.BLUE, hoverEvent(textComponent("loadMode() should retrieve the data from the dataFile and sets the TerrainModeData to it. Again there are" +
                " some methods that will get the TerrainMode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 8", ChatColor.BLUE, hoverEvent(textComponent("useMode() is the method that used the TerrainGenerator to use the mode", ChatColor.BLUE))));
        mCP1.addText(textComponent("\nStep 9", ChatColor.BLUE, hoverEvent(textComponent("and finally register the mode in your main class in onEnable(). To register:" +
                BOOK_APOSTROPHE + "com.spaceman.terrainGenerator.terrain.TerrainMode.registerMode(new TMode());" + BOOK_APOSTROPHE + ", where you put in your own TerrainMode", ChatColor.BLUE))));
        
        book.openBook(player);
    }
}
