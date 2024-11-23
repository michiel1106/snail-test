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


    public SnailTargetPlayerGoal(SnailEntity snail) {
        this.snail = snail;
        this.targetPlayer = null;
    }

    @Override
    public boolean canStart() {
        targetPlayer = snail.getTargetPlayer(); // Get target player's position
        return targetPlayer != null;
    }

    @Override
    public void start() {

    }

    @Override
    public void tick() {

    }

  //  @Override
  //  public boolean shouldContinue() {
  //      return targetPlayer != null && pathIndex < pathCoordinates.size();
  //  }
}
