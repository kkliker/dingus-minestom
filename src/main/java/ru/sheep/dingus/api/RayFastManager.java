package ru.sheep.dingus.api;

import com.extollit.tuple.Pair;
import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import dev.emortal.rayfast.vector.Vector3d;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;

public class RayFastManager {

    @SuppressWarnings("UnstableApiUsage")
    public static void init() {
        Area3d.CONVERTER.register(Entity.class, entity ->
                Area3dRectangularPrism.wrapper(
                        entity,
                        ignored -> entity.getBoundingBox().minX() + entity.getPosition().x(),
                        ignored -> entity.getBoundingBox().minY() + entity.getPosition().y(),
                        ignored -> entity.getBoundingBox().minZ() + entity.getPosition().z(),
                        ignored -> entity.getBoundingBox().maxX() + entity.getPosition().x(),
                        ignored -> entity.getBoundingBox().maxY() + entity.getPosition().y(),
                        ignored -> entity.getBoundingBox().maxZ() + entity.getPosition().z()
                )
        );
    }

    public static Pair<Entity, Vector3d> intersectWithFirstEntity(Entity entity, Instance instance,double range){

        Pos start = entity.getPosition().add(0,entity.getEyeHeight(),0);
        Vec direction = start.direction().mul(range);

        for (Entity en : instance.getNearbyEntities(start,range)){

            if(en == entity) continue;

            Area3d area3d = Area3d.CONVERTER.from(en);
            Vector3d intersection = area3d.lineIntersection(
                    start.x(),
                    start.y(),
                    start.z(),

                    direction.x(),
                    direction.y(),
                    direction.z()
            );

            if (intersection != null) {
                return new Pair<>(en,intersection);
            }

        }
        return new Pair<>(null,null);
    }

}
