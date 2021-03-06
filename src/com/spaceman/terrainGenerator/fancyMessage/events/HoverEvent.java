package com.spaceman.terrainGenerator.fancyMessage.events;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import com.spaceman.terrainGenerator.fancyMessage.TextComponent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

@TextEvent.InteractiveTextEvent
public class HoverEvent implements TextEvent {
    
    private ArrayList<TextComponent> text = new ArrayList<>();
    
    public HoverEvent() {
    }
    
    public HoverEvent(TextComponent textComponent) {
        textComponent.clearInteractiveEvents();
        this.text.add(textComponent);
    }
    
    public static HoverEvent hoverEvent(String simpleText) {
        return hoverEvent(textComponent(simpleText));
    }
    
    public static HoverEvent hoverEvent(String simpleText, ChatColor color) {
        return hoverEvent(textComponent(simpleText, color));
    }
    
    public static HoverEvent hoverEvent(TextComponent... textComponent) {
        HoverEvent hEvent = new HoverEvent();
        for (TextComponent text : textComponent) {
            hEvent.addText(text);
        }
        return hEvent;
    }
    
    @Override
    public String translateJSON(Message.TranslateMode mode) {
        String q = mode.getQuote();
        return "" + q + "hoverEvent" + q + ":{" + q + "action" + q + ":" + q + "show_text" + q + "," + q + "value" + q + ":[" +
                this.text.stream().map(t -> t.translateJSON(mode)).collect(Collectors.joining(",")) +
                "]}";
    }
    
    public void addMessage(Message message) {
        if (message != null) {
            for (TextComponent textComponent : message.getText()) {
                addText(textComponent);
            }
        }
    }
    
    public void addText(TextComponent... text) {
        for (TextComponent textComponent : text) {
            textComponent.clearInteractiveEvents();
            this.text.add(textComponent);
        }
    }
    
    public void addText(String simpleText) {
        this.text.add(textComponent(simpleText));
    }
    
    public void addText(String simpleText, ChatColor color) {
        this.text.add(textComponent(simpleText, color));
    }
    
    public ArrayList<TextComponent> getText() {
        return text;
    }
    
    public void removeLast() {
        this.text.remove(text.size() - 1);
    }
    
}

