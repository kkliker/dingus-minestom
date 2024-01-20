package ru.sheep.dingus.api;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class NoTickEntity extends Entity {
    public NoTickEntity(@NotNull EntityType entityType) {
        super(entityType);
    }

    @Override
    public void tick(long time) {

    }
}
