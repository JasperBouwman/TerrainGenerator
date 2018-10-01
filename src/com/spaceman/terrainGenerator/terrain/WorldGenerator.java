package com.spaceman.terrainGenerator.terrain;

import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import net.minecraft.server.v1_13_R2.Block;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.craftbukkit.v1_13_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_13_R2.block.impl.CraftChest;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

import static com.spaceman.terrainGenerator.Main.log;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.worldSeedValue;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.*;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.getAirPockets;
import static com.spaceman.terrainGenerator.terrain.TerrainGenerator.GenData.getHighest;

public class WorldGenerator extends ChunkGenerator {

    public static HashMap<String, String> customWorlds = new HashMap<>();
    private LinkedList<String> internalGenerators = new LinkedList<>();

    public WorldGenerator(String terrainGenDataName) {
        TerrainGenData mainGenerator = terrainGenData.get(terrainGenDataName);

        if (mainGenerator == null) {
            throw new IllegalArgumentException("TerrainGenData is null");
        }

        internalGenerators.add(terrainGenDataName);//add main generator so it would startGen with this one
        internalGenerators.addAll(getGenerators(mainGenerator.getGenerators()));//add all other generators

    }

    public static void tpAway(Player pl) {
        try {
            pl.teleport(Bukkit.getWorld("world").getSpawnLocation());
        } catch (NullPointerException npe) {
            pl.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
    }

    public static void initTerrainWorlds() {
        Files worldData = GettingFiles.getFiles("worldData");
        if (!worldData.getConfig().contains("customWorlds")) {
            return;
        }

        for (String worldName : worldData.getConfig().getConfigurationSection("customWorlds").getKeys(false)) {
            log("Loading TerrainWorld " + worldName + " with the TerrainGenerator " + worldData.getConfig().getString("customWorlds." + worldName));
            try {
                HashMap<UUID, Location> map = new HashMap<>();
                if (Bukkit.getWorld(worldName) != null) {
                    World world = Bukkit.getWorld(worldName);
                    for (Player pl : world.getPlayers()) {
                        map.put(pl.getUniqueId(), pl.getLocation());

                        if (worldData.getConfig().contains("defaultWorld")) {
                            String defaultWorld = worldData.getConfig().getString("defaultWorld");
                            try {
                                pl.teleport(Bukkit.getWorld(defaultWorld).getSpawnLocation());
                            } catch (Exception e) {
                                log(Level.WARNING, "defaultWorld '" + defaultWorld + "' does not exist, using a random world...");
                                tpAway(pl);
                            }
                        } else {
                            tpAway(pl);
                        }
                    }
                    Bukkit.unloadWorld(world, true);
                }
                World w = generateWorld(worldName, worldData.getConfig().getString("customWorlds." + worldName));
                for (UUID uuid : map.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    Location newL = map.get(uuid);
                    newL.setWorld(w);
                    player.teleport(newL);
                }

            } catch (IllegalArgumentException iae) {
                log(Level.WARNING,"TerrainWorld " + worldName + " could not been loaded, the TerrainGenerator '" + worldData.getConfig().getString("customWorlds." + worldName) + "' is missing");
            }
        }
    }

    public static void saveTerrainWorlds() {
        Files worldData = GettingFiles.getFiles("worldData");

        for (String worldName : customWorlds.keySet()) {
            worldData.getConfig().set("customWorlds." + worldName, customWorlds.get(worldName));
        }
        worldData.saveConfig();
    }

    public static World generateWorld(String worldName, WorldGenerator worldGenerator) {
        customWorlds.put(worldName, worldGenerator.internalGenerators.get(0));

        WorldCreator WC = new WorldCreator(worldName);
        WC.generator(worldGenerator);
        return Bukkit.getServer().createWorld(WC);
    }

    public static World generateWorld(String worldName, String terrainGenDataName) {
        return generateWorld(worldName, new WorldGenerator(terrainGenDataName));
    }

    @SuppressWarnings("WeakerAccess")
    public static void useFinalChunkModes(LinkedList<String> dataList, LocData locData,
                                          HashMap<String, HashMap<String, GenData>> genStorage, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                for (String sData : dataList) {
                    TerrainGenData data = terrainGenData.get(sData);

                    for (TerrainMode mode : data.getModes()) {
                        if (mode.isFinalMode()) {
                            try {
                                chunkData.x = x;
                                chunkData.z = z;
                                mode.useMode(x + chunkData.getChunkX() * 16,
                                        z + chunkData.getChunkZ() * 16,
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

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {

        //location, genDataName, fromTo
        HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage = new HashMap<>();

        ChunkData chunk = createChunkData(world);

        LocData locData = new LocData(new Location(world, chunkX * 16, 0, chunkZ * 16), 16, 16);

        HashMap<String, Object> genModeData = new HashMap<>();

        for (String internalGenerator : internalGenerators) {
            TerrainGenData i = terrainGenData.get(internalGenerator);

            if (i != null) {

                long seed = i.getSeed();
                if (seed == worldSeedValue) {
                    seed = world.getSeed();
                }

                SimplexOctaveGenerator gen = new SimplexOctaveGenerator(seed, i.getOctaves());
                gen.setScale(i.getScale());

                TerrainMode layersMode = i.getMode("layers");

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {

                        int realX = x + chunkX * 16;
                        int realZ = z + chunkZ * 16;

                        int highest = getHighest(realX, realZ, genStorage);

                        int start = i.getStart(realX, realZ, world);
                        int genHeight = (int) (gen.noise(realX, realZ, i.getFrequency(), i.getAmplitude()) * i.getMultitude() + i.getHeight());

                        ArrayList<Integer> airPockets = new ArrayList<>();

                        if (i.getFromTop()) {
                            airPockets = getAirPockets(realX, realZ, genStorage, start, genHeight);
                        }

                        int startGen;
                        if (i.getFromTop()) {
                            startGen = (start > highest ? start : highest);
                        } else {
                            startGen = world.getMaxHeight();

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

                        HashMap<String, TerrainGenerator.GenData> tmpMap = genStorage.getOrDefault(realX + ";" + realZ, new HashMap<>());
                        tmpMap.put(internalGenerator, new TerrainGenerator.GenData(startGen, endGen, start, genHeight, true, random));
                        genStorage.put(realX + ";" + realZ, tmpMap);

                        for (int y = genHeight; y > startGen; y--) {

                            if (!i.getFromTop()) {
                                if (!airPockets.contains(y)) {
                                    continue;
                                }
                            }


                            if (i.getBiome() != null) {
                                biome.setBiome(x, z, i.getBiome());
                            }

                            // internal modes: 'layers'
                            if (layersMode == null) {
                                if (!i.getTerrainBlockData().getMaterial().equals(Material.STRUCTURE_VOID)) {
//                                    setType(block, i.getTerrainBlockData().getMaterial(), i.getTerrainBlockData().getBlockFace());
                                    if (i.getTerrainBlockData().getMaterial().isBlock()) {
//                                        chunk.setBlock(x, y, z, i.getTerrainBlockData().getMaterial());
                                        setChunkBlock(x, y, z, i.getTerrainBlockData().getMaterial(), i.getTerrainBlockData().getBlockFace(), chunk);
                                    }
//                                    chunk.setBlock(1, 1, 1, new Chest(BlockFace.NORTH));
                                }
                            }
                        }

                        useModes(i, realX, realZ, genStorage, locData, "", genModeData, new TerrainChunkData(chunk, chunkX, chunkZ, x, z, biome));

                    }
                }
            }
        }
        useFinalChunkModes(internalGenerators, locData, genStorage, "", genModeData, new TerrainChunkData(chunk, chunkX, chunkZ, 0, 0, biome));

        return chunk;
    }

    public static void setChunkBlock(int x, int y, int z, Material material, BlockFace blockFace, ChunkData chunkData) {
//        try {
//            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
//            Class<?> blockData = Class.forName("org.bukkit.craftbukkit." + version + ".block.impl.Craft" + toLowerCase(material.toString()));
//            Object constructor = blockData.newInstance();
//
//            if (Directional.class.isAssignableFrom(blockData)) {
//                constructor.getClass().getMethod("setFacing", BlockFace.class).invoke(blockData, blockFace);
//            }
//            chunkData.setBlock(x, y, z, (BlockData) constructor);

//        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
//            e.printStackTrace();
//            chunkData.setBlock(x, y, z, material);
//        }

        if (material.toString().contains("_SHULKER_BOX")) {
            return;
        }

        BlockData blockData = Bukkit.createBlockData(material);
        if (blockData instanceof Directional) {
//            Directional d = (Directional) blockData;
//            d.setFacing(blockFace);
            ((Directional) blockData).setFacing(blockFace);
        }
        if (blockData instanceof Orientable) {
//            Orientable o = (Orientable) blockData;
//            o.setAxis(convertBlockFaceToAxis(blockFace));
            ((Orientable) blockData).setAxis(convertBlockFaceToAxis(blockFace));
        }
        if (blockData instanceof Rotatable) {
//            Rotatable d = (Rotatable) blockData;
//            d.setRotation(blockFace);
            ((Rotatable) blockData).setRotation(blockFace);
        }
        chunkData.setBlock(x, y, z, blockData);

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

    @SuppressWarnings("WeakerAccess")
    public class TerrainChunkData {
        private ChunkData chunkData;
        private int chunkX;
        private int chunkZ;
        private int x;
        private int z;
        private BiomeGrid biomeGrid;

        public TerrainChunkData(ChunkData chunkData, int chunkX, int chunkZ, int x, int z, BiomeGrid biomeGrid) {
            this.chunkData = chunkData;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.x = x;
            this.z = z;
            this.biomeGrid = biomeGrid;
        }

        public void setBlock(int y, Material material) {
            if (material.isBlock()) {
                chunkData.setBlock(x, y, z, material);
            }
        }
        public void setBlock(int y, Material material, BlockFace blockFace) {
            if (material.isBlock()) {
                setChunkBlock(x, y, z, material, blockFace, chunkData);
            }
        }

        public ChunkData getChunkData() {
            return chunkData;
        }

        public void setChunkData(ChunkData chunkData) {
            this.chunkData = chunkData;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public int getChunkX() {
            return chunkX;
        }

        public int getChunkZ() {
            return chunkZ;
        }

        public BiomeGrid getBiomeGrid() {
            return biomeGrid;
        }
    }

}
