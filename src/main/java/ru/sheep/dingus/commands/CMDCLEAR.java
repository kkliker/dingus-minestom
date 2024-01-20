package ru.sheep.dingus.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.sheep.dingus.Dingus;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.api.PacketsAPI;

public class CMDCLEAR extends Command {
    public CMDCLEAR() {
        super("clear");

        addSyntax((commandSender, commandContext) -> {
            Player p = (Player) commandSender;

            InstanceHelper.getOverworld().getEntities().forEach(e ->{
                p.sendPacket(PacketsAPI.unGlowPacket(p,e));
            });
            Dingus.reset();
        });

    }
}
