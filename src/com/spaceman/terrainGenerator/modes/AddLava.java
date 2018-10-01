package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.*;

@SuppressWarnings("unused, WeakerAccess, unchecked")
public class AddLava extends TerrainMode.DataBased<Integer> {

    public AddLava() {
        setModeData(-1);
    }

    @Override
    public void saveMode(String savePath) {
        TerrainUtils.saveDataInteger(savePath, getModeData());
    }

    @Override
    public DataBased getMode(String savePath, DataBased templateMode) {
        return TerrainUtils.getDataInteger(savePath, templateMode);
    }

    @Override
    public String getModeName() {
        return "addLava";
    }

    @Override
    public boolean isFinalMode() {
        return true;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that adds lava lakes starting of the given TerrainMode data";
    }

    @Override
    public void setData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() >= 1) {
                try {
                    int i = Integer.parseInt(data.get(0));
                    setModeData(i);
                    player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + getModeName() + " is now set to " + i);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED + "Given data is not a valid number");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Missing data");
            }
        }
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                            TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
        ArrayList<Integer> airPockets = getAirPockets(x, z, genStorage, genData.getStartGen(), getModeData());

        if (getModeData() < 0) {
            return;
        }

        for (int y = getModeData(); y > 0; y--) {
            if (airPockets.contains(y) || getHighest(x, z, genStorage) < y) {
                if (chunkData != null) {
                    chunkData.setBlock(y, Material.LAVA);
                } else {
                    setType(new Location(locData.getWorld(), x, y, z).getBlock(), new ItemStack(Material.LAVA));
                }
            } else {
                return;
            }
        }
    }
}
