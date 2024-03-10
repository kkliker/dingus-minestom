package ru.sheep.dingus.api;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.BossBarPacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.quests.Quest;
import ru.sheep.dingus.quests.QuestRisk;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.sheep.dingus.api.ComponentUtil.fromLegacy;

public class MessageUtil {

    public static void sendDelayedAnnosoumentBossBar(Runnable thenRun, Component title,int delay){

        final AtomicInteger current = new AtomicInteger(0);
        final UUID bossbarUUID = UUID.randomUUID();
        final float maxvalue = 100f;
        final Instance instance = InstanceHelper.getOverworld();

        MinecraftServer.getSchedulerManager().scheduleTask(() ->{


            BossBar bossBar = BossBar.bossBar(
                    title,
                    (maxvalue -  current.get()) / maxvalue,
                    BossBar.Color.WHITE,
                    BossBar.Overlay.PROGRESS);
            BossBarPacket bossBarPacket = new BossBarPacket(bossbarUUID,new BossBarPacket.AddAction(bossBar));
            BossBarPacket deletePacket = new BossBarPacket(bossbarUUID,new BossBarPacket.RemoveAction());


            instance.sendGroupedPacket(bossBarPacket);

                MinecraftServer.getSchedulerManager().submitTask(() ->{

                    int i = current.getAndIncrement();
                    bossBar.progress((maxvalue - i) / maxvalue);
                    instance.sendGroupedPacket(new BossBarPacket(bossbarUUID,new BossBarPacket.UpdateHealthAction(bossBar)));

                    if(current.get() > maxvalue){
                        instance.sendGroupedPacket(deletePacket);
                        thenRun.run();
                        return TaskSchedule.stop();
                    }
                    return TaskSchedule.tick(1);
                }, ExecutionType.ASYNC);

        },TaskSchedule.seconds(delay),TaskSchedule.stop());
    }

    public static void sendStaticKillAnnounsementBossBar(Player killer, Player killed){

        final int delay = 0;

        sendDelayedAnnosoumentBossBar(
                () -> {},
                fromLegacy(String.format("&6&lHider %s has been eliminated by hunter %s",killed.getUsername(),killer.getUsername())),
                delay);
    }

    public static void sendStaticMissKillAnnounsementBossBar(Player killer){

        final int delay = 0;

        sendDelayedAnnosoumentBossBar(
                () -> {},
                fromLegacy(String.format("&d&lHunter %s shot a civilian and is blinded for 12 seconds",killer.getUsername())),
                delay);
    }

    public static void sendStaticAnnosoumentBossBar(Quest quest, DingusPlayer player){

        final int delay = new Random().nextInt(5,10);

        boolean nessecary = true;
        if(quest.isComplete()) nessecary = false;
        if(!quest.isRequired()) nessecary = false;

        String nessecaryOrNot = nessecary ? " " : "non-essential ";

        System.out.println(quest.isComplete() + " " + quest.isRequired());

        var color = quest.getQuestRisk() == QuestRisk.RED ?  "&c&l" : "&f&l";

        sendDelayedAnnosoumentBossBar(
                () -> {},
                fromLegacy(
                        String.format("%s%s completed a %s%s task %s seconds ago",
                                color,
                                player.getPlayer().getUsername(),nessecaryOrNot, quest.getQuestRisk().toString().toUpperCase(),
                                delay)),
                delay);
    }

}
