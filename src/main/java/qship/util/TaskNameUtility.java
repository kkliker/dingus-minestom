package qship.util;

import net.kyori.adventure.text.Component;
import qship.TaskFactory;

public class TaskNameUtility {


    public static Component get(TaskFactory.Task task, int work){

        String complete_color = "&a";
        if(task.taskRisk == TaskFactory.TaskRisk.RED){
            complete_color = "&c";
        }

        if(task.work_duration - work < 0){
            return color.fromLegacy(
                    "&e[Shift] &f" +task.name + "\n" +
                            "&7" + "█".repeat(task.work_duration));
        }
        return color.fromLegacy(
                "&e[Shift] &f" +task.name + "\n" +
                        complete_color + "█".repeat(work) + "&7" + "█".repeat(task.work_duration - work));


    }

}
