package qship;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;

import net.minestom.server.instance.InstanceManager;

import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import qship.commands.GamemodeCommand;
import qship.commands.SaveAllCommand;
import qship.events.MoveEvent;
import qship.fakeplayers.FakeNPC;
import qship.session.DingusPlayer;
import qship.task.FakePlayersTask;
import qship.task.GameSessionTask;
import qship.task.IntersectTask;
import qship.task.TasksTask;
import qship.util.GsonManager;
import qship.util.RayFastManager;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class Main {


    public static HashMap<UUID, DingusPlayer> dingusMap = new HashMap<>();
    private static Instance mainInstace;
    final static Random random = new Random();
    public static Sidebar sidebar;


    // 8.5 100 26.5 | 13.5 97 0.5 | 23.5 102 -28.5
    final static List<Pos> spawnPos = List.of(
            new Pos(8.5,100,26.5),
            new Pos(23.5 ,102 ,-28.5),
            new Pos(13.5 ,97 ,0.5)
            );
    final static Integer spawnPosSize = spawnPos.size();

    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Create the instance
        DimensionType litDimension = DimensionType.builder(NamespaceID.from("test:light"))
                .ambientLight(1.0f)
                .logicalHeight(400)
                .height(400)
                .minY(-80)
                .build();
        MinecraftServer.getDimensionTypeManager().addDimension(litDimension);

        mainInstace = instanceManager.createInstanceContainer(litDimension,new AnvilLoader("./world/"));
        mainInstace.enableAutoChunkLoad(true);
        for(int x = -6; x <= 6; x++){
            for(int z = -6; z <= 6; z++){
                mainInstace.loadChunk(x,z);
            }
        }
        sidebar = Dingus.SIDEBAR();

        // Add an event callback to specify the spawning instance (and the spawn position)
        Dingus.registerEvents();
        Dingus.initTasksHolograms();
        registerTasks();
        RayFastManager.init();
        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", GsonManager.getPort());


        MoveEvent.register();
        spawnNPC();

        MinecraftServer.getCommandManager().register(new SaveAllCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());

        // hologram entity register

    }

    private static void registerTasks(){
        MinecraftServer.getSchedulerManager().scheduleTask(new FakePlayersTask(), TaskSchedule.seconds(0),TaskSchedule.seconds(1),ExecutionType.ASYNC);
        MinecraftServer.getSchedulerManager().scheduleTask(new TasksTask(), TaskSchedule.seconds(0),TaskSchedule.tick(2),ExecutionType.ASYNC);
        MinecraftServer.getSchedulerManager().scheduleTask(new IntersectTask(), TaskSchedule.seconds(0),TaskSchedule.nextTick(),ExecutionType.ASYNC);
        MinecraftServer.getSchedulerManager().scheduleTask(new GameSessionTask(), TaskSchedule.seconds(0),TaskSchedule.tick(20),ExecutionType.ASYNC);
    }

    public static void spawnNPC(){

        MinecraftServer.getSchedulerManager().scheduleNextProcess(() ->{

        for(int x = 0; x <= Dingus.MINNPC; x++){

            Pos sp = spawnPos.get(random.nextInt(spawnPosSize));

            FakeNPC fakeNPC = new FakeNPC();
            fakeNPC.setInstance(Main.getInstance(),sp);
        }

        }, ExecutionType.ASYNC);


    }

    public static Instance getInstance(){
        return mainInstace;
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

}
