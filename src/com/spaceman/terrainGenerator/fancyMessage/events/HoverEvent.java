package com.spaceman.terrainGenerator.fancyMessage.events;

import com.spaceman.terrainGenerator.fancyMessage.TextComponent;

import java.util.ArrayList;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class HoverEvent {

    private ArrayList<TextComponent> text = new ArrayList<>();

    public HoverEvent(TextComponent textComponent) {
        textComponent.clearEvents();
        this.text.add(textComponent);
    }

    public static HoverEvent hoverEvent(String simpleText) {
        return hoverEvent(textComponent(simpleText));
    }

    public static HoverEvent hoverEvent(TextComponent textComponent) {
        return new HoverEvent(textComponent);
    }

    public void addText(TextComponent textComponent) {
        textComponent.clearEvents();
        this.text.add(textComponent);
    }

    public void removeLast() {
        text.remove(text.size() -1);
    }

    public ArrayList<TextComponent> getText() {
        return text;
    }
}
