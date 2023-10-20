package ru.qship.domain;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import ru.qship.DingusManager;
import ru.qship.gametasks.TaskFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DingusPlayer {

    private static final Map<UUID, DingusPlayer> dingusPlayerMap = new HashMap<>();

    @Getter
    @Setter
    private Entity targetEntity;

    @Getter
    @Setter
    private Player player;

    @Getter
    @Setter
    private Role role = Role.HIDER;

    @Getter
    @Setter
    private int task_state;

    @Getter
    @Setter
    private TaskFactory.Task currentTask;

    @Getter
    @Setter
    private long lastSneakTime = 0;


    public DingusPlayer(Player player) {

        this.player = player;
        this.task_state = 0;
        this.currentTask = null;

        dingusPlayerMap.put(player.getUuid(),this);

    }

    public static DingusPlayer from(UUID uuid){
        return dingusPlayerMap.get(uuid);
    }

    public void delete(){
        dingusPlayerMap.remove(player.getUuid());
    }

}