package com.spaceman.terrainGenerator.terrain.terrainMode;

import com.spaceman.terrainGenerator.ColorFormatter;
import com.spaceman.terrainGenerator.fancyMessage.Message;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public abstract class ArrayMode<D> extends TerrainMode {

    private LinkedList<D> modeData = new LinkedList<>();

    public LinkedList<D> getModeData() {
        return modeData;
    }

    public void setModeData(LinkedList<D> data) {
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
        for (D data : modeData) {
            if (b) {
                hEvent.addText(textComponent(data.toString(), ColorFormatter.varInfoColor));
            } else {
                hEvent.addText(textComponent(data.toString(), ColorFormatter.varInfo2Color));
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
        for (D data : modeData) {
            if (b) {
                message.addText(textComponent(data.toString(), ColorFormatter.varInfoColor).setInsertion(" " + getInsertion(data)));
            } else {
                message.addText(textComponent(data.toString(), ColorFormatter.varInfo2Color).setInsertion(" " + getInsertion(data)));
            }
            message.addText(textComponent(",\n", ColorFormatter.infoColor));
            b = !b;
        }
        message.removeLast();

        return message;
    }

    public String getInsertion(D d) {
        return null;
    }

    @Override
    public final String modeType() {
        return "ArrayMode";
    }
}
