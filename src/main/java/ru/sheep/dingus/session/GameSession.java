package ru.sheep.dingus.session;

import ru.sheep.dingus.Dingus;
import ru.sheep.dingus.GlobalSidebar;
import ru.sheep.dingus.UnitedAPI;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.domain.WinningType;
import ru.sheep.dingus.quests.Quest;
import ru.sheep.dingus.quests.QuestContainer;
import ru.sheep.dingus.ticks.SessionsTicker;

import java.util.ArrayList;
import java.util.List;


/**
 * @param duration in seconds
 */
public record GameSession(QuestContainer questContainer, long startTime, int huntersSize, int duration, int playersSize,
                          int hidersSize) {

    public void start(){
        questContainer.generateRequiredQuests();
      //  Dingus.reset();
        Dingus.getGlobalSidebar().update(questContainer.values());
    }
    public long timeInSeconds(){
        return (System.currentTimeMillis() - startTime) / 1000;
    }
    public WinningType checkForEnd() {

        // check for time left
        if ((System.currentTimeMillis() - startTime) / 1000 > duration) return WinningType.HUNTERS_TIME;

        List<Quest> questList = questContainer.values();

        // check for completing quests
        if (UnitedAPI.isQuestsCompleted(questList)) return WinningType.HIDERS_QUESTS;

        // check for genocide
        if (UnitedAPI.isGenocide(DingusPlayer.players())) return WinningType.HUNTERS_GENOCIDE;


        return WinningType.NONE;
    }


}
