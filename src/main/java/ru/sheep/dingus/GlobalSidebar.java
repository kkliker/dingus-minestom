package ru.sheep.dingus;

import lombok.Getter;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import ru.sheep.dingus.quests.PreconfiguredTasks;
import ru.sheep.dingus.quests.Quest;
import ru.sheep.dingus.quests.QuestRisk;

import java.util.ArrayList;
import java.util.List;

import static ru.sheep.dingus.api.ComponentUtil.fromLegacy;

@Getter
public class GlobalSidebar {

    private Sidebar sidebar = new Sidebar(fromLegacy("&f&lЗадания"));

    public void update(List<Quest> questList){
        clearLines(sidebar);
        System.out.println("exec");
        for (int x = 0; x < questList.size(); x++){
            Quest quest = questList.get(x);
            if(!quest.isComplete() && quest.isRequired()) {
                //name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(); useless code
                System.out.println(x);
                if (quest.getQuestRisk() == QuestRisk.RED) {
                    sidebar.createLine(new Sidebar.ScoreboardLine(String.valueOf(x + 1), fromLegacy(String.format("&c&l%s ▍ %s", quest.getName(), quest.getDescription())), x + 1));
                } else {
                    sidebar.createLine(new Sidebar.ScoreboardLine(String.valueOf(x + 1), fromLegacy(String.format("&f&l%s ▍ %s", quest.getName(), quest.getDescription())), x + 1));
                }
            }
        }
    }

    public void show(List<Player> entities){
        entities.forEach(e ->{
            if(e == null) return;
            sidebar.removeViewer(e);
            sidebar.addViewer(e);
        });
    }
    public void clearLines(Sidebar sidebar){
        for (int x = 0; x < 100; x++){
            sidebar.removeLine(String.valueOf(x + 1));
        }
    }

}
