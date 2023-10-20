package ru.qship.task;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Metadata;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import ru.qship.domain.DingusPlayer;
import ru.qship.gametasks.TaskList;
import ru.qship.gametasks.TaskFactory;
import ru.qship.managers.InstanceHelper;
import ru.qship.managers.TaskNameUtility;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class GameTasksTask implements Runnable{

    private static final Instance instance = InstanceHelper.getOverworld();
    private static final double radius = 0.8;
    @Override
    public void run() {

        TaskList.TASKS().forEach(task ->{

            int angle = 180;
            Pos pos = task.pos;

            while(angle > 0) {

                double x = (radius * Math.sin(angle));
                double z = (radius * Math.cos(angle));
                angle -= 2;

                ParticlePacket particle;

                if(task.getTaskRisk() == TaskFactory.TaskRisk.White) {

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

        instance.getPlayers().forEach(p ->{

            DingusPlayer dingusPlayer = DingusPlayer.from(p.getUuid());

            TaskFactory.Task task = TaskFactory.getClosestTask(p);
            if(task == null) {
                dingusPlayer.setTask_state(0);
                if(dingusPlayer.getCurrentTask() != null) {
                    EntityMetaDataPacket entityMetaPacket = new EntityMetaDataPacket(dingusPlayer.getCurrentTask().getHologramEntity().getEntityId(), Map.of(22, Metadata.Chat(TaskNameUtility.get(dingusPlayer.getCurrentTask(),0))));
                    p.sendPacket(entityMetaPacket);
                }
                dingusPlayer.setCurrentTask(null);
                return;
            }

            if(System.currentTimeMillis() - dingusPlayer.getLastSneakTime() >= 1250 ){
                dingusPlayer.setTask_state(0);
                TaskFactory.Task currentTask = dingusPlayer.getCurrentTask();
                if(currentTask != null) {
                    EntityMetaDataPacket entityMetaPacket = new EntityMetaDataPacket(currentTask.getHologramEntity().getEntityId(), Map.of(22, Metadata.Chat(TaskNameUtility.get(currentTask, 0))));
                    p.sendPacket(entityMetaPacket);
                }
            }
        });

    }
}
