package ru.sheep.dingus.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ru.sheep.dingus.quests.Quest;
import ru.sheep.dingus.quests.QuestRisk;


public class ComponentUtil {


    public static Component fromLegacy(String legacyText) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(legacyText);
    }

    public static Component task_name(Quest quest, int work){

        String complete_color = "&a";
        if(quest.getQuestRisk() == QuestRisk.RED){
            complete_color = "&c";
        }
        if(quest.getWork_duration() - work < 0){
            return fromLegacy(
                    "&e[Shift] &f" + quest.getDescription() + "\n" +
                            "&7" + "█".repeat(quest.getWork_duration()));
        }
        return fromLegacy(
                "&e[Shift] &f" + quest.getDescription() + "\n" +
                        complete_color + "█".repeat(work) + "&7" + "█".repeat(quest.getWork_duration() - work));
    }

}
