package com.spaceman.terrainGenerator.terrain;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TerrainBlockData implements ConfigurationSerializable {
    private Material material = Material.DIRT;
    private BlockFace blockFace = BlockFace.NORTH;

    public TerrainBlockData(Material material) {
        this.material = material;
    }

    public TerrainBlockData(Material material, BlockFace blockFace) {
        this.material = material;
        this.blockFace = blockFace;
    }

    public TerrainBlockData() {

    }
    public TerrainBlockData(TerrainBlockData blockData) {
        this.material = blockData.getMaterial();
        this.blockFace = blockData.getBlockFace();
    }

    public Material getMaterial() {
        return material;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public void setBlockFace(BlockFace blockFace) {
        this.blockFace = blockFace;
    }
    public void setBlockFace(String blockFace) {
        this.blockFace = BlockFace.valueOf(blockFace);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    public void setMaterial(String material) {
        this.material = Material.matchMaterial(material);
    }

    public String toString() {
        return "Material: " + material.toString() + ", Direction: " + blockFace.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("material", material.name());
        map.put("blockFace", blockFace.name());
        return map;
    }

    public static TerrainBlockData deserialize(Map<String, Object> args) {
        return new TerrainBlockData(Material.matchMaterial(args.get("material").toString()), BlockFace.valueOf(args.get("blockFace").toString()));
    }
}