package ru.sheep.dingus.npc;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import org.jetbrains.annotations.NotNull;
import ru.sheep.dingus.UnitedAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class FakeNpcStrollGoal extends GoalSelector {

    private static final long DELAY = 1000;
    private final static int posAmplifier = 1;
    private final Random random = new Random();
    private long lastStroll;
    private final Vector<Pos> lastQuest = new Vector<>(3){{
        add(new Pos(0,0,0));
        add(new Pos(0,0,0));
        add(new Pos(0,0,0));
    }};
    private int flag = 0;
    private Pos currentPos;

    private long durationOfWalk = 0;


    public FakeNpcStrollGoal(@NotNull EntityCreature entityCreature) {
        super(entityCreature);
    }

    @Override
    public boolean shouldStart() {

        if(flag > 2) flag = 0;

        List<Pos> blockedPos = new ArrayList<>();
        for (Pos pos : lastQuest) {
            if (pos != null) {
                blockedPos.add(pos);
            }
        }

        Pos randomPos = UnitedAPI.getSafeClosestTaskButWithoutThese(this.getEntityCreature(),blockedPos).left.getPos();
        currentPos = randomPos;
        lastQuest.set(flag,randomPos);

        boolean shouldStart = System.currentTimeMillis() - lastStroll >= DELAY - random.nextDouble((double) DELAY /2);
        if (shouldStart){
            ((FakeNPC) entityCreature).playAnimation(Entity.Pose.SNEAKING,Entity.Pose.STANDING,5);
        }

        flag += 1;
        return shouldStart;
    }

    @Override
    public void start() {

            durationOfWalk = System.currentTimeMillis();

            double amp = random.nextDouble(-posAmplifier,posAmplifier);
            currentPos = currentPos.add(amp,0,amp);


            entityCreature.getNavigator().setPathTo(currentPos);
    }

    @Override
    public void tick(long time) {
    }

    @Override
    public boolean shouldEnd() {

        Point pos = entityCreature.getNavigator().getPathPosition();
        if(pos == null) return true;

        if(System.currentTimeMillis() - durationOfWalk > 5000) return true;

        return entityCreature.getPosition().distanceSquared(pos) < 1.5;
    }

    public boolean isPosSimplifyWith(Pos pos,Pos pos2,double epsilon){
        return Math.abs(pos.x() - pos2.x()) < epsilon &&
               Math.abs(pos.y() - pos2.y()) < epsilon &&
               Math.abs(pos.z() - pos2.z()) < epsilon;

    }
    @Override
    public void end() {
        this.lastStroll = System.currentTimeMillis();
    }



}
