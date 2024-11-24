package biker.snailz.entities;

import biker.snailz.AStarPathFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SnailTargetPlayerGoal extends Goal {
    private final SnailEntity snail;
    private Vec3d targetPlayer;
    private int currentIndex;
    private final World world;
    private Vec3d lastTargetPlayerPos;
    List<AStarPathFinder.Node> path1;
    List<BlockPos> path2 = new ArrayList<>();


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

        if (lastTargetPlayerPos == null || !lastTargetPlayerPos.equals(targetPlayer)) {

            currentIndex = 0;
            // Update the last known position
            lastTargetPlayerPos = targetPlayer;
            if (path2 != null) {
                path2.clear();

            }
            if (path1 != null){
                path1.clear();
            }
        }

            if (path2.isEmpty()) {

                AStarPathFinder.Node start = new AStarPathFinder.Node((int) snail.getX(), (int) snail.getY(), (int) snail.getZ());
                AStarPathFinder.Node end = new AStarPathFinder.Node((int) targetPlayer.x, (int) targetPlayer.y, (int) targetPlayer.z);
                List<AStarPathFinder.Node> path1 = AStarPathFinder.findPath(start, end, world);

                System.out.println(snail.getX() + " " + snail.getY()+ " " + snail.getZ());
                System.out.println(targetPlayer.x + " " + targetPlayer.y + " " + targetPlayer.z);

                for (AStarPathFinder.Node node : path1) {
                BlockPos blockpos = new BlockPos(node.x, node.y, node.z);
                System.out.println("Nodes: " + node.x + " " + node.y + " " + node.z);
                path2.add(blockpos);
            }
        }

        if (path2 == null || path2.isEmpty()) {
            System.out.println("path 2 is empty");
            return;
        }

        // please ive been working on this for hours pleasepleaseplease just work
        if (snail.getBlockPos().isWithinDistance(path2.get(currentIndex), 1.05)) {
            currentIndex++;
            if (currentIndex >= path2.size()) {
                currentIndex = 0;
                path2.clear();
                // praying that this will work
                return;
            }
        }

        // JUST GO TO THE PLACE PLEASE
        // gonna try client right now..
        // didnt work exactly :( but I made the block detection less sensitive so thats good.
        // the snail really likes to go to 1 69 -7 idk why

        //omg im getting somewhere, node adding works, and like the snail goes to you but then goes back, which is expected now that I think about it. but I hope I can fix
        snail.getNavigation().startMovingTo(path2.get(currentIndex).getX(),
                path2.get(currentIndex).getY(),
                path2.get(currentIndex).getZ(),
                SnailEntity.WALKING_SPEED); // goinginsanerightnowaaaaaa
    }
}

  //  @Override
  //  public boolean shouldContinue() {
  //      return targetPlayer != null && pathIndex < pathCoordinates.size();
  //  }

