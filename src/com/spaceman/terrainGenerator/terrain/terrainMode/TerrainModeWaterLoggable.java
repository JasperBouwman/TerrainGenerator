package com.spaceman.terrainGenerator.terrain.terrainMode;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.LinkedList;

public interface TerrainModeWaterLoggable {

    void setWaterLogged(LinkedList<String> data, Player player);

    Collection<String> tabListWaterLog(String[] args, Player player);
}
