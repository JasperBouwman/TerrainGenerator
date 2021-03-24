package com.spaceman.terrainGenerator.terrain.generators;

import com.spaceman.terrainGenerator.fileHander.Files;
import com.spaceman.terrainGenerator.fileHander.GettingFiles;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.biome.TerrainGrid;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.*;
import java.util.logging.Level;

import static com.spaceman.terrainGenerator.Main.log;
import static com.spaceman.terrainGenerator.terrain.TerrainCore.*;
import static com.spaceman.terrainGenerator.terrain.TerrainGenData.worldSeedValue;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.*;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.getAirPockets;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.getHighest;

@SuppressWarnings({"WeakerAccess", "unused"})
public class WorldGenerator extends ChunkGenerator {
    
    public static final String worldPrefix = "plugins/TerrainGenerator/worlds/";
    //<world name, chunk generator>
    public static HashMap<String, WorldGenerator> customWorlds = new HashMap<>();
    private String terrainGeneratorName;
    private boolean autoLoad;
    private ArrayList<String> chunkWhiteList = new ArrayList<>();
    
    public WorldGenerator(String terrainGenDataName, boolean autoLoad) {
        if (!terrainGenData.containsKey(terrainGenDataName)) {
            throw new IllegalArgumentException("TerrainGenData is null");
        }
        this.terrainGeneratorName = terrainGenDataName;
        this.autoLoad = autoLoad;
    }
    
    public static void tpAway(Player player) {
        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    }
    
    public static void initTerrainWorlds() {
        Files worldData = GettingFiles.getFiles("worldData");
        if (!worldData.getConfig().contains("customWorlds")) {
            return;
        }
        
        for (String worldName : worldData.getKeys("customWorlds")) {
            log("Loading TerrainWorld " + worldName + " with the TerrainGenerator " + worldData.getConfig().getString("customWorlds." + worldName + ".genName"));
            try {
                HashMap<UUID, Location> map = new HashMap<>();
                if (Bukkit.getWorld(worldPrefix + worldName) != null) {
                    World world = Bukkit.getWorld(worldPrefix + worldName);
                    if (world != null) {
                        for (Player pl : world.getPlayers()) {
                            map.put(pl.getUniqueId(), pl.getLocation());
                            
                            if (worldData.getConfig().contains("defaultWorld")) {
                                String defaultWorldName = worldData.getConfig().getString("defaultWorld");
                                try {
                                    World defaultWorld = Bukkit.getWorld(defaultWorldName == null ? "" : defaultWorldName);
                                    if (defaultWorld != null) {
                                        pl.teleport(defaultWorld.getSpawnLocation());
                                    } else {
                                        log(Level.WARNING, "defaultWorld '" + defaultWorldName + "' does not exist, using a random world...");
                                        tpAway(pl);
                                    }
                                } catch (IllegalArgumentException iae) {
                                    log(Level.WARNING, "defaultWorld '" + defaultWorldName + "' does not exist, using a random world...");
                                    tpAway(pl);
                                }
                            } else {
                                tpAway(pl);
                            }
                        }
                        Bukkit.unloadWorld(world, true);
                    }
                }
                World w = generateWorld(worldName,
                        worldData.getConfig().getString("customWorlds." + worldName + ".genName"),
                        worldData.getConfig().getBoolean("customWorlds." + worldName + ".autoLoad"));
                for (UUID uuid : map.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        Location newL = map.get(uuid);
                        newL.setWorld(w);
                        player.teleport(newL);
                    }
                }
                
            } catch (IllegalArgumentException iae) {
                log(Level.WARNING, "TerrainWorld " + worldName + " could not been loaded, the TerrainGenerator '" + worldData.getConfig().getString("customWorlds." + worldName + ".genName") + "' is missing");
            }
        }
    }
    
    public static void saveTerrainWorlds() {
        Files worldData = GettingFiles.getFiles("worldData");
        
        worldData.getConfig().set("customWorlds", null);
        
        for (String worldName : customWorlds.keySet()) {
            worldData.getConfig().set("customWorlds." + worldName + ".genName", customWorlds.get(worldName).terrainGeneratorName);
            worldData.getConfig().set("customWorlds." + worldName + ".autoLoad", customWorlds.get(worldName).autoLoad);
        }
        worldData.saveConfig();
    }
    
    public static World generateWorld(String worldName, WorldGenerator worldGenerator) {
        customWorlds.put(worldName, worldGenerator);
        
        WorldCreator WC = new WorldCreator(worldPrefix + worldName);
        WC.generator(worldGenerator);
        return Bukkit.createWorld(WC);
    }
    
    public static World generateWorld(String worldName, String terrainGenDataName, boolean autoLoad) {
        return generateWorld(worldName, new WorldGenerator(terrainGenDataName, autoLoad));
    }
    
    public static void setChunkBlock(int x, int y, int z, TerrainBlockData terrainBlockData, ChunkData chunkData) {
        //noinspection SpellCheckingInspection
        if (terrainBlockData.getMaterial().name().contains("_SHULKER_BOX")) {
            return;
        }
        
        BlockData blockData = Bukkit.createBlockData(terrainBlockData.getMaterial());
        if (blockData instanceof Directional) {
            ((Directional) blockData).setFacing(terrainBlockData.getBlockFace());
        }
        if (blockData instanceof Orientable) {
            ((Orientable) blockData).setAxis(convertBlockFaceToAxis(terrainBlockData.getBlockFace()));
        }
        if (blockData instanceof Rotatable) {
            ((Rotatable) blockData).setRotation(terrainBlockData.getBlockFace());
        }
        if (blockData instanceof Waterlogged) {
            if (terrainBlockData.isWaterLogged() != null) {
                ((Waterlogged) blockData).setWaterlogged(terrainBlockData.isWaterLogged());
            }
        }
        chunkData.setBlock(x, y, z, blockData);
    }
    
    public static void useFinalChunkModes(int realX, int realZ, LocData locData, HashMap<String, HashMap<String, GenData>> genStorage,
                                          String savePath, HashMap<String, Object> genModeData,
                                          WorldGenerator.TerrainChunkData chunkData,
                                          LinkedList<String> biomes) {

        for (String sData : biomes) {
            TerrainGenData data = terrainGenData.get(sData);
            
            for (TerrainMode mode : data.getModes()) {
                if (mode.isFinalMode()) {
                    try {
                        if (chunkData != null) {
                            chunkData.x = realX;
                            chunkData.z = realZ;
                            mode.useMode(realX + (chunkData.getChunkX() << 4),
                                    realZ + (chunkData.getChunkZ() << 4),
                                    genStorage,
                                    locData,
                                    data,
                                    savePath,
                                    genModeData,
                                    chunkData);
                        } else {
                            mode.useMode(realX,
                                    realZ,
                                    genStorage,
                                    locData,
                                    data,
                                    savePath,
                                    genModeData,
                                    null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public static void generate(int x, int z, int realX, int realZ, int chunkX, int chunkZ, TerrainGenData i, TerrainGenData mainGenerator, World world,
                                HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                                Random random, BiomeGrid biomeGrid,
                                ChunkData chunk, LocData locData, HashMap<String, Object> genModeData) {
        long seed = i.getSeed();
        if (seed == worldSeedValue) {
            seed = world.getSeed();
        }
        
        //generated height
        int genHeight = Math.min(i.getTerrainNoise().noise(realX, realZ, seed) + i.getHeight(), world.getMaxHeight());
        //generated start
        int genStart = Math.max(0, i.getStart(realX, realZ, world));
        
        Collection<Integer> airPockets = (mainGenerator.getFromTop() ? Collections.emptyList() : getAirPockets(realX, realZ, genStorage, genStart, genHeight));
        
        //final end
        int endGen;
        //final start
        int startGen;
        
        if (mainGenerator.getFromTop()) {
            endGen = genHeight;
            int highest = getHighest(realX, realZ, genStorage);
            startGen = (genStart > highest ? genStart : highest + 1);
        } else {
            endGen = Integer.MIN_VALUE;
            startGen = Integer.MAX_VALUE;
            
            for (int pocket : airPockets) {
                if (pocket > endGen) {
                    endGen = pocket;
                }
                if (pocket < startGen) {
                    startGen = pocket;
                }
            }
            if (endGen == Integer.MIN_VALUE) {
                endGen = genHeight;
            }
            if (startGen == Integer.MAX_VALUE) {
                startGen = genStart;
            }
        }
    
        endGen = Math.min(i.getMax(realX, realZ, world), endGen);
        startGen = Math.max(i.getMin(realX, realZ, world), startGen);
        
        HashMap<String, TerrainGenerator.GenData> tmpMap = genStorage.getOrDefault(realX + ";" + realZ, new HashMap<>());
        tmpMap.put(i.getName(), new TerrainGenerator.GenData(startGen, endGen, genStart, genHeight, random, mainGenerator.getFromTop()));
        genStorage.put(realX + ";" + realZ, tmpMap);

        boolean shouldBuild = i.getMode("layers") == null &&
                !i.getTerrainBlockData().getMaterial().equals(Material.STRUCTURE_VOID) &&
                i.getTerrainBlockData().getMaterial().isBlock();
        
        if (biomeGrid != null && i.getBiome() != null) {
            biomeGrid.setBiome(x, z, i.getBiome()); //todo add y (for 1.16 maybe)
        }
        for (int y = endGen; y >= startGen; y--) {
            
            if (!mainGenerator.getFromTop()) {
                if (!airPockets.contains(y)) {
                    continue;
                }
            }
            
            if (shouldBuild) {
                if (chunk == null) {
                    Block block = new Location(world, realX, y, realZ).getBlock();
                    if (i.getBiome() != null) {
                        block.setBiome(i.getBiome());
                    }
                    setType(block, i.getTerrainBlockData());
                } else {
                    setChunkBlock(x, y, z, i.getTerrainBlockData(), chunk);
                }
            }
        }
        useModes(i, realX, realZ, genStorage, locData, "", genModeData, chunk == null ? null : new TerrainChunkData(chunk, chunkX, chunkZ, x, z, biomeGrid));
    }
    
    public String getTerrainGeneratorName() {
        return terrainGeneratorName;
    }
    
    public boolean isAutoLoad() {
        return autoLoad;
    }
    
    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
        this.chunkWhiteList = new ArrayList<>();
    }
    
    public void addChunk(int chunkX, int chunkZ) {
        if (!chunkWhiteList.contains(chunkX + ";" + chunkZ) && !autoLoad) {
            chunkWhiteList.add(chunkX + ";" + chunkZ);
        }
    }
    
    public boolean isWhiteListed(int chunkX, int chunkZ) {
        return chunkWhiteList.contains(chunkX + ";" + chunkZ);
    }
    
    public void removeChunk(int chunkX, int chunkZ) {
        chunkWhiteList.remove(chunkX + ";" + chunkZ);
    }
    
    @SuppressWarnings("NullableProblems")
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        ChunkData chunk = createChunkData(world);
        
        if (!autoLoad) {
            if (!isWhiteListed(chunkX, chunkZ)) {
                return chunk;
            }
            removeChunk(chunkX, chunkZ);
        }
        
        TerrainGenData mainGenerator = terrainGenData.get(terrainGeneratorName);
        if (mainGenerator == null) {
            throw new IllegalArgumentException("TerrainGenData is null");
        }
        
        //location, genDataName, fromTo
        HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage = new HashMap<>();
        
        LocData locData = new LocData(new Location(world, chunkX << 4, 0, chunkZ << 4), 16, 16);
        
        HashMap<String, Object> genModeData = new HashMap<>();
        
        HashMap<String, LinkedList<String>> biomes = new HashMap<>();
        for (String biome : mainGenerator.getBiomeSettings().getBiomes()) {
            LinkedList<String> tmpList = new LinkedList<>();
            tmpList.add(biome);
            biomes.put(biome, getGenerators(tmpList));
        }
        
        HashMap<String, String> biomeMap = new HashMap<>();
        
        for (int x = 0; x < 16; x++) {
            int realX = x + (chunkX << 4);
            for (int z = 0; z < 16; z++) {
                int realZ = z + (chunkZ << 4);
                
                String biome = TerrainGrid.getBiome(realX, realZ, mainGenerator.getBiomeSettings().getBiomes(), mainGenerator, world);
                LinkedList<String> biomeGenerators = biomes.get(biome);
                
                for (String biomeGenerator : biomeGenerators) {
                    TerrainGenData i = terrainGenData.get(biomeGenerator);
                    
                    if (i != null) {
                        generate(x, z, realX, realZ, chunkX, chunkZ, i, mainGenerator, world, genStorage, random, biomeGrid, chunk, locData, genModeData);
                    }
                }
                
                if (mainGenerator.isFastRender()) {
                    useFinalChunkModes(realX, realZ, locData, genStorage, "", genModeData, new TerrainChunkData(chunk, chunkX, chunkZ, x, z, biomeGrid), biomeGenerators);
                } else {
                    //save per coordinate biome (BiomeMap) and get generator
                    biomeMap.put(x + ";" + z, biome);
                }
            }
        }
        
        if (!mainGenerator.isFastRender()) {
            for (int x = 0; x < 16; x++) {
                int realX = x + (chunkX << 4);
                for (int z = 0; z < 16; z++) {
                    int realZ = z + (chunkZ << 4);
                    String biome = biomeMap.get(x + ";" + z);
                    useFinalChunkModes(realX, realZ, locData, genStorage, "", genModeData, new TerrainChunkData(chunk, chunkX, chunkZ, x, z, biomeGrid), biomes.get(biome));
                }
            }
        }
        
        return chunk;
    }
    
    @SuppressWarnings("NullableProblems")
    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
    }
    
    @SuppressWarnings("WeakerAccess")
    public static class TerrainChunkData {
        
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
                setChunkBlock(x, y, z, new TerrainBlockData(material, blockFace, false), chunkData);
            }
        }
        
        public void setBlock(int y, TerrainBlockData terrainBlockData) {
            if (terrainBlockData.getMaterial().isBlock()) {
                setChunkBlock(x, y, z, terrainBlockData, chunkData);
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
