package ru.sheep.dingus.ticks;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import ru.sheep.dingus.Dingus;
import ru.sheep.dingus.UnitedAPI;
import ru.sheep.dingus.api.InstanceHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TicksScheduler {

    public static void run(){

        IntersectTicker intersectTicker = new IntersectTicker();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() ->{
            intersectTicker.tick();
        }, 0, 100, TimeUnit.MILLISECONDS);

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{
            new SessionsTicker(Dingus.getSession(), InstanceHelper.getOverworld()).tick();
        },TaskSchedule.seconds(0),TaskSchedule.tick(2),ExecutionType.ASYNC);

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{
            new AreaParticlesTicker(Dingus.getSession().questContainer().values()).tick();
        },TaskSchedule.seconds(0),TaskSchedule.tick(5),ExecutionType.ASYNC);

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{
            new QuestCompletetionsTicker().tick();
        },TaskSchedule.seconds(0),TaskSchedule.tick(5),ExecutionType.SYNC);

    }

}
