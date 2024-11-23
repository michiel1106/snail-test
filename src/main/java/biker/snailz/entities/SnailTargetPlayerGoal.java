package biker.snailz.entities;

import biker.snailz.AStarPathFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;




public class SnailTargetPlayerGoal extends Goal {
    private final SnailEntity snail;
    private Vec3d targetPlayer;
    private int currentIndex;
    private World world;
    List<AStarPathFinder.Node> path1;
    List<BlockPos> path2 = new ArrayList<>();
    //World world = snail.getWorld();

    public SnailTargetPlayerGoal(SnailEntity snail) {
        this.snail = snail;
        this.targetPlayer = null;
        this.world = this.snail.getWorld();
    }

    @Override
    public boolean canStart() {
        targetPlayer = snail.getTargetPlayer(); // Get target player's position
        return targetPlayer != null;
    }
    @Override
    public void start() {
        currentIndex = 0;

        if (path1 != null) {
            path1.clear();
        }

        if (path2 != null) {
            path2.clear();
        }

    }


    @Override
    public void tick() {

        if (world != null) {
            AStarPathFinder.Node start = new AStarPathFinder.Node((int) snail.getX(), (int) snail.getY(), (int) snail.getZ());
            AStarPathFinder.Node end = new AStarPathFinder.Node((int) targetPlayer.x, (int) targetPlayer.y, (int) targetPlayer.z);

            List<AStarPathFinder.Node> path1 = AStarPathFinder.findPath(start, end, world);


            for (AStarPathFinder.Node node : path1) {

                BlockPos blockpos = new BlockPos(node.x, node.y, node.z);
                path2.add(blockpos);


            }

            if (path1 == null || path1.isEmpty()) return;
            if (path2 == null || path2.isEmpty()) return;

            // If entity reaches the current target, move to the next point
            if (snail.getBlockPos().isWithinDistance(path2.get(currentIndex), 1.6)) {
                if (currentIndex < path2.size() - 1) { // Stay within bounds
                    currentIndex++;
                } else {

                        currentIndex = 0;

                }

            }

            // Navigate to the current target


            snail.getNavigation().startMovingTo(path2.get(currentIndex).getX(), path2.get(currentIndex).getY(), path2.get(currentIndex).getZ(), SnailEntity.WALKING_SPEED);
            System.out.println(currentIndex);
            System.out.println("Current Index: " + currentIndex + " / Path Size: " + path2.size());

        }

    }


}

  //  @Override
  //  public boolean shouldContinue() {
  //      return targetPlayer != null && pathIndex < pathCoordinates.size();
  //  }

