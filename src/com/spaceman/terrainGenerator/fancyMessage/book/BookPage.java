package com.spaceman.terrainGenerator.fancyMessage.book;

import com.spaceman.terrainGenerator.fancyMessage.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;

public class BookPage {

    private ArrayList<TextComponent> text = new ArrayList<>();

    public BookPage() {
    }

    public BookPage(TextComponent text) {
        this.text.add(text);
    }

    public static BookPage newBookPage() {
        return new BookPage();
    }

    public static BookPage newBookPage(TextComponent text) {
        return new BookPage(text);
    }

    public void addText(TextComponent... text) {
        this.text.addAll(Arrays.asList(text));
    }

    public ArrayList<TextComponent> getText() {
        return text;
    }

    public void removeLast() {
        text.remove(text.size() - 1);
    }

    public void addText(String simpleText) {
        addText(TextComponent.textComponent(simpleText));
    }

    public void addWhiteSpace() {
        addText(" ");
    }
}