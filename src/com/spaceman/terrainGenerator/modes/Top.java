package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.*;

public class Top extends TerrainMode.DataBased<TerrainBlockData> {

    public Top() {
        setModeData(new TerrainBlockData(Material.GRASS));
    }

    @Override
    public void saveMode(String savePath) {
        TerrainUtils.saveDataTerrainBlockData(savePath, getModeData());
    }

    @Override
    public DataBased getMode(String savePath, DataBased templateMode) {
        return TerrainUtils.getDataTerrainBlockData(savePath, templateMode);
    }

    @Override
    public boolean isFinalMode() {
        return false;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that will chance the top layer to given material";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setData(LinkedList<String> data, Player player) {
        if (data != null) {//todo add directional
            if (data.size() > 0) {
                TerrainBlockData is = getModeData();
                if (is == null) {
                    is = new TerrainBlockData(Material.GRASS);
                }
                if (data.get(0).toLowerCase().startsWith("material=")) {
                    Material m = Material.getMaterial(data.get(0).toLowerCase().replace("material=", ""));
                    if (m == null) {
                        player.sendMessage(ChatColor.RED + "Given material is not a valid material");
                        return;
                    }
                    is.setMaterial(m);
                } else {
                    player.sendMessage(ChatColor.RED + "Given data '" + data.get(0) + "' is not valid data, it must be 'material=STONE', where behind the = can be your value");
                    return;
                }
                player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + getModeName() + " is now set to " + is.toString());
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }

    @Override
    public String getModeName() {
        return "top";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                            TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        int highest = getHighest(x, z, genStorage);
        ArrayList<Integer> airPockets = getAirPockets(x, z, genStorage, genData.getStartGen(), highest);

        if (genData.getHeightGen() == highest && genData.getHeightGen() > 0 || airPockets.contains(genData.getHeightGen() + 1)) {

            if (!getModeData().getMaterial().equals(Material.STRUCTURE_VOID)) {
                if (genData.getStartGen() < genData.getHeightGen()) {
                    if (chunkData != null) {
                        chunkData.setBlock(genData.getHeightGen(), getModeData().getMaterial());
                    } else {
                        setType(new Location(locData.getWorld(), x, genData.getHeightGen(), z).getBlock(), getModeData().getMaterial(), getModeData().getBlockFace());
                    }
                }
            }
        }
    }
}

