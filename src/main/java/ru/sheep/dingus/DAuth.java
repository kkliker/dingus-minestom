package ru.sheep.dingus;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.DisplayScoreboardPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import ru.sheep.dingus.Dingus;
import ru.sheep.dingus.GlobalSidebar;
import ru.sheep.dingus.ServerConfig;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.domain.Role;

import java.util.List;
import java.util.Random;

public class DAuth {

    @Getter
    private final static List<Pos> spawnPos = List.of(
            new Pos(8.5,100,26.5,180,0),
            new Pos(23.5 ,102 ,-28.5,90,0),
            new Pos(13.5 ,97 ,0.5,13,0)
    );

    public static void init(){
            registerSpawn();
    }
    private static void registerSpawn(){

        EventNode<Event> eventNode = EventNode.all("auth");

        eventNode.addListener(AsyncPlayerConfigurationEvent.class, event ->{

            event.setSpawningInstance(InstanceHelper.getOverworld());
            Player player = event.getPlayer();

            DingusPlayer dingusPlayer = new DingusPlayer(player); // Registering player

            dingusPlayer.setRole(Role.DEAD);
            dingusPlayer.chache();

            player.setRespawnPoint(getRandomSpawnPoint());
        });

        eventNode.addListener(PlayerSpawnEvent.class,event ->{

            Player player = event.getPlayer();
            UnitedAPI.update(player,DingusPlayer.from(player.getUuid()));
        });

        MinecraftServer.getGlobalEventHandler().addChild(eventNode);
    };

    public static Pos getRandomSpawnPoint(){
        double amp = ServerConfig.amplifier();
        double rnd1 = new Random().nextDouble(-amp,amp);
        double rnd2 = new Random().nextDouble(-amp,amp);
        return spawnPos.get(new Random().nextInt(spawnPos.size())).add(rnd1,0,rnd2);
    }
}
