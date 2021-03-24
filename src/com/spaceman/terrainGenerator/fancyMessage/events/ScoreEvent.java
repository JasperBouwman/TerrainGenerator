package com.spaceman.terrainGenerator.fancyMessage.events;

import com.spaceman.terrainGenerator.fancyMessage.Message;

public class ScoreEvent implements TextEvent {
    
    private String name;
    private String objective;
    
    public ScoreEvent() {
    }
    
    public ScoreEvent(String name, String objective) {
        this.name = name;
        this.objective = objective;
    }
    
    public static ScoreEvent scoreEvent(String name, String objective) {
        return new ScoreEvent(name, objective);
    }
    
    public String translateJSON(Message.TranslateMode mode) {
        String q = mode.getQuote();
        return String.format(q +"score" + q + ":{" + q + "name" + q + ":" + q + "%s" + q + "," + q + "objective" + q + ":" + q + "%s" + q + "}", name, objective);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setObjective(String objective) {
        this.objective = objective;
    }
    
    public String getName() {
        return name;
    }
    
    public String getObjective() {
        return objective;
    }
}
