package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.terrain.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.*;

@SuppressWarnings({"unused, WeakerAccess, unchecked", "deprecation"})
public class AddHouse extends TerrainMode.DataBased<Integer> {

    public AddHouse() {
        setModeData(1);
    }

    private static int getLowestFrom(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage, int start) {
        int lowest = 0;
        ArrayList<Integer> airPockets = getAirPockets(x, z, genStorage, lowest, start);
        if (airPockets.size() > 0) {

            for (int i = start - 1; i > lowest; i--) {
                if (!airPockets.contains(i)) {
                    return i;
                }
            }
        }

        return start + 1;
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
    public void setData(LinkedList<String> data, Player player) {
        if (data != null) {
            if (data.size() >= 1) {
                try {
                    int i = Integer.parseInt(data.get(0));
                    setModeData(i);
                    player.sendMessage(ChatColor.DARK_AQUA + "TerrainMode " + getModeName() + " is now set to " + i);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED +"Given data is not a valid number");
                }
            } else {
                player.sendMessage(ChatColor.RED +"Missing data");
            }
        }
    }

    @Override
    public String getModeName() {
        return "addHouse";
    }

    @Override
    public boolean isFinalMode() {
        return true;
    }

    @Override
    public String getModeDescription() {
        return getModeName() + " is a TerrainMode that adds houses";
    }

    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                            TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        if (getModeData() > new Random().nextInt(10000)) {

            int startHouse = 0;
            int heightHouse = 300;

            if (x + 4 > locData.getStartL().getBlockX() + locData.getX() ||
                    x - 5 < locData.getStartL().getBlockX() ||
                    z + 4 > locData.getStartL().getBlockZ() + locData.getZ() ||
                    z - 4 < locData.getStartL().getBlockZ()
                    ) {
                return;
            }

            ArrayList<String> houseList;
            if (genModeData.containsKey(getModeName() + ";" + x + ";" + z)) {
                if (genModeData.get(getModeName() + ";" + x + ";" + z) instanceof ArrayList) {
                    houseList = (ArrayList<String>) genModeData.get(getModeName() + ";" + x + ";" + z);
                } else {
                    return;
                }
            } else {
                houseList = new ArrayList<>();
            }

            for (int i = x - 5; i < x + 4; i++) {
                for (int j = z - 5; j < z + 4; j++) {

                    if (houseList.contains(i + ";" + j)) {
                        return;
                    } else {
                        houseList.add(i + ";" + j);
                    }

                    TerrainGenerator.GenData genData = getGenData(i, j, data.getName() + savePath, genStorage);
                    int highest = getHighest(i, j, genStorage);
                    ArrayList<Integer> airPockets = getAirPockets(i, j, genStorage, genData.getHeightGen(), highest);

                    if (airPockets.size() > 0) {

                        int tmpI = airPockets.get(0);
                        int tmpStart = tmpI;
                        airPockets.remove(Integer.valueOf(tmpI));

                        for (int k : airPockets) {
                            if (k - 1 == tmpI) {
                                tmpI = k;
                            }
                        }

                        if (tmpI < heightHouse) {
                            heightHouse = tmpI;
                        }
                        if (tmpStart > startHouse) {
                            startHouse = tmpStart;
                        }

                    } else {
                        if (genData.getHeightGen() == highest) {

                            if (startHouse < genData.getHeightGen()) {
                                startHouse = genData.getHeightGen();
                            }

                        } else {
                            return;
                        }
                    }

                }
            }

            startHouse += 1;

            if (heightHouse - startHouse > 5 /*where 5 is min height of house*/) {

                for (int i = x - 5; i < x + 4; i++) {
                    for (int j = z - 5; j < z + 4; j++) {
                        setType(new Location(locData.getWorld(), i, startHouse, j).getBlock(), new ItemStack(Material.COBBLESTONE));
                    }
                }

                for (int i = startHouse - 1; i > getLowestFrom(x - 5, z - 5, genStorage, startHouse); i--) {
                    setType(new Location(locData.getWorld(), x - 5, i, z - 5).getBlock(), new ItemStack(Material.BIRCH_FENCE));
                }
                for (int i = startHouse - 1; i > getLowestFrom(x - 5, z + 3, genStorage, startHouse); i--) {
                    setType(new Location(locData.getWorld(), x - 5, i, z + 3).getBlock(), new ItemStack(Material.BIRCH_FENCE));
                }
                for (int i = startHouse - 1; i > getLowestFrom(x + 3, z - 5, genStorage, startHouse); i--) {
                    setType(new Location(locData.getWorld(), x + 3, i, z - 5).getBlock(), new ItemStack(Material.BIRCH_FENCE));
                }
                for (int i = startHouse - 1; i > getLowestFrom(x + 3, z + 3, genStorage, startHouse); i--) {
                    setType(new Location(locData.getWorld(), x + 3, i, z + 3).getBlock(), new ItemStack(Material.BIRCH_FENCE));
                }

                int a = x - 5;
                int b = startHouse + 1;
                int c = z - 5;

                wallLog(startHouse, locData, a, c);
                wallLogSide(startHouse, locData, a, c);
                wallSide(locData, a, b, c);
                ceilingStairs(locData, a, b, c);
                ceilingSupport(locData, a, b, c);
                setDoor(locData, a, b, c);




            }
        }
    }

    public static void wallLog(int startHouse, TerrainGenerator.LocData locData, int a, int c) {
        for (int i = startHouse + 1; i < startHouse + 4; i++) {
            setType(new Location(locData.getWorld(), a, i, c).getBlock(), new ItemStack(Material.OAK_LOG));
        }
        for (int i = startHouse + 1; i < startHouse + 4; i++) {
            setType(new Location(locData.getWorld(), a + 4, i, c).getBlock(), new ItemStack(Material.OAK_LOG));
        }
        for (int i = startHouse + 1; i < startHouse + 4; i++) {
            setType(new Location(locData.getWorld(), a + 8, i, c).getBlock(), new ItemStack(Material.OAK_LOG));
        }
        for (int i = startHouse + 1; i < startHouse + 4; i++) {
            setType(new Location(locData.getWorld(), a, i, c + 4).getBlock(), new ItemStack(Material.OAK_LOG));
        }
        for (int i = startHouse + 1; i < startHouse + 4; i++) {
            setType(new Location(locData.getWorld(), a + 4, i, c + 8).getBlock(), new ItemStack(Material.OAK_LOG));
        }
        for (int i = startHouse + 1; i < startHouse + 4; i++) {
            setType(new Location(locData.getWorld(), a, i, c + 8).getBlock(), new ItemStack(Material.OAK_LOG));
        }
        for (int i = startHouse + 1; i < startHouse + 4; i++) {
            setType(new Location(locData.getWorld(), a + 8, i, c + 4).getBlock(), new ItemStack(Material.OAK_LOG));
        }
        for (int i = startHouse + 1; i < startHouse + 4; i++) {
            setType(new Location(locData.getWorld(), a + 8, i, c + 8).getBlock(), new ItemStack(Material.OAK_LOG));
        }
    }

    public static void wallLogSide(int startHouse, TerrainGenerator.LocData locData, int a, int c) {
        for (int i = 1; i <= 3; i++) {
//            setType(new Location(locData.getWorld(), a + i, startHouse + 1, c).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
//            setType(new Location(locData.getWorld(), a + i, startHouse + 3, c).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
            setType(new Location(locData.getWorld(), a + i, startHouse + 1, c), Material.OAK_LOG, BlockFace.EAST);
            setType(new Location(locData.getWorld(), a + i, startHouse + 3, c), Material.OAK_LOG, BlockFace.EAST);
        }
        for (int i = 1; i <= 3; i++) {
//            setType(new Location(locData.getWorld(), a + i + 4, startHouse + 1, c).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
//            setType(new Location(locData.getWorld(), a + i + 4, startHouse + 3, c).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
            setType(new Location(locData.getWorld(), a + i + 4, startHouse + 1, c), Material.OAK_LOG, BlockFace.EAST);
            setType(new Location(locData.getWorld(), a + i + 4, startHouse + 3, c), Material.OAK_LOG, BlockFace.EAST);
        }
        for (int i = 1; i <= 3; i++) {
//            setType(new Location(locData.getWorld(), a + i, startHouse + 1, c + 8).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
//            setType(new Location(locData.getWorld(), a + i, startHouse + 3, c + 8).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
            setType(new Location(locData.getWorld(), a + i, startHouse + 1, c + 8), Material.OAK_LOG, BlockFace.EAST);
            setType(new Location(locData.getWorld(), a + i, startHouse + 3, c + 8), Material.OAK_LOG, BlockFace.EAST);
        }
        for (int i = 1; i <= 3; i++) {
//            setType(new Location(locData.getWorld(), a + i + 4, startHouse + 1, c + 8).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
//            setType(new Location(locData.getWorld(), a + i + 4, startHouse + 3, c + 8).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
            setType(new Location(locData.getWorld(), a + i + 4, startHouse + 1, c + 8), Material.OAK_LOG, BlockFace.EAST);
            setType(new Location(locData.getWorld(), a + i + 4, startHouse + 3, c + 8), Material.OAK_LOG, BlockFace.EAST);
        }


        for (int i = 1; i <= 3; i++) {
            setType(new Location(locData.getWorld(), a, startHouse + 1, c + i).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
            setType(new Location(locData.getWorld(), a, startHouse + 3, c + i).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
        }
        for (int i = 1; i <= 3; i++) {
            setType(new Location(locData.getWorld(), a, startHouse + 1, c + i + 4).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
            setType(new Location(locData.getWorld(), a, startHouse + 3, c + i + 4).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
        }
        for (int i = 1; i <= 3; i++) {
            setType(new Location(locData.getWorld(), a + 8, startHouse + 1, c + i).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
            setType(new Location(locData.getWorld(), a + 8, startHouse + 3, c + i).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
        }
        for (int i = 1; i <= 3; i++) {
            setType(new Location(locData.getWorld(), a + 8, startHouse + 1, c + i + 4).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
            setType(new Location(locData.getWorld(), a + 8, startHouse + 3, c + i + 4).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
        }
    }

    public static void wallSide(TerrainGenerator.LocData locData, int a, int b, int c) {
        setType(new Location(locData.getWorld(), a + 1, b + 1, c).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 3, b + 1, c).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 5, b + 1, c).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 7, b + 1, c).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 1, b + 1, c + 8).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 3, b + 1, c + 8).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 5, b + 1, c + 8).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 7, b + 1, c + 8).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a, b + 1, c + 1).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a, b + 1, c + 3).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a, b + 1, c + 5).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a, b + 1, c + 7).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 1, c + 1).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 1, c + 3).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 1, c + 5).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 1, c + 7).getBlock(), new ItemStack(Material.SPRUCE_PLANKS, 1, (short) 1));


        setType(new Location(locData.getWorld(), a + 2, b + 1, c).getBlock(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        setType(new Location(locData.getWorld(), a + 6, b + 1, c).getBlock(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        setType(new Location(locData.getWorld(), a + 2, b + 1, c + 8).getBlock(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        setType(new Location(locData.getWorld(), a + 6, b + 1, c + 8).getBlock(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        setType(new Location(locData.getWorld(), a, b + 1, c + 2).getBlock(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        setType(new Location(locData.getWorld(), a, b + 1, c + 6).getBlock(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        setType(new Location(locData.getWorld(), a + 8, b + 1, c + 2).getBlock(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        setType(new Location(locData.getWorld(), a + 8, b + 1, c + 6).getBlock(), new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
    }

    public static void ceilingStairs(TerrainGenerator.LocData locData, int a, int b, int c) {
        setType(new Location(locData.getWorld(), a, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a, b + 3, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a, b + 3, c + 6).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a, b + 3, c + 5).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a, b + 3, c + 4).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a, b + 3, c + 3).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a, b + 3, c + 2).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a, b + 3, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));

        setType(new Location(locData.getWorld(), a + 8, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 2).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 3).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 4).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 5).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 6).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));

        setType(new Location(locData.getWorld(), a + 8, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 7, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 6, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 5, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 4, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 3, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 2, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 1, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a, b + 3, c).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));

        setType(new Location(locData.getWorld(), a, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 1, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 2, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 3, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 4, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 5, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 6, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 7, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 8, b + 3, c + 8).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));

        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 6).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 5).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 4).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 3).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 2).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));
        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 0));

        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 2).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 3).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 4).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 5).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 6).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));
        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 1));

        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 6, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 5, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 4, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 3, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 2, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));
        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 1).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 2));

        setType(new Location(locData.getWorld(), a + 1, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 2, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 3, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 4, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 5, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 6, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
        setType(new Location(locData.getWorld(), a + 7, b + 4, c + 7).getBlock(), new ItemStack(Material.SPRUCE_STAIRS, 1, (short) 3));
    }

    public static void ceilingSupport(TerrainGenerator.LocData locData, int a, int b, int c) {
        for (int i = 1; i < 8; i++) {
            setType(new Location(locData.getWorld(), a + i, b + 3, c + 2).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));
            setType(new Location(locData.getWorld(), a + i, b + 3, c + 6).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 4));

            setType(new Location(locData.getWorld(), a + 2, b + 3, c + i).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
            setType(new Location(locData.getWorld(), a + 6, b + 3, c + i).getBlock(), new ItemStack(Material.OAK_LOG, 1, (short) 8));
        }
        setType(new Location(locData.getWorld(), a + 2, b + 3, c + 2).getBlock(), new ItemStack(Material.GLOWSTONE));
        setType(new Location(locData.getWorld(), a + 2, b + 3, c + 6).getBlock(), new ItemStack(Material.GLOWSTONE));
        setType(new Location(locData.getWorld(), a + 6, b + 3, c + 2).getBlock(), new ItemStack(Material.GLOWSTONE));
        setType(new Location(locData.getWorld(), a + 6, b + 3, c + 6).getBlock(), new ItemStack(Material.GLOWSTONE));

        for (int i = 2; i < 7; i++) {
            for (int j = 2; j < 7; j++) {
                setType(new Location(locData.getWorld(), a + i, b + 4, c + j).getBlock(), new ItemStack(Material.SPRUCE_PLANKS));
            }
        }
    }

    public static void setDoor(TerrainGenerator.LocData locData, int a, int b, int c) {
        int i = new Random().nextInt(8);
        switch (i) {
            case 0:
                setType(new Location(locData.getWorld(), a, b, c + 2).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 5));
                setType(new Location(locData.getWorld(), a, b + 1, c + 2).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 9));
                break;
            case 1:
                setType(new Location(locData.getWorld(), a, b, c + 6).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 5));
                setType(new Location(locData.getWorld(), a, b + 1, c + 6).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 9));
                break;
            case 2:
                setType(new Location(locData.getWorld(), a + 2, b, c).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 6));
                setType(new Location(locData.getWorld(), a + 2, b + 1, c).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 9));
                break;
            case 3:
                setType(new Location(locData.getWorld(), a + 6, b, c).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 6));
                setType(new Location(locData.getWorld(), a + 6, b + 1, c).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 9));
                break;
            case 4:
                setType(new Location(locData.getWorld(), a + 8, b, c + 2).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 7));
                setType(new Location(locData.getWorld(), a + 8, b + 1, c + 2).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 9));
                break;
            case 5:
                setType(new Location(locData.getWorld(), a + 8, b, c + 6).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 7));
                setType(new Location(locData.getWorld(), a + 8, b + 1, c + 6).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 9));
                break;
            case 6:
                setType(new Location(locData.getWorld(), a + 2, b, c + 8).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 4));
                setType(new Location(locData.getWorld(), a + 2, b + 1, c + 8).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 9));
                break;
            case 7:
                setType(new Location(locData.getWorld(), a + 6, b, c + 8).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 4));
                setType(new Location(locData.getWorld(), a + 6, b + 1, c + 8).getBlock(), new ItemStack(Material.SPRUCE_DOOR, 1, (short) 9));
                break;
        }
    }
}
