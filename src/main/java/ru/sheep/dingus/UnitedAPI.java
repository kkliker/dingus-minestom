package ru.sheep.dingus;

import com.extollit.tuple.Pair;
import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.CrossbowMeta;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;
import ru.sheep.dingus.api.MessageUtil;
import ru.sheep.dingus.api.ComponentUtil;
import ru.sheep.dingus.api.InstanceHelper;
import ru.sheep.dingus.api.PacketsAPI;
import ru.sheep.dingus.domain.DingusPlayer;
import ru.sheep.dingus.domain.Role;
import ru.sheep.dingus.npc.FakeNPC;
import ru.sheep.dingus.quests.PreconfiguredTasks;
import ru.sheep.dingus.quests.Quest;
import ru.sheep.dingus.quests.QuestContainer;
import ru.sheep.dingus.quests.QuestRisk;
import ru.sheep.dingus.session.GameSession;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UnitedAPI {

    @Getter
    private static final Map<UUID, Long> cooldown = new HashMap<>(); // для кулдауна на тик задания

    public static void completeTask(Quest quest, DingusPlayer player) {

        player.getTask_state().set(0);
        MessageUtil.sendStaticAnnosoumentBossBar(quest, player);
        quest.setComplete(true);

        Dingus.getGlobalSidebar().update(Dingus.getSession().questContainer().values());

    }

    public static void kill(Player killer, Entity entity) {

        killer.getInstance().playSound(Sound.sound(SoundEvent.ENTITY_FIREWORK_ROCKET_BLAST, Sound.Source.PLAYER, 1f, 1f));

        if (entity instanceof Player player) {
            DingusPlayer dp = DingusPlayer.from(player.getUuid());
            dp.setRole(Role.HIDER);
            MessageUtil.sendStaticKillAnnounsementBossBar(killer, player);
            return;
        }
        entity.remove();
        MessageUtil.sendStaticMissKillAnnounsementBossBar(killer);
    }


    public static void stun() {


    }

    public static void update(Player player, DingusPlayer dp) {

        // clearing
        player.getInventory().clear();
        player.getInventory().update();
        player.setInvisible(false);

        // adding weapons
        if (dp.getRole() == Role.HUNTER) player.getInventory()
                .addItemStack(ItemStack.of(Material.CROSSBOW)
                        .withMeta(CrossbowMeta.class, meta -> {
                            meta.projectile(ItemStack.of(Material.ARROW));
                        }));

        // check for dead
        if (dp.getRole() == Role.DEAD) player.setInvisible(true);

        // showing npc
        Dingus.getGlobalSidebar().show(List.of(player));
        Dingus.getNPCs().forEach(npc -> {
            player.sendPacket(npc.getInfoCreatePacket());
            player.sendPacket(UnitedAPI.getTeamsPacket(player, npc));
            npc.updateNewViewer(player);
        });

        // updating teams for glow colors
        InstanceHelper.getOverworld().getEntities().forEach(e -> {
            if (e instanceof LivingEntity livingEntity) {

                InstanceHelper.getOverworld().getPlayers().forEach(p -> {

                    EntityMetaDataPacket metaPacket = UnitedAPI.getGlowPacket(p, livingEntity);
                    if (metaPacket == null) return;

                    p.sendPacket(metaPacket);
                });
            }
        });
    }

    public static GameSession defaultGameSession() {
        return new GameSession(
                new QuestContainer(PreconfiguredTasks.tasks_towny()),
                System.currentTimeMillis(),
                0,
                300,
                0,
                0
        );
    }

    public static void autoTeams() {


        Set<Player> players = InstanceHelper.getOverworld().getPlayers();

        int size = players.size();
        AtomicInteger hunters = new AtomicInteger((int) (size * 0.4));

        players.forEach(p -> {

            if(hunters.get() > 0) {
                int item = new Random().nextInt(size);
                int i = 0;
                for (Player obj : players) {
                    if (i == item) {
                        hunters.decrementAndGet();
                    }
                    ;
                    i++;
                }
            }



        });



    }
    public static void initiateDingus(){
       Dingus.setSession(defaultGameSession());
       Dingus.getSession().start();
    }
    public static boolean isGenocide(List<DingusPlayer> players){

        var arr = new ArrayList<>(players);
        for (DingusPlayer dingusPlayer : arr){
            if (dingusPlayer.getRole() == Role.HIDER) return false;
        }
        return true;
    }

    public static boolean isQuestsCompleted(List<Quest> questList){

        var arr = new ArrayList<>(questList);
        for (Quest q: arr){
            if(!q.isComplete()) return false;
        }
        return true;

    }

    public static Pair<Quest, Double> getClosestTask(Entity entity) {

        List<Quest> quest = Dingus.getSession().questContainer().values();

        Quest first = quest.get(0);

        Pos epos = entity.getPosition();
        Pos fpos = first.getPos();

        double distance = distsquare_coord(
                epos.x(), epos.y(), epos.z(),
                fpos.x(), fpos.y(), fpos.z()
        );

        for (Quest location : quest) {

            Pos qpos = location.getPos();
            double distance2 = distsquare_coord(
                    epos.x(), epos.y(), epos.z(),
                    qpos.x(), qpos.y(), qpos.z()
            );

            if (distance2 < distance) {
                first = location;
                distance = distance2;
            }
        }

        Pos fpos2 = first.getPos();
        return new Pair<>(first,distsquare_coord(
                epos.x(), epos.y(), epos.z(),
                fpos2.x(), fpos2.y(), fpos2.z()
        ));
    }

    public static Pair<Quest, Double> getSafeClosestTaskButWithoutThese(Entity entity, List<Pos> quests) {

        List<Quest> quest2 = new ArrayList<>(Dingus.getSession().questContainer().values());
        List<Quest> quest = new ArrayList<>();

        for (Quest q : quest2){
            if(q.getQuestRisk() == QuestRisk.RED) continue;
            if(!quests.contains(q.getPos())) quest.add(q);
        }

        Quest first = quest.get(0);

        Pos epos = entity.getPosition();
        Pos fpos = first.getPos();

        double distance = distsquare_coord(
                epos.x(), epos.y(), epos.z(),
                fpos.x(), fpos.y(), fpos.z()
        );

        for (Quest location : quest) {

            Pos qpos = location.getPos();
            double distance2 = distsquare_coord(
                    epos.x(), epos.y(), epos.z(),
                    qpos.x(), qpos.y(), qpos.z()
            );

            if (distance2 < distance) {
                first = location;
                distance = distance2;
            }
        }

        return new Pair<>(first, distance);
    }

    @Nullable
    public static EntityMetaDataPacket getGlowPacket(Player player, LivingEntity t){

        if(t instanceof FakeNPC npc){
            return PacketsAPI.unGlowPacket(player,t);
        } else if (t instanceof Player target) {

            DingusPlayer dingusPlayer = DingusPlayer.from(player.getUuid());
            DingusPlayer dingusTarget = DingusPlayer.from(target.getUuid());

            if(dingusTarget == null || dingusPlayer == null) return null;

            Role playerRole = dingusPlayer.getRole();
            Role targetRole = dingusTarget.getRole();

            if((playerRole == Role.HUNTER) && targetRole == Role.HUNTER){
                return PacketsAPI.glowPacket(player,t);

            } else if (playerRole == Role.HIDER && targetRole == Role.HUNTER) {
                return PacketsAPI.glowPacket(player,t);

            } else if (playerRole == Role.HIDER && targetRole == Role.HIDER) {
                return PacketsAPI.glowPacket(player,t);
            }
            else{ // hunter - hider
                return PacketsAPI.unGlowPacket(player,t);
            }

        }

        return null;
    }

    public static TeamsPacket getTeamsPacket(Player player, LivingEntity t){

        if(t instanceof FakeNPC npc){
            return PacketsAPI.createTeamPacket(npc.name,"NPC_TEAM",false, NamedTextColor.RED);
        } else if (player == t) {

        } else if (t instanceof Player target) {

            DingusPlayer dingusPlayer = DingusPlayer.from(player.getUuid());
            DingusPlayer dingusTarget = DingusPlayer.from(target.getUuid());

            if(dingusTarget == null || dingusPlayer == null) PacketsAPI.createTeamPacket(target.getUsername(),"NPC_TEAM",false, NamedTextColor.WHITE);

            Role playerRole = dingusPlayer.getRole();
            Role targetRole = dingusTarget.getRole();

            if((playerRole == Role.HUNTER) && targetRole == Role.HUNTER){
                return PacketsAPI.createTeamPacket(target.getUsername(),"HUNTERS_TEAM",true, NamedTextColor.BLACK);

            } else if (playerRole == Role.HIDER && targetRole == Role.HUNTER) {
                return PacketsAPI.createTeamPacket(target.getUsername(),"HUNTERS_TEAM",false, NamedTextColor.BLACK);

            } else if (playerRole == Role.HIDER && targetRole == Role.HIDER) {
                return PacketsAPI.createTeamPacket(target.getUsername(),"HUNTERS_TEAM",true, NamedTextColor.WHITE);
            } else if (playerRole == Role.DEAD && targetRole == Role.HIDER) {
                return PacketsAPI.createTeamPacket(target.getUsername(),"HIDERS_TEAM",true, NamedTextColor.WHITE);
            } else if (playerRole == Role.DEAD && targetRole == Role.HUNTER) {
                return PacketsAPI.createTeamPacket(target.getUsername(),"HUNTERS_TEAM",true, NamedTextColor.BLACK);
            }
            else{ // hunter - hider
                return PacketsAPI.createTeamPacket(target.getUsername(),"HUNTERS_TEAM",false, NamedTextColor.RED);
            }

        }

        return null;
    }
    private static double distsquare_coord(double xa, double ya, double za,
                                           double xb, double yb, double zb)
    {
        double dx = xa-xb; double dy=ya-yb; double dz=za-zb;
        return dx*dx + dy*dy + dz*dz;
    }

    public static void aim(DingusPlayer dingusPlayer){
        ItemStack it1 = dingusPlayer.getPlayer().getItemInMainHand();
        ItemStack it2 = dingusPlayer.getPlayer().getItemInOffHand();

        var it11 = addProjective(it1);
        var it22 = addProjective(it2);

        if(it11 != null) dingusPlayer.getPlayer().setItemInMainHand(it11);
        if(it22 != null) dingusPlayer.getPlayer().setItemInOffHand(it22);
        dingusPlayer.getPlayer().getInventory().update();
    }
    public static void noAim(DingusPlayer dingusPlayer){
        ItemStack it1 = dingusPlayer.getPlayer().getItemInMainHand();
        ItemStack it2 = dingusPlayer.getPlayer().getItemInOffHand();
        var it11 = removeProjective(it1);
        var it22 = removeProjective(it2);

        if(it11 != null) dingusPlayer.getPlayer().setItemInMainHand(it11);
        if(it22 != null) dingusPlayer.getPlayer().setItemInOffHand(it22);

        dingusPlayer.getPlayer().getInventory().update();
    }
    public static ItemStack addProjective(ItemStack itemStack){
        if(itemStack.material() != Material.CROSSBOW) return null;

        return itemStack.withMeta(CrossbowMeta.class,meta ->{
               meta.charged(true);
           });

    }
    public static ItemStack removeProjective(ItemStack itemStack){
        if(itemStack.material() != Material.CROSSBOW) return null;

        return itemStack.withMeta(CrossbowMeta.class,meta ->{
            meta.charged(false);
        });
    }


    public static void initTasksHolograms(){
        for(Quest quest : Dingus.getSession().questContainer().values()){

            Entity hologram = new LivingEntity(EntityType.TEXT_DISPLAY);
            TextDisplayMeta textDisplayMeta  = (TextDisplayMeta) hologram.getEntityMeta();
            textDisplayMeta.setViewRange(0.04f);
            textDisplayMeta.setText(ComponentUtil.task_name(quest,0));
            textDisplayMeta.setHasNoGravity(true);

            textDisplayMeta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);

            hologram.setInstance(InstanceHelper.getOverworld(), quest.getPos().add(0,1,0));
            quest.setHologramEntityId(hologram.getEntityId());
        }
    }

}
