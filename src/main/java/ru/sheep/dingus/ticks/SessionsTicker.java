package ru.sheep.dingus.ticks;

import lombok.Getter;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.domain.WinningType;
import ru.sheep.dingus.session.GameSession;

import static ru.sheep.dingus.api.ComponentUtil.fromLegacy;

public class SessionsTicker extends AbstractTicker {

    @Getter
    private final GameSession session;
    private final Instance instance;

    public SessionsTicker(GameSession session,Instance instance){
        this.session = session;
        this.instance = instance;
    }

    public void tick() {

        // Time remaining

        long time = session.duration() - session.timeInSeconds();

        int min = (int) (time / 60);
        int sec = (int) (time % 60);

        for (Player p : instance.getPlayers()) {
            p.sendActionBar(fromLegacy("&f&l" + min + ":" + (sec < 10 ? "0" + sec : sec )));
        }

        WinningType type = session.checkForEnd();
        if(type == WinningType.NONE) return;

        //    endCurrentSession(type);

        }

    private void endCurrentSession(WinningType type) {

        if(type == WinningType.HUNTERS_TIME){
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                p.sendTitlePart(TitlePart.TITLE,fromLegacy("ПОБЕДА ОХОТНИКОВ"));
                p.sendTitlePart(TitlePart.SUBTITLE,fromLegacy("ВРЕМЯ ВЫШЛО"));
            });
            return;
        }

        switch (type){
            case HUNTERS_GENOCIDE-> {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                    p.sendTitlePart(TitlePart.TITLE,fromLegacy("ПОБЕДА ОХОТИНИКОВ"));
                    p.sendTitlePart(TitlePart.SUBTITLE,fromLegacy("ГЕНОЦИД"));
                });
            }
            case HIDERS_QUESTS -> {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->{
                    p.sendTitlePart(TitlePart.TITLE, fromLegacy("ПОБЕДА ПРЯТОЧНИКОВ"));
                    p.sendTitlePart(TitlePart.SUBTITLE,fromLegacy("КВЕСТЫ ЗАВЕРШЕНЫ"));
                });
            }
        }
    }
}
