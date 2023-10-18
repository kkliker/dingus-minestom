package qship.goals;

import com.extollit.gaming.ai.path.HydrazinePathFinder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.timer.ExecutionType;
import org.jetbrains.annotations.NotNull;
import qship.Dingus;
import qship.TaskFactory;
import qship.fakeplayers.FakeNPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class CityNPCStrollGoal extends GoalSelector {

        private static final long DELAY = 1000;
        private final Vector<Pos> lastFivePos = new Vector(5);
        private final int link = 0;

        private final static int posAmplifier = 1;

        private final Random random = new Random();

        private long lastStroll;

        public CityNPCStrollGoal(@NotNull EntityCreature entityCreature) {
            super(entityCreature);

        }

        @Override
        public boolean shouldStart() {

            boolean shouldStart = System.currentTimeMillis() - lastStroll >= DELAY - random.nextDouble(DELAY/2);
            if (shouldStart){
                ((FakeNPC) entityCreature).playAnimation(Entity.Pose.SNEAKING,Entity.Pose.STANDING,5);
            }

            return shouldStart;
        }

        @Override
        public void start() {
            MinecraftServer.getSchedulerManager().scheduleNextProcess(() ->{

                List<TaskFactory.InterestingLocation> closest = Dingus.NPCWALKPOS;

                Pos randomPos = closest.get(random.nextInt(closest.size())).pos;

                /*while(lastFivePos.contains(randomPos)){
                   randomPos = closest.get(random.nextInt(closest.size()));
                }
                if(lastFivePos.size() >= 5) {
                    lastFivePos.remove(link);
                }
                System.out.println(lastFivePos.size());
                lastFivePos.add(randomPos);

                 */
                System.out.println("choosed");
                double amp = random.nextDouble(-posAmplifier,posAmplifier);
                randomPos = randomPos.add(amp,0,amp);

                entityCreature.getNavigator().setPathTo(randomPos);

            }, ExecutionType.ASYNC);
        }

        @Override
        public void tick(long time) {
        }

        @Override
        public boolean shouldEnd() {

            Point pos = entityCreature.getNavigator().getPathPosition();
            if(pos == null){
                return true;
            }

            if(entityCreature.getPosition().distanceSquared(pos) < 2){
                System.out.println("ended2");
                return true;
            }
            return false;
        }

        @Override
        public void end() {
            this.lastStroll = System.currentTimeMillis();
        }

        private List<TaskFactory.InterestingLocation> getClosestLocation(){

            TaskFactory.InterestingLocation closestPos = Dingus.NPCWALKPOS.get(0);
            TaskFactory.InterestingLocation closestPos2 = Dingus.NPCWALKPOS.get(1);

            double distanceSquared = entityCreature.getDistanceSquared(closestPos.pos);
            for (TaskFactory.InterestingLocation location : Dingus.NPCWALKPOS) {
                double distanceSquared2 = entityCreature.getPosition().distanceSquared(location.pos);
                if (distanceSquared > distanceSquared2){
                    closestPos2 = closestPos;
                    closestPos = location;
                }
            }
            System.out.println(closestPos2 + "  " + closestPos );
            return List.of(closestPos,closestPos2);
        }

        private boolean nearAvaiblePos(){
            Point pos = entityCreature.getNavigator().getPathPosition();
            if(pos == null){
                return true;
            }

            if(entityCreature.getPosition().distanceSquared(pos) < 2){
                System.out.println("ended2");
                return true;
            }
            return false;
        }



}