package ru.qship;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.chunk.ChunkUtils;
import ru.qship.auth.DAuth;
import ru.qship.managers.DDymensionType;
import ru.qship.managers.DebugLogger;
import ru.qship.managers.InstanceHelper;
import ru.qship.managers.RayFastManager;
import ru.qship.task.GameTasksTask;
import ru.qship.task.IntersectTask;
import ru.qship.task.SessionsTask;

import java.util.concurrent.CompletableFuture;

public class Main {

    @Getter
    private static final ServerConfig cfg = new ServerConfig();

    public static void main(String[] args) {

        MinecraftServer minecraftServer = MinecraftServer.init();

        // Add an event callback to specify the spawning instance (and the spawn position)

        minecraftServer.start("0.0.0.0", getCfg().getPORT());

        MinecraftServer.getDimensionTypeManager().addDimension(DDymensionType.DINGUS);

        {
            InstanceHelper.init();
            DAuth.init();
            RayFastManager.init();

            PlayerEvents.registerEvents();
            DingusManager.initTasksHolograms();

            registerTasks();
        }

        DingusManager.spawnNPC();
    }

    private static void registerTasks() {
        MinecraftServer.getSchedulerManager().scheduleTask(new GameTasksTask(), TaskSchedule.seconds(0), TaskSchedule.tick(4), ExecutionType.SYNC);
        MinecraftServer.getSchedulerManager().scheduleTask(new IntersectTask(), TaskSchedule.seconds(0), TaskSchedule.tick(1), ExecutionType.ASYNC);
        MinecraftServer.getSchedulerManager().scheduleTask(new SessionsTask(), TaskSchedule.seconds(0), TaskSchedule.tick(20), ExecutionType.ASYNC);
    }

    private static void preloadSpawnChunks() {
        ChunkUtils.forChunksInRange(new Pos(-392, 69, 678), 8, (c, s) -> {
            if (!InstanceHelper.getOverworld().isChunkLoaded(c, s)) {
                CompletableFuture.runAsync(() -> {
                    InstanceHelper.getOverworld().loadChunk(c, s);
                    DebugLogger.info("Preparing spawn chunks....Please wait");
                });
            }
        });
        DebugLogger.info("Loaded!");
    }

}

    /*
    Set.of(
  new Vec(1, 0, 0), // walk left
  new Vec(-1, 0, 0), // walk right
  new Vec(0, 0, 1), // walk forwards
  new Vec(0, 0, -1), // walk backwards
  new Vec(0, 1, 0) // jump up
);
     */




