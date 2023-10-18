package qship.task;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Metadata;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import qship.Dingus;
import qship.Main;
import qship.TaskFactory;
import qship.session.DingusPlayer;
import qship.util.TaskNameUtility;
import qship.util.color;

import java.util.Map;

public class TasksTask implements Runnable{

    static double radius = 0.8;

    @Override
    public void run() {


        Dingus.TASKS.forEach(task ->{

            int angle = 180;
            Pos pos = task.pos;

            while(angle > 0) {

                double x = (radius * Math.sin(angle));
                double z = (radius * Math.cos(angle));
                angle -= 2;

                ParticlePacket particle;

                if(task.taskRisk == TaskFactory.TaskRisk.WHITE) {

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

                Main.getInstance().sendGroupedPacket(particle);
            }

        });
        // tasks


        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{

            DingusPlayer dingusPlayer = Main.dingusMap.get(p.getUuid());

            if(dingusPlayer == null){
                return;
            }

            TaskFactory.Task task = TaskFactory.getClosestTask(p);
           // p.sendMessage(task + "  task");

            if(task == null) {
                dingusPlayer.setTask_state(0);
                if(dingusPlayer.getCurrentTask() != null) {
                    p.sendPacket(resetTextPacket(dingusPlayer.getCurrentTask()));
                }
                dingusPlayer.setCurrentTask(null);
                return;
            }

            if(System.currentTimeMillis() - dingusPlayer.getLastSneakTime() >= 1000 ){
                dingusPlayer.setTask_state(0);
            }

            EntityMetaDataPacket entityMetaPacket = new EntityMetaDataPacket(task.hologramEntity.getEntityId(), Map.of(22, Metadata.Chat(TaskNameUtility.get(task,dingusPlayer.getTask_state()))));

            p.sendPacket(entityMetaPacket);
            // p.sendMessage("sended packet");

            dingusPlayer.setCurrentTask(task);

        });

    }

    private EntityMetaDataPacket resetTextPacket(TaskFactory.Task task){
        return new EntityMetaDataPacket(task.hologramEntity.getEntityId(), Map.of(22, Metadata.Chat(TaskNameUtility.get(task,0))));
    }

}
