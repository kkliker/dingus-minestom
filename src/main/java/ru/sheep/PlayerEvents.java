package ru.sheep;

import com.extollit.tuple.Pair;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import ru.sheep.dingus.Dingus;
import ru.sheep.dingus.UnitedAPI;
import ru.sheep.dingus.api.MessageUtil;
import ru.sheep.dingus.api.PacketsAPI;
import ru.sheep.dingus.api.RayFastManager;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.api.ComponentUtil;
import ru.sheep.dingus.npc.FakeNPC;
import ru.sheep.dingus.quests.Quest;

import java.util.Map;

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
                Entity e = RayFastManager.intersectWithFirstEntity(event.getPlayer(),event.getInstance(),10).left;
                if (e != null){
                    UnitedAPI.kill(event.getPlayer(),e);
                }
                event.setCancelled(true);
            }
        });

    }

}
