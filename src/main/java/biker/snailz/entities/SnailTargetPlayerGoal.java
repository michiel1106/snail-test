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
        System.out.println("Snail's current position: " + snail.getPos());
        System.out.println("Path target: " + pathCoordinates.get(pathIndex));
        System.out.println("Player's position: " + targetPlayer);


        System.out.println("Executing path calculation...");
        pathCoordinates.clear();
        pathIndex = 0;

        // Define start and end nodes for pathfinding
        AStarPathFinder.Node start = new AStarPathFinder.Node(
                (int) snail.getX(),
                (int) snail.getY(),
                (int) snail.getZ()
        );
        AStarPathFinder.Node end = new AStarPathFinder.Node(
                (int) Math.floor(targetPlayer.getX()),
                (int) Math.floor(targetPlayer.getY()),
                (int) Math.floor(targetPlayer.getZ())
        );

        System.out.println("Pathfinder start: " + start + ", end: " + end);

        // Generate the path
        List<AStarPathFinder.Node> path = AStarPathFinder.findPath(start, end, snail.getWorld());
        if (path == null || path.isEmpty()) {
            System.out.println("No path found!");
            return;
        }

        // Convert path to BlockPos
        pathCoordinates = path.stream()
                .map(node -> new BlockPos(node.x, node.y, node.z))
                .collect(Collectors.toList());

        System.out.println("Generated path: " + pathCoordinates);
    }

    @Override
    public void tick() {
        targetPlayer = snail.getTargetPlayer();
        if (targetPlayer == null) return;

        // Ensure the player's target block is correctly defined
        BlockPos targetBlock = new BlockPos(
                (int) Math.floor(targetPlayer.getX()),
                (int) Math.floor(targetPlayer.getY()),
                (int) Math.floor(targetPlayer.getZ())
        );

        // Recalculate path if needed
        if (pathCoordinates.isEmpty() || pathIndex >= pathCoordinates.size() ||
                !pathCoordinates.get(pathCoordinates.size() - 1).equals(targetBlock)) {
            System.out.println("Recalculating path to target block: " + targetBlock);
            start(); // Recalculate path
        }

        // Ensure pathCoordinates is not empty before accessing
        if (!pathCoordinates.isEmpty() && pathIndex < pathCoordinates.size()) {
            BlockPos currentTarget = pathCoordinates.get(pathIndex);
            Vec3d snailPos = snail.getPos();

            // Log movement details for debugging
            System.out.println("Snail at: " + snailPos + ", moving to: " + currentTarget);

            // Check if the snail is close enough to the current target block
            if (snailPos.isInRange(new Vec3d(
                    currentTarget.getX() + 0.5,
                    currentTarget.getY(),
                    currentTarget.getZ() + 0.5
            ), 1.0)) {
                pathIndex++; // Progress to the next waypoint
            } else {
                // Move toward the current target block
                snail.getNavigation().startMovingTo(
                        currentTarget.getX() + 0.5,
                        currentTarget.getY(),
                        currentTarget.getZ() + 0.5,
                        SnailEntity.WALKING_SPEED
                );
            }
        } else {
            System.out.println("No valid path to follow!");
        }
    }

  //  @Override
  //  public boolean shouldContinue() {
  //      return targetPlayer != null && pathIndex < pathCoordinates.size();
  //  }
}
