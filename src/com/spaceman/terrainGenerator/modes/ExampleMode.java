package com.spaceman.terrainGenerator.modes;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.terrain.TerrainBlockData;
import com.spaceman.terrainGenerator.terrain.TerrainGenData;
import com.spaceman.terrainGenerator.terrain.TerrainUtils;
import com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator;
import com.spaceman.terrainGenerator.terrain.generators.WorldGenerator;
import com.spaceman.terrainGenerator.terrain.terrainMode.DataMode;
import com.spaceman.terrainGenerator.terrain.terrainMode.TerrainMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.terrain.TerrainCore.setType;
import static com.spaceman.terrainGenerator.terrain.generators.TerrainGenerator.GenData.*;

public class ExampleMode extends DataMode<TerrainBlockData> {
    
    @Override
    public String getInsertion() {
        return "m=" + getModeData().getMaterial().name() +
                ",d=" + getModeData().getBlockFace().name() +
                ",w=" + getModeData().isWaterLogged();
    }
    
    @Override
    public Collection<String> tabListCreate(String[] args, Player player) {
        return TerrainUtils.tabListCreateAndSetTerrainBlockData(args);
    }
    
    @Override
    public Collection<String> tabListSet(String[] args, Player player) {
        return TerrainUtils.tabListCreateAndSetTerrainBlockData(args);
    }
    
    @Override
    public Message dataAsString() {
        return super.dataAsString(); //the default is fine for this
    }
    
    @Override
    public Message dataAsStringWithHover() {
        return super.dataAsStringWithHover(); //the default is fine for this
    }
    
    @Override
    public void setData(LinkedList<String> data, Player player) {
        TerrainUtils.setDataTerrainBlockData(data, player, this);
    }
    
    @Override
    public String getModeName() {
        return "ExampleMode";
    }
    
    @Override
    public boolean isFinalMode() {
        return false;
    }
    
    @Override
    public String getModeDescription() {
        return "The example TerrainMode";
    }
    
    @Override
    public void saveMode(ConfigurationSection section) {
        if (this.getModeData() != null) {
            section.set("data", this.getModeData());
        }
    }
    
    @Override
    public TerrainMode loadMode(ConfigurationSection section) {
        TerrainBlockData data = (TerrainBlockData) section.get("data");
        this.setModeData(data);
        return this;
    }
    
    @Override
    public void useMode(int x, int z, HashMap<String, HashMap<String, TerrainGenerator.GenData>> genStorage,
                        TerrainGenerator.LocData locData, TerrainGenData data, String savePath, HashMap<String, Object> genModeData, WorldGenerator.TerrainChunkData chunkData) {
    
        //check is material type is not Material.STRUCTURE_VOID
        //I use STRUCTURE_VOID the same as Minecraft does, a material that is ignored
        if (!getModeData().getMaterial().equals(Material.STRUCTURE_VOID)) {
            
            //get the generation data for your TerrainGenerator
            TerrainGenerator.GenData genData = getGenData(x, z, data.getName() + savePath, genStorage);
            //get the highest block of the total terrain (including all generated TerrainGenerators), this can change after the next generator generated above it
            int highest = getHighest(x, z, genStorage);
            
            //check if your are the top one, and if above 0
            if (genData.getHeightGen() == highest && genData.getHeightGen() > 0 ||
                    //get all air pockets and check if the next block above your is not generated
                    getAirPockets(x, z, genStorage, genData.getStartGen(), highest).contains(genData.getHeightGen() + 1)) {
                
                //check if the bottom is not above the top layer, if this is true (bottom > top) there won't be any generation
                if (genData.getStartGen() < genData.getHeightGen()) {
                    int y = genData.getHeightGen(); //get the top layer (to change)
                
                    if (chunkData != null) { //check if its a world generation, of terrain generation
                        //change the block to the TerrainMode its block in that chunk
                        chunkData.setBlock(y, getModeData());
                    } else {
                        //change the block to the TerrainMode its block in the world
                        setType(new Location(locData.getWorld(), x, y, z).getBlock(), getModeData());
                    }
                }
            }
        }
    }
}
