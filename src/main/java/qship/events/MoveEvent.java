package qship.events;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.player.PlayerMoveEvent;

public class MoveEvent {

    public static void register(){

        MinecraftServer.getGlobalEventHandler().addListener(PlayerMoveEvent.class,event ->{

            Entity player = event.getPlayer();
            Pos pos = player.getPosition();
            if(pos.y() < 50){
                player.teleport(pos.add(0,200,0));
            }


        });


    }


}
