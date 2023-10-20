package ru.qship.managers;

import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;

public class DDymensionType {

    public static final DimensionType DINGUS = DimensionType.builder(NamespaceID.from("dingus:goobero"))
            .fixedTime(6000L)
            .ultrawarm(false)
            .natural(true)
            .piglinSafe(false)
            .respawnAnchorSafe(false)
            .bedSafe(true)
            .raidCapable(true)
            .skylightEnabled(true)
            .ceilingEnabled(false)
            .f
            .fixedTime(null)
            .ambientLight(0.0f)
            .logicalHeight(256)
            .infiniburn(NamespaceID.from("dingus:infiniburn_goobero"))
            .build();
}
