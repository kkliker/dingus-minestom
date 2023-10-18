package qship.task;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.network.packet.server.play.*;
import qship.Main;
import qship.TaskFactory;
import qship.session.DingusPlayer;
import qship.session.Role;
import qship.util.color;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static qship.session.Role.HIDER;

public class GameSessionTask implements Runnable {
    static int time = 300;

    @Override
    public void run() {

        // Time

        String min = String.valueOf(time / 60);
        String sec = String.valueOf(time % 60);

        if (sec.length() < 2) {
            sec = "0" + sec;
        }
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            p.sendActionBar(color.fromLegacy("&f&l" + min + ":" + sec));
        }

        time--;

        if(time < 0){
            endCurrentSession(HIDER);
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


    public void endCurrentSession(Role winner) {

        switch (winner){
            case HUNTER -> {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                    p.sendTitlePart(TitlePart.TITLE,color.fromLegacy("ПОБЕДА ОХОТИНИКОВ"));
                    p.sendTitlePart(TitlePart.SUBTITLE,color.fromLegacy("КВЕСТЫ ЗАВЕРШЕНЫ"));
                });
           // "ПОБЕДА ПРЯТОЧНИКОВ\NКВЕСТЫ ЗАВЕРШЕНЫ"
            }
            case HIDER -> {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                    p.sendTitlePart(TitlePart.TITLE,color.fromLegacy("ПОБЕДА ПРЯТОЧНИКОВ"));
                    p.sendTitlePart(TitlePart.SUBTITLE,color.fromLegacy("КВЕСТЫ ЗАВЕРШЕНЫ"));
                });
            }
        }
    }



    private EntityMetaDataPacket resetTextPacket(TaskFactory.Task task){
        return new EntityMetaDataPacket(task.hologramEntity.getEntityId(), Map.of(22, Metadata.Chat(color.fromLegacy("&a[Shift] " +  task.name))));
    }
}




