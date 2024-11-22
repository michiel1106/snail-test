package biker.snailz.entities;

import biker.snailz.AStarPathFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SnailTargetPlayerGoal extends Goal {
    private final SnailEntity snail;
    private Vec3d targetPlayer;
    private List<BlockPos> pathCoordinates;
    private List<AStarPathFinder.Node> path1; // List of BlockPos coordinates for the path
    private int pathIndex; // Tracks the current position in the path

    public SnailTargetPlayerGoal(SnailEntity snail) {
        this.snail = snail;
        this.targetPlayer = null;
        this.pathCoordinates = new ArrayList<>();
        this.pathIndex = 0;
    }

    @Override
    public boolean canStart() {
        targetPlayer = snail.getTargetPlayer(); // Get target player's position
        return targetPlayer != null;
    }

    @Override
    public void start() {
        System.out.println("executed start");
        if (path1 != null) {
            path1.clear();
        }
        pathCoordinates.clear();


        pathIndex = 0;

        // Initialize start and end nodes
        AStarPathFinder.Node start = new AStarPathFinder.Node(
                (int) snail.getX(),
                (int) snail.getY(),
                (int) snail.getZ()
        );

        AStarPathFinder.Node end = new AStarPathFinder.Node(
                (int) targetPlayer.getX(),
                (int) targetPlayer.getY(),
                (int) targetPlayer.getZ()
        );

        // Find the path
        List<AStarPathFinder.Node> path1 = AStarPathFinder.findPath(start, end, snail.getWorld());
        System.out.println(path1);
        if (path1 == null || path1.isEmpty()) {
            System.out.println("No path found!");
            return;
        }

        // Convert path to BlockPos
        pathCoordinates = path1.stream()
                .map(node -> new BlockPos(node.x, node.y, node.z))
                .collect(Collectors.toList());
    }

    @Override
    public void tick() {
        targetPlayer = snail.getTargetPlayer(); // Dynamically update player position
        if (targetPlayer == null) return;

        // Check if path needs recalculating
        if (pathCoordinates.isEmpty() || pathIndex >= pathCoordinates.size()) {
            System.out.println("pathindex" + pathIndex);
            System.out.println("path coords size" + pathCoordinates.size());
            System.out.println("doin pathcoordinatesempty stuff! snail position. X: " + snail.getX() + " Y: " + snail.getY() + " Z: " + snail.getZ() + "and target/player position: " + "  X: " + targetPlayer.getX() + "  Y: " + targetPlayer.getY() + "  Z: " + targetPlayer.getZ() + "or other player coords: " + "  X: " + targetPlayer.x + "  Y: " + targetPlayer.y + "  Z: " + targetPlayer.z);
            start(); // Recalculate the path
        }

        // Continue along the path
        if (!pathCoordinates.isEmpty() && pathIndex < pathCoordinates.size()) {
            BlockPos currentTarget = pathCoordinates.get(pathIndex);
            Vec3d snailPos = snail.getPos();

            // Check if close to current target
            if (snailPos.isInRange(new Vec3d(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ()), 1.5)) {
                pathIndex++; // Move to next waypoint
            } else {
                // Move towards the target
                snail.getNavigation().startMovingTo(
                        currentTarget.getX() + 0.5, // Center the target position
                        currentTarget.getY(),
                        currentTarget.getZ() + 0.5,
                        SnailEntity.WALKING_SPEED
                );
            }


        }


        if (Math.floor(targetPlayer.z) == Math.floor(snail.getZ()) && Math.floor(targetPlayer.x) == Math.floor(snail.getX()) && Math.floor(targetPlayer.y) == Math.floor(snail.getY()) ) {

            System.out.println("destination reached! snail position: " + snail.getX() + snail.getY() + snail.getZ() + "and target/player position: " + targetPlayer.getX() + targetPlayer.getY() + targetPlayer.getZ() + "or player coords: " + targetPlayer.x + targetPlayer.y + targetPlayer.z);
            start();
        }


    }


  //  @Override
  //  public boolean shouldContinue() {
  //      return targetPlayer != null && pathIndex < pathCoordinates.size();
  //  }
}
