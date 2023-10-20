package ru.qship.managers;

import lombok.Getter;
import net.hollowcube.polar.PolarLoader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;

import java.io.IOException;
import java.nio.file.Path;

public class InstanceHelper {


    @Getter
    private static Instance overworld;

    public static void init(){
        try {
            overworld = MinecraftServer.getInstanceManager().createInstanceContainer(DDymensionType.DINGUS,new PolarLoader(Path.of("./dingus.polar")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
