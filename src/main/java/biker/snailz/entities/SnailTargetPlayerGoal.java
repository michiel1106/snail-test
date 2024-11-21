package biker.snailz.entities;



import biker.snailz.AStarPathFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;



public class SnailTargetPlayerGoal extends Goal {
    private World world;
    private Path path;
    private final SnailEntity snail;
    private Vec3d targetPlayer;

    public SnailTargetPlayerGoal(SnailEntity snail) {
        this.snail = snail;
        this.targetPlayer = null;
    }

    @Override
    public boolean canStart() {
        targetPlayer = snail.getTargetPlayer(); // This will now return the position of the resolved player
        Vec3d targetPos = snail.getTargetPlayer();


        return targetPlayer != null;

    }



    @Override
    public void tick() {
        Vec3d targetPos = snail.getTargetPlayer();
        if (targetPos == null) return;

        //GoalBlock goal = new GoalBlock((int) targetPos.x, (int) targetPos.y, (int) targetPos.z);


        //path = this.snail.getNavigation().findPathTo(targetPos.x, targetPos.y, targetPos.z, 0);
        //snail.getNavigation().startMovingAlong(path, SnailEntity.WALKING_SPEED);


        //BaritoneAPI.getProvider().getPrimaryBaritone().

        //Path pathss = (Path) BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().getGoal();
        //snail.getNavigation().startMovingTo(targetPos.x, targetPos.y, targetPos.z, 0, 0.3);
        //snail.getNavigation().startMovingTo(targetPos.x, targetPos.y, targetPos.z, SnailEntity.WALKING_SPEED);

        snail.getNavigation().getNodeMaker().setCanWalkOverFences(true);
        snail.getNavigation().getNodeMaker().setCanEnterOpenDoors(true);
        snail.getNavigation().getNodeMaker().setCanSwim(true);
        snail.getNavigation().getNodeMaker().setCanOpenDoors(true);





        AStarPathFinder.Node start = new AStarPathFinder.Node((int) snail.getX(), (int) snail.getY(), (int) snail.getZ());
        AStarPathFinder.Node end = new AStarPathFinder.Node((int) targetPos.x, (int) targetPos.y, (int) targetPos.z);

        List<AStarPathFinder.Node> path = AStarPathFinder.findPath(start, end, world);

        if (!path.isEmpty()) {
            for (AStarPathFinder.Node node : path) {
                //System.out.println("Step: " + node.x + ", " + node.y + ", " + node.z);
                snail.getNavigation().startMovingTo(node.x, node.y, node.z, 0, SnailEntity.WALKING_SPEED);
            }
        } else {

            return;
        }



        //path.setNode(1, new PathNode((int) targetPos.x, (int) targetPos.y, (int) targetPos.z));




    }
}
