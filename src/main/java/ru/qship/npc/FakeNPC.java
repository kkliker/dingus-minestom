package ru.qship.npc;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.*;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FakeNPC extends EntityCreature {

    private static int counter = 0;

    public String name;

    public FakeNPC() {
        super(EntityType.PLAYER);
        setAutoViewable(true);
        addAIGroup(
                List.of(
                        new FakeNpcStrollGoal(this) // Walk around
                ),
                List.of()
        );
        this.name = String.valueOf(counter);
        counter++;
        this.hasCollision = false;

    }

    public PlayerInfoUpdatePacket getInfoCreatePacket(){
        Component customName = getCustomName();
        Component displayName = customName == null ? Component.text(name) : customName;

        PlayerSkin skin = PlayerSkin.fromUsername("Notch");

        PlayerInfoUpdatePacket packet = new PlayerInfoUpdatePacket(
                PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                new PlayerInfoUpdatePacket.Entry(uuid, name, List.of(new PlayerInfoUpdatePacket.Property("textures",skin.textures(),skin.signature())),false,1,GameMode.CREATIVE,displayName, null)
        );
        setCustomName(displayName);
        return packet;
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
