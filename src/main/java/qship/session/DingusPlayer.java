package qship.session;

import com.extollit.gaming.ai.path.HydrazinePathFinder;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import qship.Dingus;
import qship.TaskFactory;
import qship.util.color;

public class DingusPlayer {

    @Getter
    @Setter
    Entity targetEntity;
    @Getter
    @Setter
    Player player;
    @Getter
    @Setter
    Role role;

    @Getter
    @Setter
    int task_state;

    @Getter
    @Setter
    TaskFactory.Task currentTask;

    @Getter
    @Setter
    long lastSneakTime;

    public DingusPlayer(Player player,Role role){
        this.player = player;
        this.role = role;
        this.task_state = 0;
        this.currentTask = null;
        this.lastSneakTime = 0;

    }


}
