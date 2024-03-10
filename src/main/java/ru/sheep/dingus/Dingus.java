package ru.sheep.dingus;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.domain.Role;
import ru.sheep.dingus.npc.FakeNPC;
import ru.sheep.dingus.session.GameSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dingus {

    @Getter
    private static final GlobalSidebar globalSidebar = new GlobalSidebar();

    @Getter
    @Setter
    private static GameSession session;

    private static final List<FakeNPC> npcs = new ArrayList<>();

    public static void reset(){
        InstanceHelper.getOverworld().getEntities().forEach(e ->{
            if (e instanceof FakeNPC) e.remove();
        });
        npcs.clear();
        session = UnitedAPI.defaultGameSession();
        session.start();

        spawnNPC();
        spawnPlayers();
    }

    public static List<FakeNPC> getNPCs(){
        return npcs;
    }

    public static void spawnNPC(){
            for(int x = 0; x < ServerConfig.npc_number(); x++){
                FakeNPC fakeNPC = new FakeNPC();
                fakeNPC.setInstance(InstanceHelper.getOverworld(), DAuth.getRandomSpawnPoint());
                npcs.add(fakeNPC);
            }
    }

    public static void spawnPlayers(){
        InstanceHelper.getOverworld().getPlayers().forEach(p ->{
            DingusPlayer dp = DingusPlayer.from(p.getUuid());
            var rnd = new Random();
            if (rnd.nextDouble() > 0.5) dp.setRole(Role.HIDER);
            else dp.setRole(Role.HUNTER);

            p.teleport(DAuth.getRandomSpawnPoint());
            UnitedAPI.update(p,dp);
        });
    }


}
