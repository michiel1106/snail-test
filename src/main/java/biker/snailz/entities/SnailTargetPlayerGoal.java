package biker.snailz.entities;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SnailTargetPlayerGoal extends Goal {

    private final SnailEntity snail;
    private PlayerEntity targetPlayer;

    public SnailTargetPlayerGoal(SnailEntity snail) {
        this.snail = snail;
        this.targetPlayer = null;
    }

    @Override
    public boolean canStart() {
        targetPlayer = snail.getTargetPlayer();
        return targetPlayer != null;
    }

    @Override
    public void tick() {
        if (targetPlayer == null) return;

        // Make sure the player is still valid
        if (!targetPlayer.isAlive()) {
            targetPlayer = null;
            return;
        }

        // Move the snail towards the player
        double distance = this.snail.squaredDistanceTo(targetPlayer);
        if (distance > 0.0D) { // Ignore very close targets
            double xDiff = targetPlayer.getX() - snail.getX();
            double yDiff = targetPlayer.getY() - snail.getY();
            double zDiff = targetPlayer.getZ() - snail.getZ();

            double distanceToTarget = MathHelper.sqrt((float) (xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));

            // Normalize the movement vector
            snail.getNavigation().startMovingTo(targetPlayer, 0.2);
        }
    }
}
