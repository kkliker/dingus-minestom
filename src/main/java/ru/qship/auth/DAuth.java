package ru.qship.auth;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import ru.qship.DingusManager;
import ru.qship.domain.DingusPlayer;
import ru.qship.Main;
import ru.qship.managers.InstanceHelper;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

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

        eventNode.addListener(AsyncPlayerPreLoginEvent.class,event ->{
        });

        eventNode.addListener(PlayerLoginEvent.class, event ->{

            event.setSpawningInstance(InstanceHelper.getOverworld());
            Player player = event.getPlayer();
            DingusPlayer dingusPlayer = new DingusPlayer(player); // Registering player
            player.setRespawnPoint(getRandomSpawnPoint());

        });

        eventNode.addListener(PlayerSpawnEvent.class,event ->{

            Player player = event.getPlayer();
            player.setTeam(DingusManager.HIDERS_TEAM);
            DingusManager.getSidebar().addViewer(player);
            DingusManager.getNPCs().values().forEach(npc ->{
                player.sendPacket(npc.getInfoCreatePacket());
                player.sendPacket(npc.getTeamPacket());
                npc.updateNewViewer(player);


            });
            player.getInventory().addItemStack(ItemStack.of(Material.CROSSBOW));

        });

        MinecraftServer.getGlobalEventHandler().addChild(eventNode);
    };

    public static Pos getRandomSpawnPoint(){
        double amp = Main.getCfg().getAmplifier();
        double rnd1 = new Random().nextDouble(-amp,amp);
        double rnd2 = new Random().nextDouble(-amp,amp);
        return spawnPos.get(new Random().nextInt(spawnPos.size())).add(rnd1,0,rnd2);
    }
}
