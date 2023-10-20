package ru.qship;

import net.kyori.adventure.bossbar.BossBar;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.BossBarPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import ru.qship.domain.DingusPlayer;
import ru.qship.gametasks.TaskFactory;
import ru.qship.managers.InstanceHelper;
import ru.qship.managers.TaskNameUtility;
import ru.qship.managers.color;
import ru.qship.npc.FakeNPC;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerEvents {


    public static void registerEvents(){
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();



        globalEventHandler.addListener(PlayerItemAnimationEvent.class, event ->{

            if(event.getItemAnimationType() == PlayerItemAnimationEvent.ItemAnimationType.CROSSBOW){
                event.setCancelled(true);
            }
        });


        globalEventHandler.addListener(PlayerUseItemEvent.class, event ->{

            if(event.getItemStack().material() == Material.CROSSBOW){
                DingusPlayer dingusPlayer = DingusPlayer.from(event.getPlayer().getUuid());
                if(dingusPlayer.getTargetEntity() instanceof FakeNPC npc) {
                    npc.remove();
                } else if (dingusPlayer.getTargetEntity() instanceof Player p) {
                    p.kill();
                }
                event.setCancelled(true);
            }
        });

        // tasks
        globalEventHandler.addListener(PlayerStartSneakingEvent.class, event ->{

            final Player player = event.getPlayer();
            DingusPlayer dingusPlayer = DingusPlayer.from(event.getPlayer().getUuid());

            TaskFactory.Task task = TaskFactory.getClosestTask(player);

            if(task != null){
                dingusPlayer.setCurrentTask(task);
                dingusPlayer.setTask_state(dingusPlayer.getTask_state() + 1);

                EntityMetaDataPacket entityMetaPacket = new EntityMetaDataPacket(task.getHologramEntity().getEntityId(), Map.of(22, Metadata.Chat(TaskNameUtility.get(task,dingusPlayer.getTask_state()))));
                player.sendPacket(entityMetaPacket);

                dingusPlayer.setCurrentTask(task);

                if(dingusPlayer.getTask_state() >= task.getWork_duration()){
                    completeTask(task,dingusPlayer);
                }

            }

        });

        globalEventHandler.addListener(PlayerStopSneakingEvent.class,event ->{

            final Player player = event.getPlayer();
            DingusPlayer dingusPlayer = DingusPlayer.from(event.getPlayer().getUuid());;

            dingusPlayer.setLastSneakTime(System.currentTimeMillis());

            TaskFactory.Task task = TaskFactory.getClosestTask(player);

            /*if(task == null){
                dingusPlayer.setCurrentTask(null);
                dingusPlayer.setTask_state(0);
                return;
            }

            if(task != dingusPlayer.getCurrentTask()){
                dingusPlayer.setCurrentTask(null);
                dingusPlayer.setTask_state(1);
            }

             */
        });
    }


    public static void completeTask(TaskFactory.Task task,DingusPlayer player){



            player.setTask_state(0);
            Sidebar sidebar = DingusManager.getSidebar();
            sidebar.removeViewer(player.getPlayer());

            sendAnnosoumentBossBar(task,player);

            task.setComplete(true);
            Sidebar sd = DingusManager.UPDATE_SIDEBAR(DingusManager.getCurrentTasks());
            InstanceHelper.getOverworld().getPlayers().forEach(p ->{
                sd.addViewer(p);
            });
            EntityMetaDataPacket entityMetaPacket = new EntityMetaDataPacket(task.getHologramEntity().getEntityId(), Map.of(22, Metadata.Chat(TaskNameUtility.get(task,0))));
            player.getPlayer().sendPacket(entityMetaPacket);

            player.setCurrentTask(null);
    }

    private static void sendAnnosoumentBossBar(TaskFactory.Task task,DingusPlayer player){

        final int delay = new Random().nextInt(5,10);

        final AtomicInteger current = new AtomicInteger(0);
        final UUID bossbarUUID = UUID.randomUUID();
        final float maxvalue = 100f;
        final Instance instance = InstanceHelper.getOverworld();

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{


            BossBar bossBar = BossBar.bossBar(
                    color.fromLegacy(
                            String.format("&f&l%s COMPLETE NECCESARY %s TASK %s SECONDS AGO",player.getPlayer().getUsername(),task.getTaskRisk().toString().toUpperCase(),delay)
                    ),
                    (maxvalue -  current.get()) / maxvalue,
                    BossBar.Color.WHITE,
                    BossBar.Overlay.PROGRESS);
            BossBarPacket bossBarPacket = new BossBarPacket(bossbarUUID,new BossBarPacket.AddAction(bossBar));
            BossBarPacket deletePacket = new BossBarPacket(bossbarUUID,new BossBarPacket.RemoveAction());


            instance.sendGroupedPacket(bossBarPacket);
            MinecraftServer.getSchedulerManager().scheduleTask(() ->{
            MinecraftServer.getSchedulerManager().submitTask(() ->{

                int i = current.getAndIncrement();
                bossBar.progress((maxvalue - i) / maxvalue);
                player.getPlayer().sendPacket(new BossBarPacket(bossbarUUID,new BossBarPacket.UpdateHealthAction(bossBar)));

                if(current.get() > maxvalue){
                    player.getPlayer().sendPacket(deletePacket);
                    return TaskSchedule.stop();
                }
                return TaskSchedule.tick(1);
            }, ExecutionType.ASYNC);
        }, TaskSchedule.tick(1),TaskSchedule.stop());

        },TaskSchedule.seconds(delay),TaskSchedule.stop());


    }
}
