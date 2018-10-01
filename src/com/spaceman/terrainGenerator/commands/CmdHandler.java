package com.spaceman.terrainGenerator.commands;

import org.bukkit.entity.Player;

public abstract class CmdHandler {

    public abstract String alias();

    public abstract void run(String[] args, Player player);
}
