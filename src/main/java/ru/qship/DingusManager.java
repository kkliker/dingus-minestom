package ru.qship;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.utils.StringUtils;
import ru.qship.gametasks.TaskFactory;
import ru.qship.gametasks.TaskList;
import ru.qship.managers.TaskNameUtility;
import ru.qship.npc.FakeNPC;
import ru.qship.auth.DAuth;
import ru.qship.managers.InstanceHelper;
import ru.qship.managers.color;

import java.util.*;

public class DingusManager {


    @Getter
    private static List<TaskFactory.Task> currentTasks = new ArrayList<>(TaskList.TASKS());

    @Getter
    private static final Map<UUID,FakeNPC> NPCs = new HashMap<>();

    @Getter
    private static Sidebar sidebar = UPDATE_SIDEBAR(currentTasks);

    public static void reset(){
        NPCs.values().forEach(Entity::remove);
        NPCs.clear();
    }

    public static void spawnNPC(){
        MinecraftServer.getSchedulerManager().scheduleNextProcess(() ->{
            for(int x = 0; x <= Main.getCfg().getNPC(); x++){
                FakeNPC fakeNPC = new FakeNPC();
                fakeNPC.setInstance(InstanceHelper.getOverworld(), DAuth.getRandomSpawnPoint());
                NPCs.put(fakeNPC.getUuid(),fakeNPC);
            }
        }, ExecutionType.ASYNC);
    }

    public static void initTasksHolograms(){
        for(TaskFactory.Task task : DingusManager.getCurrentTasks()){

            Entity hologram = new Entity(EntityType.TEXT_DISPLAY);
            TextDisplayMeta textDisplayMeta  = (TextDisplayMeta) hologram.getEntityMeta();
            textDisplayMeta.setViewRange(0.04f);
            textDisplayMeta.setText(TaskNameUtility.get(task,0));
            textDisplayMeta.setHasNoGravity(true);

            textDisplayMeta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);

            hologram.setInstance(InstanceHelper.getOverworld(),task.pos.add(0,1,0));
            task.setHologramEntity(hologram);
        }
    }

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
    public static final Sidebar UPDATE_SIDEBAR(List<TaskFactory.Task> taskList){
        sidebar = new Sidebar(color.fromLegacy("&f&lЗадания"));
        for (int x = 0; x < taskList.size(); x++){
            TaskFactory.Task task = taskList.get(x);
            if(!task.isComplete()) {
                String name = task.getName();
                //name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                if (task.getTaskRisk() == TaskFactory.TaskRisk.Red) {
                    sidebar.createLine(new Sidebar.ScoreboardLine(String.valueOf(x + 1), color.fromLegacy(String.format("&c&l%s ▍ %s", task.getTaskLocation().name(), name)), x + 1));
                } else {
                    sidebar.createLine(new Sidebar.ScoreboardLine(String.valueOf(x + 1), color.fromLegacy(String.format("&f&l%s ▍ %s", task.getTaskLocation().name(), name)), x + 1));
                }
            }
        }
        return sidebar;
    }






}
