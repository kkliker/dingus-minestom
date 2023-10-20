package ru.qship.gametasks;

import net.minestom.server.coordinate.Pos;

import java.util.ArrayList;
import java.util.List;

public class TaskList {


    public static List<TaskFactory.Task> TASKS() {

        return new ArrayList<>(List.of(
                new TaskFactory.Task(
                        new Pos(11.5, 103, -28.6),
                        TaskFactory.TaskLocation.Rock,
                        TaskFactory.TaskRisk.White,
                        "celebaroty yipeee",
                        "Interact",
                        8),
                new TaskFactory.Task(
                        new Pos(26.5, 102, -24),
                        TaskFactory.TaskLocation.Rock,
                        TaskFactory.TaskRisk.White,
                        "wave at tha Gaberro",
                        "Interact",
                        8),
                new TaskFactory.Task(
                        new Pos(7, 100, 6),
                        TaskFactory.TaskLocation.Street,
                        TaskFactory.TaskRisk.White,
                        "dancing",
                        "Interact",
                        8),
                new TaskFactory.Task(
                        new Pos(8, 100, 20),
                        TaskFactory.TaskLocation.Street,
                        TaskFactory.TaskRisk.White,
                        "idk",
                        "Interact",
                        8),

                // Beach

                new TaskFactory.Task(
                        new Pos(18, 97, -20.5),
                        TaskFactory.TaskLocation.Beach,
                        TaskFactory.TaskRisk.White,
                        "doing something on anclove",
                        "Interact",
                        8),

                new TaskFactory.Task(
                        new Pos(23, 97, -11),
                        TaskFactory.TaskLocation.Beach,
                        TaskFactory.TaskRisk.White,
                        "relax",
                        "Interact",
                        8),
                new TaskFactory.Task(
                        new Pos(21, 97, 3),
                        TaskFactory.TaskLocation.Beach,
                        TaskFactory.TaskRisk.White,
                        "another relax",
                        "Interact",
                        8),
                new TaskFactory.Task(
                        new Pos(17, 97, 9),
                        TaskFactory.TaskLocation.Beach,
                        TaskFactory.TaskRisk.White,
                        "laugh at this goober",
                        "Interact",
                        8),
                new TaskFactory.Task(
                        new Pos(9.2, 100, -9.5),
                        TaskFactory.TaskLocation.Street,
                        TaskFactory.TaskRisk.Red,
                        "broke the street light",
                        "Interact",
                        8)

        ));
    }

    // 18 97 -20.5 | 23 97 -11 | 21 97 3 | 17 97 9
    // 9.2 100 -9.5

    public static List<TaskFactory.InterestingLocation> NPCWALKPOS(){

        return new ArrayList<>(List.of(
                new TaskFactory.InterestingLocation(7.5, 100, 7.5),
                new TaskFactory.InterestingLocation(7.5, 100, -7.5),
                new TaskFactory.InterestingLocation(-7.5, 100, -7.5),
                new TaskFactory.InterestingLocation(-7.5, 100, 7.5),

                new TaskFactory.InterestingLocation(20, 97, -5.3),
                new TaskFactory.InterestingLocation(20, 97, 7),
                new TaskFactory.InterestingLocation(16, 97, 9),
                new TaskFactory.InterestingLocation(8.5, 100, 3),

                new TaskFactory.InterestingLocation(15, 100, -15),
                new TaskFactory.InterestingLocation(-21, 100, -24),
                new TaskFactory.InterestingLocation(-21, 100, -13),

                new TaskFactory.InterestingLocation(-26, 100, -28.5),
                new TaskFactory.InterestingLocation(-23, 100, -8)));
    }

}
