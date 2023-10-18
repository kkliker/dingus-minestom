package qship;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;

public class TaskFactory {

    public static class Task extends InterestingLocation {

        public TaskLocation taskLocation;
        public TaskRisk taskRisk;
        public String name;
        public String description;
        public int work_duration;
        public Entity hologramEntity;

        public Task(Pos pos, TaskLocation location, TaskRisk risk, String name, String description, int work_duration) {
            super(pos);
            this.taskLocation = location;
            this.taskRisk = risk;
            this.name = name;
            this.description = description;
            this.work_duration = work_duration;
            this.hologramEntity = null;
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

        WHITE,
        RED
    }

    @Nullable
    public static TaskFactory.Task getClosestTask(Player player){

        TaskFactory.Task closestPos = Dingus.TASKS.get(0);
        double distance =  player.getPosition().distanceSquared(closestPos.pos);;

        for (TaskFactory.Task location : Dingus.TASKS) {
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
