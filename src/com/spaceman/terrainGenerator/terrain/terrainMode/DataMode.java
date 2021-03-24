package com.spaceman.terrainGenerator.terrain.terrainMode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public abstract class DataMode<D> extends TerrainMode {

    private D modeData = null;

    public D getModeData() {
        return modeData;
    }

    public void setModeData(D data) {
        this.modeData = data;
    }

    /*
    * used when:
    * /terrain mode edit <name> <TerrainMode name> set <data...>
    * /terrain mode add <name> <name> [data... (is always a new TerrainMode)]
    * /terrain mode set <name> <TerrainMode name> <number> [data... (only for new TerrainModes)]
    * */
    public abstract void setData(LinkedList<String> data, Player player);

    @Override
    public Message dataAsString() {
        return new Message(TextComponent.textComponent(modeData.toString(), ColorFormatter.varInfoColor));
    }

    @Override
    public Message dataAsStringWithHover() {
        return new Message(TextComponent.textComponent(modeData.toString(), ColorFormatter.varInfoColor).setInsertion(" " + getInsertion()));
    }

    public String getInsertion() {
        return null;
    }

    @Override
    public final String modeType() {
        return "DataMode";
    }
}
