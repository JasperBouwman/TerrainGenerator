package com.spaceman.terrainGenerator.fileHander;

import com.spaceman.terrainGenerator.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

public class Files {
    
    private final String fileName;
    
    private File configFile;
    private FileConfiguration fileConfiguration;
    
    public Files(String fileName) {
        String s = fileName + (fileName.toLowerCase().endsWith(".yml") ? "" : ".yml");
        this.fileName = s;
        this.configFile = new File(Main.getInstance().getDataFolder(), s);
    }
    
    public Files(String extraPath, String fileName) {
        String fileName1 = fileName + (fileName.toLowerCase().endsWith(".yml") ? "" : ".yml");
        this.fileName = fileName1;
        this.configFile = new File(Main.getInstance().getDataFolder() + extraPath, fileName1);
    }
    
    public Collection<String> getKeys(String path) {
        ConfigurationSection configurationSection = getConfig().getConfigurationSection(path);
        if (getConfig().contains(path) && configurationSection != null) {
            return configurationSection.getKeys(false);
        } else {
            return Collections.emptyList();
        }
    }
    
    private void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }
    
    public void saveConfig() {
        
        if (fileConfiguration != null && configFile != null) {
            try {
                getConfig().save(configFile);
                
            } catch (IOException ex) {
                Main.log(Level.SEVERE, "Could not save config to " + configFile + ", " + ex.getMessage());
            }
        }
    }
    
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            Main.getInstance().saveResource(fileName, false);
        }
    }
}