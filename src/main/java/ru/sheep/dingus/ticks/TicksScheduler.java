package ru.sheep.dingus.ticks;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import ru.sheep.dingus.Dingus;
import ru.sheep.dingus.UnitedAPI;
import ru.sheep.dingus.api.InstanceHelper;

public class TicksScheduler {

    public static void run(){

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{
          new IntersectTicker().tick();
        },TaskSchedule.seconds(0),TaskSchedule.nextTick(),ExecutionType.ASYNC);

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{
            new SessionsTicker(Dingus.getSession(), InstanceHelper.getOverworld()).tick();
        },TaskSchedule.seconds(0),TaskSchedule.tick(2),ExecutionType.ASYNC);

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{
            new AreaParticlesTicker(Dingus.getSession().questContainer().values()).tick();
        },TaskSchedule.seconds(0),TaskSchedule.tick(5),ExecutionType.ASYNC);

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{
            new QuestCompletetionsTicker().tick();
        },TaskSchedule.seconds(0),TaskSchedule.tick(5),ExecutionType.ASYNC);

    }

}
