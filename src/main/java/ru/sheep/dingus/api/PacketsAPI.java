package ru.sheep.dingus.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import ru.sheep.dingus.quests.Quest;

import java.util.List;
import java.util.Map;

import static ru.sheep.dingus.api.ComponentUtil.fromLegacy;

public class PacketsAPI {


    public static TeamsPacket createTeamPacket(String affectedUsername, String teamName, boolean nameTagVisible, NamedTextColor color){

        TeamsPacket.NameTagVisibility visibility = nameTagVisible ? TeamsPacket.NameTagVisibility.ALWAYS : TeamsPacket.NameTagVisibility.NEVER;
        TeamsPacket createTeamPacket = new TeamsPacket(
                teamName,
                new TeamsPacket.CreateTeamAction(
                Component.text(""),
                        (byte) 1,
                visibility,
                TeamsPacket.CollisionRule.NEVER,
                        color,
                        Component.text(""),
                        Component.text(""),
                        List.of(affectedUsername)
        ));
        return createTeamPacket;
    }
    public static EntityMetaDataPacket glowPacket(Player player, Entity target){
        return new EntityMetaDataPacket(target.getEntityId(), Map.of(0, Metadata.Byte((byte) 0x40)));
    }
    public static EntityMetaDataPacket unGlowPacket(Player player, Entity target){
        return new EntityMetaDataPacket(target.getEntityId(), Map.of(0, Metadata.Byte((byte) 0)));
    }

    public static EntityMetaDataPacket textDisplaySetTextPacket(int entityId,Component text){
        return new EntityMetaDataPacket(entityId, Map.of(23, Metadata.Chat(text)));
    }

    public static EntityMetaDataPacket resetTextPacket(Quest quest){ // for TextDisplayEntity
        return new EntityMetaDataPacket(quest.getHologramEntityId(), Map.of(22, Metadata.Chat(fromLegacy("&a[Shift] " +  quest.getName()))));
    }
}
