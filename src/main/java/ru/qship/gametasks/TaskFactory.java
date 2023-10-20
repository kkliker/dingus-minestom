package ru.qship.gametasks;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import org.jetbrains.annotations.Nullable;
import ru.qship.DingusManager;

import java.util.List;

public class TaskFactory {

    @Getter
    @Setter
    public static class Task extends InterestingLocation {

        private TaskLocation taskLocation;
        private TaskRisk taskRisk;
        private String name;
        private String description;
        private int work_duration;
        private Entity hologramEntity;

        private boolean complete;

        public Task(Pos pos, TaskLocation location, TaskRisk risk, String name, String description, int work_duration) {
            super(pos);
            this.taskLocation = location;
            this.taskRisk = risk;
            this.name = name;
            this.description = description;
            this.work_duration = work_duration;
            this.hologramEntity = null;
            this.complete = false;
        }
    }

    public static class InterestingLocation{
        public Pos pos;
        public InterestingLocation(Pos pos){
            this.pos = pos;
        }
        public InterestingLocation(double x,double y,double z){
            this.pos = new Pos(x,y,z);
        }
    }


    public enum TaskLocation{

        Street,
        Garden,
        Beach,
        Rock,
        Store

    }

    public enum TaskRisk{

        White,
        Red
    }

    @Nullable
    public static TaskFactory.Task getClosestTask(Entity player){

        List<Task> task = DingusManager.getCurrentTasks();

        Task closestPos = task.get(0);
        double distance =  player.getPosition().distanceSquared(closestPos.pos);;

        for (Task location : task) {
            double distanceSquared = player.getDistanceSquared(location.pos);

            if (distanceSquared < distance){
                closestPos = location;
                distance = distanceSquared;
            }
        }

        if(distance > 1.5){
            return null;
        }

        return closestPos;
    }

}
