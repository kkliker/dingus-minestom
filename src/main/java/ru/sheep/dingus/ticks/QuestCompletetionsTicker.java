package ru.sheep.dingus.ticks;

import com.extollit.tuple.Pair;
import net.minestom.server.MinecraftServer;
import ru.sheep.dingus.UnitedAPI;
import ru.sheep.dingus.api.ComponentUtil;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.api.PacketsAPI;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.quests.Quest;

public class QuestCompletetionsTicker extends AbstractTicker{

    private static final long cooldown = 500;

    @Override
    public void tick() {

        InstanceHelper.getOverworld().getPlayers().forEach(e ->{

            Pair<Quest, Double> quest = UnitedAPI.getClosestTask(e);
            DingusPlayer dingusPlayer = DingusPlayer.from(e.getUuid());


            if(dingusPlayer.getCurrentQuest() != null && quest.left != dingusPlayer.getCurrentQuest()){
                dingusPlayer.getTask_state().set(0);
                Quest q = dingusPlayer.getCurrentQuest();
                dingusPlayer.getPlayer().sendPacket(PacketsAPI.textDisplaySetTextPacket(q.getHologramEntityId(), ComponentUtil.task_name(q,dingusPlayer.getTask_state().get())));
                dingusPlayer.setCurrentQuest(null);
                return;
            }

            if(!e.isSneaking()) return;

            Long cd = UnitedAPI.getCooldown().get(e.getUuid());

            if(cd == null) cd = 0L;

            if(System.currentTimeMillis() - cd < 500) return;


            if(quest.right < 1.5) {

                Quest q = quest.left;
                dingusPlayer.setCurrentQuest(q);
                dingusPlayer.getTask_state().incrementAndGet();

                e.sendPacket(PacketsAPI.textDisplaySetTextPacket(q.getHologramEntityId(), ComponentUtil.task_name(q,dingusPlayer.getTask_state().get())));

                if (dingusPlayer.getTask_state().get() >= q.getWork_duration()) {
                    UnitedAPI.completeTask(q, dingusPlayer);
                    dingusPlayer.getTask_state().set(0);
                }
                UnitedAPI.getCooldown().put(e.getUuid(),System.currentTimeMillis());
            }

        });

    }
}
