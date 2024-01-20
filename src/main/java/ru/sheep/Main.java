package ru.sheep;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.utils.chunk.ChunkUtils;
import ru.sheep.dingus.Dingus;
import ru.sheep.dingus.ServerConfig;
import ru.sheep.dingus.UnitedAPI;
import ru.sheep.dingus.DAuth;
import ru.sheep.dingus.api.DDymensionType;
import ru.sheep.dingus.api.DebugLogger;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.api.RayFastManager;
import ru.sheep.dingus.quests.PreconfiguredTasks;
import ru.sheep.dingus.commands.CMDCLEAR;
import ru.sheep.dingus.ticks.TicksScheduler;

import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {

        ServerConfig.init();

        MinecraftServer minecraftServer = MinecraftServer.init();
        minecraftServer.start("0.0.0.0", ServerConfig.port());

        MinecraftServer.getDimensionTypeManager().addDimension(DDymensionType.DINGUS);

        {
            InstanceHelper.init();
            DAuth.init();
            RayFastManager.init();

            PlayerEvents.registerEvents();

            preloadSpawnChunks();
        }

        PreconfiguredTasks.load();

        UnitedAPI.initiateDingus();

        TicksScheduler.run();
        Dingus.spawnNPC();

        Dingus.getGlobalSidebar().update(PreconfiguredTasks.tasks_towny());

        UnitedAPI.initTasksHolograms();

        MinecraftServer.getCommandManager().register(new CMDCLEAR());
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




