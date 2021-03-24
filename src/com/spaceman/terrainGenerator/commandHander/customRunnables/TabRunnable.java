package com.spaceman.terrainGenerator.commandHander.customRunnables;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

@FunctionalInterface
public interface TabRunnable {
    Collection<String> run(String[] args, Player player);
}
