package ru.sheep.dingus.quests;

public enum QuestRisk {

    WHITE,
    RED;

    public static QuestRisk valueOfWithIgnoreCase(String str){
        for (QuestRisk risk : QuestRisk.values()){
            if(risk.toString().equalsIgnoreCase(str)) return risk;
        }
        return null;
    }
}
