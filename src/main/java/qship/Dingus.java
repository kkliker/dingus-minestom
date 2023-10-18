package qship;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.CrossbowMeta;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.scoreboard.Team;
import qship.fakeplayers.FakeNPC;
import qship.session.DingusPlayer;
import qship.session.Role;
import qship.util.GsonManager;
import qship.util.TaskNameUtility;
import qship.util.color;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Dingus {

    final static Integer MINNPC = GsonManager.getNPCCount();
    public final static Team NPC_TEAM = MinecraftServer.getTeamManager().createBuilder("NPC_TEAM")
            .collisionRule(TeamsPacket.CollisionRule.NEVER)
            .updateNameTagVisibility(TeamsPacket.NameTagVisibility.HIDE_FOR_OTHER_TEAMS)
            .teamColor(NamedTextColor.RED)
            .build();
    public static final Team HIDERS_TEAM = MinecraftServer.getTeamManager().createBuilder("HIDERS_TEAM")
            .collisionRule(TeamsPacket.CollisionRule.NEVER)
            .updateNameTagVisibility(TeamsPacket.NameTagVisibility.HIDE_FOR_OTHER_TEAMS)
            .build();
    public static final Team HUNTERS_TEAM = MinecraftServer.getTeamManager().createBuilder("HUNTERS_TEAM")
            .collisionRule(TeamsPacket.CollisionRule.NEVER)
            .updateNameTagVisibility(TeamsPacket.NameTagVisibility.HIDE_FOR_OTHER_TEAMS)
            .teamColor(NamedTextColor.BLACK)
            .build();
    public static final Sidebar SIDEBAR(){
        Sidebar sidebar = new Sidebar(color.fromLegacy("&f&lЗадания"));
        sidebar.createLine(new Sidebar.ScoreboardLine(" ",color.fromLegacy("&f&lСад ▍ Отпиздить"),1));

        return sidebar;
    }

    private final static List<TaskFactory.Task> TASKS() {

        List<TaskFactory.Task> list = new ArrayList<>();
        list.addAll(List.of(
            new TaskFactory.Task(
                    new Pos(11.5, 103, -28.6),
                    TaskFactory.TaskLocation.Rock,
                    TaskFactory.TaskRisk.WHITE,
                    "celebaroty yipeee",
                    "Interact",
                    8),
            new TaskFactory.Task(
                    new Pos(26.5, 102, -24),
                    TaskFactory.TaskLocation.Rock,
                    TaskFactory.TaskRisk.WHITE,
                    "wave at tha Gaberro",
                    "Interact",
                    8),
            new TaskFactory.Task(
                    new Pos(7, 100, 6),
                    TaskFactory.TaskLocation.Street,
                    TaskFactory.TaskRisk.WHITE,
                    "dancing",
                    "Interact",
                    8),
            new TaskFactory.Task(
                    new Pos(8, 100, 20),
                    TaskFactory.TaskLocation.Street,
                    TaskFactory.TaskRisk.WHITE,
                    "idk",
                    "Interact",
                    8),

            // Beach

            new TaskFactory.Task(
                new Pos(18, 97 ,-20.5),
                TaskFactory.TaskLocation.Beach,
                TaskFactory.TaskRisk.WHITE,
                "doing something on anclove",
                "Interact",
                    8),

            new TaskFactory.Task(
                        new Pos(23, 97, -11),
                        TaskFactory.TaskLocation.Beach,
                        TaskFactory.TaskRisk.WHITE,
                        "relax",
                        "Interact",
                    8),
            new TaskFactory.Task(
                        new Pos(21 ,97, 3),
                        TaskFactory.TaskLocation.Beach,
                        TaskFactory.TaskRisk.WHITE,
                        "another relax",
                        "Interact",
                    8),
            new TaskFactory.Task(
                        new Pos(17 ,97 ,9),
                        TaskFactory.TaskLocation.Beach,
                        TaskFactory.TaskRisk.WHITE,
                        "laugh at this goober",
                        "Interact",
                    8),
            new TaskFactory.Task(
                        new Pos(9.2, 100 ,-9.5),
                        TaskFactory.TaskLocation.Street,
                        TaskFactory.TaskRisk.RED,
                        "broke the street light",
                        "Interact",
                        8)

            )
        );

        return list;
    }

    // 18 97 -20.5 | 23 97 -11 | 21 97 3 | 17 97 9
    // 9.2 100 -9.5

    private static List<TaskFactory.InterestingLocation> NPCWALKPOS(){

        List<TaskFactory.InterestingLocation> list = new ArrayList<>();
        list.addAll(List.of(
                new TaskFactory.InterestingLocation(7.5,100,7.5),
                new TaskFactory.InterestingLocation(7.5,100,-7.5),
                new TaskFactory.InterestingLocation(-7.5,100,-7.5),
                new TaskFactory.InterestingLocation(-7.5,100,7.5),

                new TaskFactory.InterestingLocation(20, 97, -5.3),
                new TaskFactory.InterestingLocation(20, 97, 7),
                new TaskFactory.InterestingLocation(16 ,97 ,9),
                new TaskFactory.InterestingLocation( 8.5 ,100, 3),

                new TaskFactory.InterestingLocation( 15, 100 ,-15),
                new TaskFactory.InterestingLocation( -21 ,100 ,-24),
                new TaskFactory.InterestingLocation( -21,100,-13),

                new TaskFactory.InterestingLocation(-26, 100 ,-28.5),
                new TaskFactory.InterestingLocation(-23 ,100, -8)));

        list.addAll(TASKS);

        return list;
    }

    public final static List<TaskFactory.Task> TASKS = TASKS();
    public final static List<TaskFactory.Task> TASKS_COPY = List.copyOf(TASKS);
    public static List<TaskFactory.InterestingLocation> NPCWALKPOS = NPCWALKPOS();

    public static void registerEvents(){
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(Main.getInstance());
            Main.getInstance().loadChunk(player.getPosition());

            player.setRespawnPoint(new Pos(0, 100, 0));



        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event ->{
            final Player player = event.getPlayer();

            player.setTeam(Dingus.HIDERS_TEAM);

            Main.dingusMap.put(player.getUuid(),new DingusPlayer(player, Role.HIDER));

            Main.sidebar.addViewer(player);

            player.getInventory().addItemStack(ItemStack.builder(Material.CROSSBOW)
                            .meta(CrossbowMeta.class,meta ->{
                                meta.charged(true);
                            })
                    .build());

            // Sending npc packet


        });

        globalEventHandler.addListener(PlayerItemAnimationEvent.class,event ->{

            if(event.getItemAnimationType() == PlayerItemAnimationEvent.ItemAnimationType.CROSSBOW){
                event.setCancelled(true);
            }

        });

        globalEventHandler.addListener(PlayerUseItemEvent.class, event ->{

            if(event.getItemStack().material() == Material.CROSSBOW){
                DingusPlayer dingusPlayer = Main.dingusMap.get(event.getPlayer().getUuid());
                if(dingusPlayer.getTargetEntity() instanceof FakeNPC) {
                    dingusPlayer.getTargetEntity().remove();
                } else if (dingusPlayer.getTargetEntity() instanceof Player) {
                    ((Player) dingusPlayer.getTargetEntity()).kill();
                }
                event.setCancelled(true);

            }
        });

        // tasks
        globalEventHandler.addListener(PlayerStartSneakingEvent.class,event ->{

            final Player player = event.getPlayer();
            final DingusPlayer dingusPlayer = Main.dingusMap.get(player.getUuid());

            TaskFactory.Task task = TaskFactory.getClosestTask(player);

            if(task != null){
                dingusPlayer.setCurrentTask(task);
                dingusPlayer.setTask_state(dingusPlayer.getTask_state() + 1);
                player.sendMessage(String.valueOf(dingusPlayer.getTask_state()));
                dingusPlayer.setLastSneakTime(System.currentTimeMillis());

                if(dingusPlayer.getTask_state() > task.work_duration){
                    completeTask(task);
                }
            }

        });

        globalEventHandler.addListener(PlayerStopSneakingEvent.class,event ->{

            final Player player = event.getPlayer();
            final DingusPlayer dingusPlayer = Main.dingusMap.get(player.getUuid());

            TaskFactory.Task task = TaskFactory.getClosestTask(player);

            if(task != dingusPlayer.getCurrentTask() | task == null){
                dingusPlayer.setCurrentTask(null);
                dingusPlayer.setTask_state(0);

            }

        });

    }

    public static void initTasksHolograms(){
        for(TaskFactory.Task task : TASKS){

            Entity hologram = new Entity(EntityType.TEXT_DISPLAY);
            TextDisplayMeta textDisplayMeta  = (TextDisplayMeta) hologram.getEntityMeta();
            textDisplayMeta.setViewRange(0.05f);
            textDisplayMeta.setText(TaskNameUtility.get(task,0));
            textDisplayMeta.setHasNoGravity(true);

            textDisplayMeta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);

            hologram.setInstance(Main.getInstance(),task.pos.add(0,1,0));
            task.hologramEntity = hologram;
        }




    }


    public static void completeTask(TaskFactory.Task task){

    }


}
