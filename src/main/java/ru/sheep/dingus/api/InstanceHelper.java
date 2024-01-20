package ru.sheep.dingus.api;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;

import java.nio.file.Path;

public class InstanceHelper {


    @Getter
    private static Instance overworld;

    public static void init(){
            overworld = MinecraftServer.getInstanceManager().createInstanceContainer(DDymensionType.DINGUS,new AnvilLoader(Path.of("./run/worlds/towny")));

    }
}
