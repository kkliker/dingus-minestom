package qship.task;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import qship.Main;
import qship.fakeplayers.FakeNPC;

import java.util.List;

public class FakePlayersTask implements Runnable{

    @Override
    public void run() {

        List<Entity> fakeNPCList = Main.getInstance().getEntities().stream().filter(e -> e instanceof FakeNPC).toList();
        fakeNPCList.forEach(e ->{

            FakeNPC fakeNPC = (FakeNPC) e;
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                p.getPlayerConnection().sendPacket(fakeNPC.getPacket());
                p.getPlayerConnection().sendPacket(fakeNPC.getTeamPacket());
                System.out.println("fakeplayertask");
            });
        });

    }
}
