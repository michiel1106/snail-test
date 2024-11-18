package biker.snailz.entities;

import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SnailTargetPlayerGoal extends Goal {

    private final SnailEntity snail;
    private Vec3d targetPlayer;

    public SnailTargetPlayerGoal(SnailEntity snail) {
        this.snail = snail;
        this.targetPlayer = null;
    }

    @Override
    public boolean canStart() {
        targetPlayer = snail.getTargetPlayer(); // This will now return the position of the resolved player
        return targetPlayer != null;
    }

    @Override
    public void tick() {
        Vec3d targetPos = snail.getTargetPlayer();
        if (targetPos == null) return;


            snail.getNavigation().startMovingTo(targetPos.x, targetPos.y, targetPos.z, SnailEntity.WALKING_SPEED);


    }
}
