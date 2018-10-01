package com.spaceman.terrainGenerator.terrain;

import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.*;
import java.util.logging.Level;

import static com.spaceman.terrainGenerator.fileHander.GettingFiles.getFiles;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.terrainGenData;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.worldSeedValue;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.getAirPockets;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.getHighest;
import static com.spaceman.terrainGenerator.terrain.TerrainMode.getNewMode;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TerrainGenerator {

    public static HashMap<String, TerrainGenData> terrainGenData = new HashMap<>();

    public static void setType(final Location l, final Material material, BlockFace blockFace) {
        setType(l.getBlock(), material, blockFace);
    }

    public static void setType(final Location l, final Material material) {
        setType(l.getBlock(), material);
    }

    public static void setType(final Block b, final Material material) {
        new BukkitRunnable() {
            public void run() {
                b.setType(material);
            }
        }.runTask(GettingFiles.p);
    }

    public static void setType(final Block block, final Material material, BlockFace blockFace) {
        new BukkitRunnable() {
            public void run() {
                block.setType(material);
                BlockData blockData = block.getBlockData();
                try {
                    if (blockFace != null) {
                        if (blockData instanceof Directional) {
                            ((Directional) blockData).setFacing(blockFace);
                            block.setBlockData(blockData);
                        }
                        if (blockData instanceof Orientable) {
                            ((Orientable) blockData).setAxis(convertBlockFaceToAxis(blockFace));
                            block.setBlockData(blockData);
                        }
                        if (blockData instanceof Rotatable) {
                            ((Rotatable) blockData).setRotation(blockFace);
                            block.setBlockData(blockData);
                        }
                    }
                } catch (Error | Exception ignore) {
                }
            }
        }.runTask(GettingFiles.p);
    }

    private static Axis convertBlockFaceToAxis(BlockFace face) {
        switch (face) {
            case NORTH:
            case SOUTH:
                return Axis.Z;
            case EAST:
            case WEST:
                return Axis.X;
            case UP:
            case DOWN:
                return Axis.Y;
            default:
                return Axis.X;
        }
    }

    public static TerrainGenData addGen(String name, TerrainGenData data) {
        if (terrainGenData.containsKey(name)) {
            return terrainGenData.get(name);
        } else {
            terrainGenData.put(name, data);
            return data;
        }
    }

    public static int removeGen(String name) {
        /* return codes
        * 1: removed
        * 2: does not exist
        * */
        TerrainGenData data = getGen(name);

        if (data != null) {
            for (TerrainGenData tmpData : terrainGenData.values()) {
                tmpData.getGenerators().remove(data.getName());
            }
            terrainGenData.remove(name);
            return 1;
        } else {
            return 2;
        }
    }

    public static TerrainGenData getGen(String name) {
        return terrainGenData.getOrDefault(name, null);
    }

    public static Set<String> getGens() {
        return terrainGenData.keySet();
    }

    public static void generate(String terrainGenDataName, int x1, int z1, Location startL) {
        TerrainGenData mainGenerator = terrainGenData.get(terrainGenDataName);

        if (mainGenerator == null) {
            throw new IllegalArgumentException("TerrainGenData is null");
        }

        LocData locData = new LocData(startL, x1, z1);
        Random random = new Random();

        LinkedList<String> internalGenerators = new LinkedList<>();
        internalGenerators.add(terrainGenDataName);//add main generator so it would startGen with this one
        internalGenerators.addAll(getGenerators(mainGenerator.getGenerators()));//add all other generators
        //location, genDataName, fromTo
        HashMap<String, HashMap<String, GenData>> genStorage = new HashMap<>();
        HashMap<String, Object> genModeData = new HashMap<>();

        for (String internalGenerator : internalGenerators) {
            TerrainGenData i = terrainGenData.get(internalGenerator);

            if (i != null) {

                long seed = i.getSeed();
                if (seed == worldSeedValue) {
                    seed = startL.getWorld().getSeed();
                }

                SimplexOctaveGenerator gen = new SimplexOctaveGenerator(seed, i.getOctaves());
                gen.setScale(i.getScale());

                TerrainMode layersMode = i.getMode("layers");

                for (int x = startL.getBlockX(); x < x1 + startL.getBlockX(); x++) {
                    for (int z = startL.getBlockZ(); z < z1 + startL.getBlockZ(); z++) {

                        int highest = getHighest(x, z, genStorage);

                        int start = i.getStart(x, z, startL.getWorld());
                        int genHeight = (int) (gen.noise(x, z, i.getFrequency(), i.getAmplitude()) * i.getMultitude() + i.getHeight());

                        ArrayList<Integer> airPockets = getAirPockets(x, z, genStorage, start, genHeight);

                        int startGen;
                        if (i.getFromTop()) {
                            startGen = (start > highest ? start : highest);
                        } else {
                            startGen = startL.getWorld().getMaxHeight();

                            for (int j : airPockets) {
                                if (j < startGen) {
                                    startGen = j;
                                }
                            }
                        }

                        int endGen;
                        if (i.getFromTop()) {
                            endGen = genHeight;
                        } else {
                            endGen = 0;

                            for (int j : airPockets) {
                                if (j > endGen) {
                                    endGen = j;
                                }
                            }
                        }

                        HashMap<String, GenData> tmpMap = genStorage.getOrDefault(x + ";" + z, new HashMap<>());
                        tmpMap.put(internalGenerator, new GenData(startGen, endGen, start, genHeight, true, random));
                        genStorage.put(x + ";" + z, tmpMap);

                        for (int y = genHeight; y > startGen; y--) {

                            if (!i.getFromTop()) {
                                if (!airPockets.contains(y)) {
                                    continue;
                                }
                            }

                            Block block = new Location(startL.getWorld(), x, y, z).getBlock();

                            if (i.getBiome() != null) {
                                block.setBiome(i.getBiome());
                            }

                            // internal modes: 'layers'
                            if (layersMode == null) {
                                if (!i.getTerrainBlockData().getMaterial().equals(Material.STRUCTURE_VOID)) {
                                    setType(block, i.getTerrainBlockData().getMaterial(), i.getTerrainBlockData().getBlockFace());
                                }
                            }
                        }

                        useModes(i, x, z, genStorage, locData, "", genModeData, null);

                    }
                }
            }
        }
        useFinalModes(internalGenerators, locData, genStorage, "", genModeData,null);
    }

    public static LinkedList<String> getGenerators(LinkedList<String> d) {

        if (d == null) {
            return new LinkedList<>();
        }

        LinkedList<String> list = new LinkedList<>(d);
        for (String data : d) {
            for (String tmpData : getGenerators(terrainGenData.get(data).getGenerators())) {
                if (!list.contains(tmpData)) {
                    if (TerrainGenData.terrainGenData(tmpData, false) != null) {
                        list.add(tmpData);
                    } else {
                        throw new NullPointerException("TerrainGenData " + tmpData + " does not exist anymore");
                    }
                }
            }
        }
        return list;
    }

    public static LinkedList<String> getGenerators(String... d) {
        return getGenerators(new LinkedList<>(Arrays.asList(d)));
    }

    public static void useModes(TerrainGenData data, int x, int z, HashMap<String,
            HashMap<String, GenData>> genStorage, LocData locData, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

//        System.out.println(chunkData.getChunkX() + " " + chunkData.getChunkZ());

        for (TerrainMode mode : data.getModes()) {
            if (!mode.isFinalMode()) {
                try {
                    mode.useMode(x, z, genStorage, locData, data, savePath, genModeData, chunkData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void useFinalModes(LinkedList<String> dataList, LocData locData,
                                     HashMap<String, HashMap<String, GenData>> genStorage, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        for (int x = locData.startL.getBlockX(); x < locData.x + locData.startL.getBlockX(); x++) {
            for (int z = locData.startL.getBlockZ(); z < locData.z + locData.startL.getBlockZ(); z++) {

                for (String sData : dataList) {
                    TerrainGenData data = terrainGenData.get(sData);

                    for (TerrainMode mode : data.getModes()) {
                        if (mode.isFinalMode()) {
                            try {
                                mode.useMode(x,
                                        z,
                                        genStorage,
                                        locData,
                                        data,
                                        savePath,
                                        genModeData,
                                        chunkData);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void saveGenerators() {
        Files terrainData = getFiles("terrainData");
        terrainData.getConfig().set("terrainGenData", null);

        if (terrainGenData.isEmpty()) {
            return;
        }

        for (String name : terrainGenData.keySet()) {
            TerrainGenData genData = terrainGenData.get(name);

            terrainData.getConfig().set("terrainGenData." + name + ".data.name", genData.getName());
            terrainData.getConfig().set("terrainGenData." + name + ".data.frequency", genData.getFrequency());
            terrainData.getConfig().set("terrainGenData." + name + ".data.amplitude", genData.getAmplitude());
            terrainData.getConfig().set("terrainGenData." + name + ".data.multitude", genData.getMultitude());
            terrainData.getConfig().set("terrainGenData." + name + ".data.scale", genData.getScale());
            terrainData.getConfig().set("terrainGenData." + name + ".data.height", genData.getHeight());
            terrainData.getConfig().set("terrainGenData." + name + ".data.startGen", genData.getStart());
            terrainData.getConfig().set("terrainGenData." + name + ".data.seed", genData.getSeed());
            terrainData.getConfig().set("terrainGenData." + name + ".data.terrainBlockData", genData.getTerrainBlockData());
            terrainData.getConfig().set("terrainGenData." + name + ".data.generators", genData.getGenerators());
            if (genData.getBiome() != null) {
                terrainData.getConfig().set("terrainGenData." + name + ".data.biome", genData.getBiome().toString());
            }

            //save terrainModes
            for (TerrainMode mode : genData.getModes()) {
                String savePath = "terrainGenData." + name + ".terrainMode." + mode.getModeName() + ".data";
                if (mode instanceof TerrainMode.DataBased) {
                    ((TerrainMode.DataBased) mode).saveMode(savePath);
                    terrainData.getConfig().set("terrainGenData." + name + ".terrainMode." + mode.getModeName() + ".type", "DataBased");
                }
                if (mode instanceof TerrainMode.MapBased) {
                    ((TerrainMode.MapBased) mode).saveMode(savePath);
                    terrainData.getConfig().set("terrainGenData." + name + ".terrainMode." + mode.getModeName() + ".type", "MapBased");
                }
                if (mode instanceof TerrainMode.ArrayBased) {
                    ((TerrainMode.ArrayBased) mode).saveMode(savePath);
                    terrainData.getConfig().set("terrainGenData." + name + ".terrainMode." + mode.getModeName() + ".type", "ArrayBased");
                }
            }
        }
        terrainData.saveConfig();
    }

    public static void initGenerators() {
        Files terrainData = getFiles("terrainData");

        if (terrainData.getConfig().contains("terrainGenData")) {
            for (String name : terrainData.getConfig().getConfigurationSection("terrainGenData").getKeys(false)) {

                TerrainGenData genData = terrainGenData(terrainData.getConfig().getString("terrainGenData." + name + ".data.name"), true);

                if (genData == null) {
                    continue;
                }

                genData.setFrequency(terrainData.getConfig().getDouble("terrainGenData." + name + ".data.frequency"));
                genData.setAmplitude(terrainData.getConfig().getDouble("terrainGenData." + name + ".data.amplitude"));
                genData.setMultitude(terrainData.getConfig().getDouble("terrainGenData." + name + ".data.multitude"));
                genData.setScale(terrainData.getConfig().getDouble("terrainGenData." + name + ".data.scale"));
                genData.setHeight(terrainData.getConfig().getInt("terrainGenData." + name + ".data.height"));
                genData.setStart(terrainData.getConfig().getString("terrainGenData." + name + ".data.startGen"));
                genData.setSeed(terrainData.getConfig().getLong("terrainGenData." + name + ".data.seed"));
                genData.setTerrainBlockData((TerrainBlockData) terrainData.getConfig().get("terrainGenData." + name + ".data.terrainBlockData"));
                genData.setGenerators(new LinkedList<>(terrainData.getConfig().getStringList("terrainGenData." + name + ".data.generators")));
                if (terrainData.getConfig().contains("terrainGenData." + name + ".data.biome")) {
                    genData.setBiome(Biome.valueOf(terrainData.getConfig().getString("terrainGenData." + name + ".data.biome")));
                }

                if (terrainData.getConfig().contains("terrainGenData." + name + ".terrainMode")) {
                    for (String modeName : terrainData.getConfig().getConfigurationSection("terrainGenData." + name + ".terrainMode").getKeys(false)) {

                        TerrainMode terrainMode = getNewMode(modeName);

                        if (terrainMode != null) {
                            switch (terrainData.getConfig().getString("terrainGenData." + name + ".terrainMode." + modeName + ".type")) {
                                case "MapBased": {
                                    TerrainMode.MapBased templateMode = (TerrainMode.MapBased) terrainMode;
                                    genData.addMode(templateMode.getMode("terrainGenData." + name + ".terrainMode." + modeName + ".data", templateMode));
                                    break;
                                }
                                case "ArrayBased": {
                                    TerrainMode.ArrayBased templateMode = (TerrainMode.ArrayBased) terrainMode;
                                    genData.addMode(templateMode.getMode("terrainGenData." + name + ".terrainMode." + modeName + ".data", templateMode));
                                    break;
                                }
                                case "DataBased": {
                                    TerrainMode.DataBased templateMode = (TerrainMode.DataBased) terrainMode;
                                    genData.addMode(templateMode.getMode("terrainGenData." + name + ".terrainMode." + modeName + ".data", templateMode));
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            for (String name : terrainGenData.keySet()) {
                for (String internalGenerators : terrainGenData.get(name).getGenerators()) {
                    if (terrainGenData.getOrDefault(internalGenerators, null) == null) {
                        removeGen(internalGenerators);
                        Bukkit.getLogger().log(Level.WARNING, "The TerrainGenerator " + internalGenerators + " does not exist anymore, but is used in other TerrainGenerators." +
                                "This TerrainGenerator will be removed from all the other TerrainGenerators");
                    }
                }
            }

        }
    }

    public static class LocData {
        private Location startL;
        private int x;
        private int z;

        public /*todo make un-public*/ LocData(Location startL, int x, int z) {
            this.startL = startL;
            this.x = x;
            this.z = z;
        }

        public int getZ() {
            return z;
        }

        public int getX() {
            return x;
        }

        public Location getStartL() {
            return startL;
        }

        public World getWorld() {
            return startL.getWorld();
        }
    }

    public static class GenData {
        private int startGen;
        private int heightGen;
        private int start;
        private int end;
        private boolean terrain;
        private Random random;

        public GenData(int startGen, int endGen, int start, int genHeight, boolean terrain, Random random) {
            this.startGen = startGen;
            this.heightGen = endGen;
            this.start = start;
            this.end = genHeight;
            this.terrain = terrain;
            this.random = random;
        }

        public static int getHighest(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage) {
            int highest = 0;
            for (String loc : genStorage.keySet()) {
                if (!loc.equals(x + ";" + z)) {
                    continue;
                }
                for (GenData genData : genStorage.get(loc).values()) {
                    if (genData.heightGen > highest) {
                        highest = genData.heightGen;
                    }
                }
            }
            return highest;
        }

        public static int getLowest(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage) {
            int lowest = Integer.MAX_VALUE;
            for (String loc : genStorage.keySet()) {
                if (!loc.equals(x + ";" + z)) {
                    continue;
                }
                for (GenData genData : genStorage.get(loc).values()) {
                    if (genData.heightGen < lowest) {
                        lowest = genData.heightGen;
                    }
                }
            }
            return lowest;
        }

        public static ArrayList<Integer> getAirPockets(int x, int z, HashMap<String, HashMap<String, GenData>> genStorage, int start, int end) {
            ArrayList<Integer> airPockets = new ArrayList<>();
            if (start == -1 || end == -1) {
                return airPockets;
            }

            for (int i = start; i < end; i++) {
                airPockets.add(i);
            }

            for (String loc : genStorage.keySet()) {
                if (!loc.equals(x + ";" + z)) {
                    continue;
                }
                for (GenData genData : genStorage.get(loc).values()) {
                    for (int i = genData.startGen; i <= genData.heightGen; i++) {
                        airPockets.remove(Integer.valueOf(i));
                    }
                }
            }
            return airPockets;
        }


        public static GenData getGenData(int x, int z, String name, HashMap<String, HashMap<String, GenData>> genStorage) {
            return genStorage.getOrDefault(x + ";" + z, new HashMap<>()).getOrDefault(name, new GenData(-1, -1, -1, -1, true, new Random()));
        }

        public int getStartGen() {
            return startGen;
        }

        public int getHeightGen() {
            return heightGen;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public boolean isTerrain() {
            return terrain;
        }

        public Random getRandom() {
            return random;
        }
    }
}
