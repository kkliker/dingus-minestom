package ru.qship.managers;

import net.kyori.adventure.text.Component;
import ru.qship.gametasks.TaskFactory;


public class TaskNameUtility {


    public static Component get(TaskFactory.Task task, int work){

        String complete_color = "&a";
        if(task.getTaskRisk() == TaskFactory.TaskRisk.Red){
            complete_color = "&c";
        }
        if(task.getWork_duration() - work < 0){
            return color.fromLegacy(
                    "&e[Shift] &f" +task.getName() + "\n" +
                            "&7" + "█".repeat(task.getWork_duration()));
        }
        return color.fromLegacy(
                "&e[Shift] &f" +task.getName() + "\n" +
                        complete_color + "█".repeat(work) + "&7" + "█".repeat(task.getWork_duration() - work));
    }

}
