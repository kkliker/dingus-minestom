package qship.fakeplayers;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import qship.Dingus;
import qship.goals.CityNPCStrollGoal;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class FakeNPC extends EntityCreature {

    static int counter = 0;
    static Random rnd = new Random();

    public String name;

    public FakeNPC() {
        super(EntityType.PLAYER);

        addAIGroup(
                List.of(
                        new CityNPCStrollGoal(this) // Walk around
                ),
                List.of()
        );
        this.name = String.valueOf(counter);
        counter++;
        this.hasCollision = false;

    }

    public PlayerInfoUpdatePacket getPacket(){
        return new PlayerInfoUpdatePacket(
                PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                new PlayerInfoUpdatePacket.Entry(uuid, name, List.of(),false,1, GameMode.CREATIVE, Component.text(name), null)
        );
    }
    public PlayerInfoRemovePacket getInfoRemovePacket(){
        return new PlayerInfoRemovePacket(this.getUuid());
    }

    public TeamsPacket getTeamPacket() {
        return new TeamsPacket("NPC_TEAM", new TeamsPacket.AddEntitiesToTeamAction(List.of(this.name)));
    }

    public void playAnimation(Entity.Pose startAnimation,Entity.Pose endAnimation,int ticks){
        this.setPose(startAnimation);
        MinecraftServer.getSchedulerManager().scheduleTask(() ->{
            this.setPose(endAnimation);
        }, TaskSchedule.tick(ticks),TaskSchedule.stop(), ExecutionType.ASYNC);
    }

}
