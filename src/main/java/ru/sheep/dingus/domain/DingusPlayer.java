package ru.sheep.dingus.domain;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.thread.Acquirable;
import ru.sheep.dingus.quests.Quest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
public class DingusPlayer {

    private static final Map<UUID, DingusPlayer> dingusPlayerMap = new HashMap<>();
    private Acquirable<Entity> targetEntity;
    private Player player;
    private Role role = Role.HIDER;
    private AtomicInteger task_state;
    private Quest currentQuest;
    private long lastSneakTime = 0;


    public DingusPlayer(Player player) {

        this.player = player;
        this.task_state = new AtomicInteger(0);
        this.currentQuest = null;
    }
    public void chache(){
        dingusPlayerMap.put(player.getUuid(),this);
    }

    public static DingusPlayer from(UUID uuid){
        return dingusPlayerMap.get(uuid);
    }

    public static List<DingusPlayer> players(){
        return new ArrayList<>(dingusPlayerMap.values());
    }

    public void delete(){
        dingusPlayerMap.remove(player.getUuid());
    }

}