package ru.sheep.dingus.ticks;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Metadata;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import ru.sheep.dingus.UnitedAPI;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.api.ComponentUtil;
import ru.sheep.dingus.quests.PreconfiguredTasks;
import ru.sheep.dingus.quests.Quest;
import ru.sheep.dingus.quests.QuestRisk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AreaParticlesTicker extends AbstractTicker{

    private static final Instance instance = InstanceHelper.getOverworld();
    private static final double radius = 0.8;
    private final List<Quest> quests;

    public AreaParticlesTicker(List<Quest> quests){
        this.quests = new ArrayList<>(quests);
    }

    public void tick() {

        quests.forEach(task ->{

            int angle = 180;
            Pos pos = task.getPos();

            while(angle > 0) {

                double x = (radius * Math.sin(angle));
                double z = (radius * Math.cos(angle));
                angle -= 2;

                ParticlePacket particle;

                if(task.getQuestRisk() == QuestRisk.WHITE) {

                    particle = ParticleCreator.createParticlePacket(Particle.DUST, true, pos.x() + x, pos.y(), pos.z() + z, 0f, 0f, 0f, 0, 1, writer -> {
                        writer.writeFloat(1f);
                        writer.writeFloat(1f);
                        writer.writeFloat(1f);
                        writer.writeFloat(1f);
                    });
                }
                else{
                    particle = ParticleCreator.createParticlePacket(Particle.DUST, true, pos.x() + x, pos.y(), pos.z() + z, 0f, 0f, 0f, 0, 1, writer -> {
                        writer.writeFloat(1f);
                        writer.writeFloat(0f);
                        writer.writeFloat(0f);
                        writer.writeFloat(1f);
                    });
                }
                InstanceHelper.getOverworld().sendGroupedPacket(particle);
            }
        });



    }
}
