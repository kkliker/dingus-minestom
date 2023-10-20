package ru.qship.task;

import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.vector.Vector3d;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import ru.qship.domain.DingusPlayer;
import ru.qship.managers.InstanceHelper;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class IntersectTask implements Runnable{

    @Override
    public void run() {

        Instance instance = InstanceHelper.getOverworld();

        for (Player player : instance.getPlayers()) {

            DingusPlayer dingusPlayer = DingusPlayer.from(player.getUuid());

            if(player.getItemInMainHand().material() != Material.CROSSBOW && player.getItemInOffHand().material() != Material.CROSSBOW){
                unglow(player, dingusPlayer.getTargetEntity());
                dingusPlayer.setTargetEntity(null);
                return;
            }

            CompletableFuture.runAsync(() ->{
            Entity entity = getLineOfSightEntity(player, instance);

            if (entity != null) {

                glow(player,entity);

                if(dingusPlayer.getTargetEntity() != entity){
                    unglow(player,dingusPlayer.getTargetEntity());
                }
                dingusPlayer.setTargetEntity(entity);

            } else {
                unglow(player, dingusPlayer.getTargetEntity());
                dingusPlayer.setTargetEntity(null);
            }
            });

                /*

                Collection<Entity> list = instance.getNearbyEntities(player.getPosition(), 10);
                list.forEach(entity -> {

                    if(entity == player){
                        return;
                    }

                    Pos pos = player.getPosition().add(0, player.getEyeHeight(), 0);
                    Vec direction = pos.direction().mul(5,1,5);

                    Area3d area3d = Area3d.CONVERTER.from(entity);
                    Vector3d intersection = area3d.lineIntersection(
                            pos.x(),
                            pos.y(),
                            pos.z(),

                            direction.x(),
                            direction.y(),
                            direction.z()
                    );

                    System.out.println("intersected");

                    DingusPlayer dingusPlayer = Main.dingusMap.get(player.getUuid());

                    if (intersection != null) {

                        glow(player,entity);

                        if(dingusPlayer.getTargetEntity() != entity){
                            unglow(player,dingusPlayer.getTargetEntity());
                        }
                        dingusPlayer.setTargetEntity(entity);

                    } else {
                       unglow(player, dingusPlayer.getTargetEntity());
                        dingusPlayer.setTargetEntity(null);
                    }
                });

                 */
        }
    }

    private Entity getLineOfSightEntity(Player player, Instance instance){
        Collection<Entity> list = instance.getNearbyEntities(player.getPosition(), 6);
        Entity target = null;
        Pos pos = player.getPosition().add(0, player.getEyeHeight(), 0);
        Vec direction = pos.direction().mul(7);
        for(Entity entity : list) {

            if (entity == player) {
                continue;
            }


            Area3d area3d = Area3d.CONVERTER.from(entity);
            Vector3d intersection = area3d.lineIntersection(
                    pos.x(),
                    pos.y(),
                    pos.z(),

                    direction.x(),
                    direction.y(),
                    direction.z()
            );

            if (intersection != null) {
                target = entity;
                break;
            }
        }
        return target;
    }

    private void glow(Player player,Entity target){
        EntityMetaDataPacket entityMetaPacket = new EntityMetaDataPacket(target.getEntityId(), Map.of(0, Metadata.Byte((byte) 0x40)));
        player.sendPacket(entityMetaPacket);
    }
    private void unglow(Player player, Entity target){

        if(target == null){
            return;
        }

        EntityMetaDataPacket entityMetaPacket = new EntityMetaDataPacket(target.getEntityId(), Map.of(0, Metadata.Byte((byte) 0)));
        player.sendPacket(entityMetaPacket);
    }

}
