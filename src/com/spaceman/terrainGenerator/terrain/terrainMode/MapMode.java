package com.spaceman.terrainGenerator.terrain.terrainMode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;
import static com.spaceman.terrainGenerator.fancyMessage.events.HoverEvent.hoverEvent;

public abstract class MapMode<V, D> extends TerrainMode {

    private LinkedHashMap<V, D> modeData = new LinkedHashMap<>();

    public LinkedHashMap<V, D> getModeData() {
        return modeData;
    }
    
    public void setModeData(LinkedHashMap<V, D> data) {
        this.modeData = data;
    }
    
    /* used when:
     * /terrain mode edit <name> <TerrainMode name> add <data...>
     * */
    public abstract void addData(LinkedList<String> data, Player player);
    
    /* used when:
     * /terrain mode edit <name> <TerrainMode name> remove <data...>
     * */
    public abstract void removeData(LinkedList<String> data, Player player);
    
    /* used when:
     * /terrain mode edit <name> <TerrainMode name> set <data...>
     * /terrain mode add <name> <name> [data... (is always a new TerrainMode)]
     * /terrain mode set <name> <TerrainMode name> <number> [data... (only for new TerrainModes)]
     * /terrain mode edit <name> <TerrainMode name> set <number> <data...>
     * */
    public abstract void setData(LinkedList<String> data, int number, Player player);

    public Collection<String> tabListAdd(String[] args, Player player) {
        return Collections.emptyList();
    }

    public Collection<String> tabListRemove(String[] args, Player player) {
        return Collections.emptyList();
    }

    @Override
    public Message dataAsString() {
        Message hEvent = new Message(textComponent("\n"));
        boolean b = true;
        for (V value : modeData.keySet()) {
            if (b) {
                hEvent.addText(textComponent(value.toString(), ColorFormatter.varInfoColor));
                hEvent.addText(textComponent(" = ", ColorFormatter.infoColor));
                hEvent.addText(textComponent(modeData.get(value).toString(), ColorFormatter.varInfoColor));
            } else {
                hEvent.addText(textComponent(value.toString(), ColorFormatter.varInfo2Color));
                hEvent.addText(textComponent(" = ", ColorFormatter.infoColor));
                hEvent.addText(textComponent(modeData.get(value).toString(), ColorFormatter.varInfo2Color));
            }
            hEvent.addText(textComponent(",\n", ColorFormatter.infoColor));
            b = !b;
        }
        hEvent.removeLast();
        return hEvent;
    }

    @Override
    public Message dataAsStringWithHover() {
        Message message = new Message();
        message.addText("");
        boolean b = true;
        for (V value : modeData.keySet()) {
            if (b) {
                message.addText(textComponent(value.toString(), ColorFormatter.varInfoColor, hoverEvent(textComponent(modeData.get(value).toString(), ColorFormatter.varInfoColor)))
                        .setInsertion(" " + getInsertion(value, modeData.get(value))));
            } else {
                message.addText(textComponent(value.toString(), ColorFormatter.varInfo2Color, hoverEvent(textComponent(modeData.get(value).toString(), ColorFormatter.varInfo2Color)))
                        .setInsertion(" " + getInsertion(value, modeData.get(value))));
            }
            message.addText(textComponent(",\n", ColorFormatter.infoColor));
            b = !b;
        }
        message.removeLast();

        return message;
    }

    public String getInsertion(V v, D d) {
        return null;
    }

    @Override
    public final String modeType() {
        return "MapMode";
    }
}
