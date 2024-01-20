package ru.sheep.dingus.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestContainer {

    private final List<Quest> quests;
    public List<Quest> values(){
        return new ArrayList<>(quests);
    }
    public QuestContainer(List<Quest> quests){
        this.quests = new ArrayList<>(quests);
    }
    public void generateRequiredQuests(){

        var rnd = new Random();


        for (int i = 0; i < quests.size(); i++) {

            if (rnd.nextDouble() > 0.5) {
                values().get(i).setRequired(true);
            }
        }

    }



}
