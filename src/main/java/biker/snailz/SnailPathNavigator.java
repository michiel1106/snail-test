package biker.snailz;

import biker.snailz.entities.SnailEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SnailPathNavigator {

    private SnailEntity snail;
    private List<PathNode> nodes;
    private int currentNodeIndex = 0;
    private double speed;

    public SnailPathNavigator(SnailEntity snail, Path path, double speed) {
        this.snail = snail;
        //this.nodes = path.getno(); // Assuming Path has a getNodes() method
        this.speed = speed;
        this.currentNodeIndex = 0;
    }

    public void navigate() {
        if (currentNodeIndex >= nodes.size()) {
            return; // Path is complete
        }

        PathNode currentNode = nodes.get(currentNodeIndex);

        // Navigate to the current node
        snail.getNavigation().startMovingTo(
                currentNode.x + 0.5, // Center the snail on the node
                currentNode.y,
                currentNode.z + 0.5,
                speed
        );

        // Check if the snail has reached the current node
        if (hasReachedNode(currentNode)) {
            currentNodeIndex++; // Proceed to the next node
        }
    }

    private boolean hasReachedNode(PathNode node) {
        BlockPos currentPos = snail.getBlockPos();
        return Math.abs(currentPos.getX() - node.x) < 1 &&
                Math.abs(currentPos.getY() - node.y) < 1 &&
                Math.abs(currentPos.getZ() - node.z) < 1;
    }

    public boolean isFinished() {
        return currentNodeIndex >= nodes.size();
    }
}
