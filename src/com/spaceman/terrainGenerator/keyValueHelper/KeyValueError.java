package com.spaceman.terrainGenerator.keyValueHelper;

import com.spaceman.terrainGenerator.fancyMessage.Message;
import org.bukkit.entity.Player;

import static com.spaceman.terrainGenerator.fancyMessage.TextComponent.textComponent;

public class KeyValueError extends Exception {
    
    private Message message;
    
    public KeyValueError(String message) {
        this.message = new Message(textComponent(message));
    }
    
    public KeyValueError(Message message) {
        this.message = message;
    }
    
    public void sendMessage(Player player) {
        message.sendMessage(player);
    }
    
}
