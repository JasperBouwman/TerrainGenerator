package com.spaceman.terrainGenerator.fancyMessage.events;

import com.spaceman.terrainGenerator.fancyMessage.Message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface TextEvent {
    
    public String translateJSON(Message.TranslateMode mode);
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface InteractiveTextEvent {
    
    }
}
