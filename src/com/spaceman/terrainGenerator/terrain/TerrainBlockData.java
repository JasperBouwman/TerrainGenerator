package com.spaceman.terrainGenerator.terrain;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static com.spaceman.terrainGenerator.ColorFormatter.formatError;

public class TerrainBlockData implements ConfigurationSerializable {
    private Material material = Material.DIRT;
    private BlockFace blockFace = BlockFace.NORTH;
    private Boolean waterLogged = null;
    
    public TerrainBlockData(Material material) {
        this.material = material;
    }
    
    public TerrainBlockData(Material material, BlockFace blockFace, Boolean waterLogged) {
        this.material = material;
        this.blockFace = blockFace;
        this.waterLogged = waterLogged;
    }
    
    public TerrainBlockData() {
    }
    
    public TerrainBlockData(TerrainBlockData blockData) {
        this.material = blockData.getMaterial();
        this.blockFace = blockData.getBlockFace();
        this.waterLogged = blockData.isWaterLogged();
    }
    
    public static TerrainBlockData fromString(String s) {
        //m=GRASS,d=NORTH,w=true or m=STONE,d=EAST or m=DIRT
        
        if (s.startsWith("m=") || s.startsWith("M=")) {
            String a = s.split("[Mm]=")[1];
            String materialString = a;
            String blockFaceString = "NORTH";
            Boolean waterLogged = null;
            
            if (a.contains(",d=") || a.contains(",D=")) {
                materialString = a.split(",[Dd]=")[0];
                blockFaceString = a.split(",[dD]=")[1];
                if (a.contains(",w=") || a.contains(",W=")) {
                    if (!a.split(",[Ww]=")[1].equalsIgnoreCase("null")) {
                        waterLogged = Boolean.valueOf(a.split(",[Ww]=")[1]);
                    }
                }
            }
            
            Material m = Material.matchMaterial(materialString);
            if (m == null) {
                throw new IllegalArgumentException(materialString + " is not a valid Material");
            }
            BlockFace b;
            try {
                b = BlockFace.valueOf(blockFaceString.toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException(formatError("%s is not a valid direction", blockFaceString));
            }
            return new TerrainBlockData(m, b, waterLogged);
        }
        return null;
    }
    
    public static TerrainBlockData deserialize(Map<String, Object> args) {
        TerrainBlockData terrainBlockData = new TerrainBlockData(Material.DIRT, BlockFace.NORTH, null);
        if (args.containsKey("material")) {
            terrainBlockData.setMaterial(Material.matchMaterial(args.get("material").toString()));
        }
        if (args.containsKey("blockFace")) {
            terrainBlockData.setBlockFace(BlockFace.valueOf((String) args.get("blockFace")));
        }
        if (args.containsKey("waterLogged")) {
            terrainBlockData.setWaterLogged(Boolean.valueOf(args.get("waterLogged").toString()));
        }
        return terrainBlockData;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TerrainBlockData) {
            TerrainBlockData otherTerrainBlockData = (TerrainBlockData) obj;
            return this.getMaterial() == otherTerrainBlockData.getMaterial() &&
                    this.blockFace == otherTerrainBlockData.getBlockFace();
        }
        return false;
    }
    
    public boolean equalsExact(Object obj) {
        if (obj instanceof TerrainBlockData) {
            TerrainBlockData otherTerrainBlockData = (TerrainBlockData) obj;
            return this.getMaterial() == otherTerrainBlockData.getMaterial() &&
                    this.blockFace == otherTerrainBlockData.getBlockFace() &&
                    this.waterLogged == otherTerrainBlockData.isWaterLogged();
        }
        return false;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public void setMaterial(@Nonnull Material material) {
        this.material = material;
    }
    
    public BlockFace getBlockFace() {
        return blockFace;
    }
    
    public void setBlockFace(@Nonnull BlockFace blockFace) {
        this.blockFace = blockFace;
    }
    
    public Boolean isWaterLogged() {
        return waterLogged;
    }
    
    public void setWaterLogged(Boolean waterLogged) {
        this.waterLogged = waterLogged;
    }
    
    public String toString() {
        return "Material: " + material +
                ", Direction: " + blockFace +
                ", WaterLogged: " + waterLogged;
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("material", material.name());
        map.put("blockFace", blockFace.name());
        if (waterLogged != null) {
            map.put("waterLogged", waterLogged);
        }
        return map;
    }
}