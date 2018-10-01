package com.spaceman.terrainGenerator.events;

import com.spaceman.terrainGenerator.terrain.WorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import java.util.logging.Level;

public class WorldEvents implements Listener {

    @EventHandler
    @SuppressWarnings("unused")
    public void worldInitEvent(WorldInitEvent e) {
        if (WorldGenerator.customWorlds.containsKey(e.getWorld().getName())) {
           if (!(e.getWorld().getGenerator() instanceof WorldGenerator)) {
               Bukkit.getServer().getLogger().log(Level.WARNING, "World " + e.getWorld().getName() + " is a TerrainWorld, but it does not have the TerrainGenerator as generator. Weird chunks can appear");
           }
        }
    }
}
