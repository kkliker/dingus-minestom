package ru.sheep.dingus.npc;

import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dLike;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import ru.sheep.dingus.api.PacketsAPI;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeNPC extends EntityCreature {

    private static final AtomicInteger counter = new AtomicInteger(0);
    public final String name;

    public FakeNPC() {
        super(EntityType.PLAYER);
        setAutoViewable(true);
        addAIGroup(
                List.of(
                        new FakeNpcStrollGoal(this) // Walk around
                ),
                List.of()
        );
        this.name = String.valueOf(counter.getAndIncrement());
        this.hasCollision = false;
        this.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.20f);

    }

    public PlayerInfoUpdatePacket getInfoCreatePacket() {
        Component customName = getCustomName();
        Component displayName = customName == null ? Component.text(name) : customName;

        PlayerSkin skin = new PlayerSkin("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2RjYWJiNTEwMDQxYTQ1ZGIwZjMwOTBiOTlmZTY1YmQ0N2E2NDEwZTdhMGNjMmUwZTA2ZmYwZjE0M2YwZTMxOSJ9fX0=", "");

        PlayerInfoUpdatePacket packet = new PlayerInfoUpdatePacket(
                PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                new PlayerInfoUpdatePacket.Entry(uuid, name, List.of(new PlayerInfoUpdatePacket.Property("textures", skin.textures(), skin.signature())), false, 1, GameMode.CREATIVE, displayName, null)
        );
        setCustomName(displayName);
        return packet;
    }

    public PlayerInfoRemovePacket getInfoRemovePacket() {
        return new PlayerInfoRemovePacket(this.getUuid());
    }

    public TeamsPacket getTeamPacket() {
        return PacketsAPI.createTeamPacket(name, "NPC_TEAM", false, NamedTextColor.BLUE);
    }

    public void playAnimation(Entity.Pose startAnimation, Entity.Pose endAnimation, int ticks) {
        this.setPose(startAnimation);
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            this.setPose(endAnimation);
        }, TaskSchedule.tick(ticks), TaskSchedule.stop(), ExecutionType.ASYNC);
    }
}

