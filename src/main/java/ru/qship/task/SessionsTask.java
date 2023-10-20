package ru.qship.task;

import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;

import ru.qship.domain.DingusPlayer;
import ru.qship.domain.Role;
import ru.qship.gametasks.TaskFactory;
import ru.qship.managers.InstanceHelper;
import ru.qship.managers.TaskNameUtility;
import ru.qship.managers.color;


import java.util.Map;

public class SessionsTask implements Runnable{

    private static int time = 300;
    private static final Instance instance = InstanceHelper.getOverworld();

    @Override
    public void run() {

        // Time


        String min = String.valueOf(time / 60);
        String sec = String.valueOf(time % 60);

        if (sec.length() < 2) {
            sec = "0" + sec;
        }
        for (Player p : instance.getPlayers()) {
            p.sendActionBar(color.fromLegacy("&f&l" + min + ":" + sec));
        }

        time--;

        if(time < 0){
            endCurrentSession(Role.HUNTER,true);
            time = 300;
        }

        // task hologram



        // Glowing

        /*

        for(Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            DingusPlayer dingusPlayer = Main.dingusMap.get(p.getUuid());
            Role playerRole = dingusPlayer.getRole();
            for (Player target : MinecraftServer.getConnectionManager().getOnlinePlayers()) {

                if (target == p) {
                    return;
                }

                DingusPlayer dingusTarget = Main.dingusMap.get(target.getUuid());

                //В начало создании сессии/прив ходе игрока обновить тиму
                if (targetRole == HIDER & playerRole == HIDER) {

                    new TeamsPacket(p.getTeam().getTeamName(), new TeamsPacket.UpdateTeamAction(
                            color.fromLegacy(p.getTeam().getTeamName()),
                            (byte) 1,
                            TeamsPacket.NameTagVisibility.HIDE_FOR_OTHER_TEAMS,
                            TeamsPacket.CollisionRule.NEVER,
                            NamedTextColor.WHITE,
                            color.fromLegacy(""),
                            color.fromLegacy("")
                    ));


                }
            }
        }

         */

    }


    public void endCurrentSession(Role winner,boolean timeout) {

        if(timeout){
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                p.sendTitlePart(TitlePart.TITLE,color.fromLegacy("ПОБЕДА ОХОТНИКОВ"));
                p.sendTitlePart(TitlePart.SUBTITLE,color.fromLegacy("ВРЕМЯ ВЫШЛО"));
            });
        }

        switch (winner){
            case HUNTER -> {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                    p.sendTitlePart(TitlePart.TITLE,color.fromLegacy("ПОБЕДА ОХОТИНИКОВ"));
                    p.sendTitlePart(TitlePart.SUBTITLE,color.fromLegacy("ГЕНОЦИД"));
                });
                // "ПОБЕДА ПРЯТОЧНИКОВ\NКВЕСТЫ ЗАВЕРШЕНЫ"
            }
            case HIDER -> {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                    p.sendTitlePart(TitlePart.TITLE, color.fromLegacy("ПОБЕДА ПРЯТОЧНИКОВ"));
                    p.sendTitlePart(TitlePart.SUBTITLE,color.fromLegacy("КВЕСТЫ ЗАВЕРШЕНЫ"));
                });
            }
        }
    }



    private EntityMetaDataPacket resetTextPacket(TaskFactory.Task task){
        return new EntityMetaDataPacket(task.getHologramEntity().getEntityId(), Map.of(22, Metadata.Chat(color.fromLegacy("&a[Shift] " +  task.getName()))));
    }
}
