package ru.sheep.dingus.ticks;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import ru.sheep.dingus.api.PacketsAPI;
import ru.sheep.dingus.api.RayFastManager;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.api.InstanceHelper;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class IntersectTicker extends AbstractTicker{

    public void tick() {

        Instance instance = InstanceHelper.getOverworld();

        for (Player player : instance.getPlayers()) {

            DingusPlayer dingusPlayer = DingusPlayer.from(player.getUuid());

            if (dingusPlayer == null) return;

            CompletableFuture.runAsync(() ->{

                Entity entity = RayFastManager.intersectWithFirstEntity(player, instance,10).left;
                Entity lastTarget = dingusPlayer.getTargetEntity();

                if (entity == null){
                    if(dingusPlayer.getTargetEntity() != null){
                        player.sendPacket(PacketsAPI.unGlowPacket(player,lastTarget));
                    }
                    dingusPlayer.setTargetEntity(null);
                    return;
                }

                if(lastTarget == null){
                    player.sendPacket(PacketsAPI.glowPacket(player,entity));
                    dingusPlayer.setTargetEntity(entity);
                    return;
                }

                if(lastTarget.getEntityId() == entity.getEntityId()){
                    return;
                }

                if(entity.getEntityId() != lastTarget.getEntityId()){
                    player.sendPacket(PacketsAPI.unGlowPacket(player,lastTarget));
                    return;
                }

                player.sendPacket(PacketsAPI.glowPacket(player,entity));
                dingusPlayer.setTargetEntity(entity);

            });

        }
    }


}
