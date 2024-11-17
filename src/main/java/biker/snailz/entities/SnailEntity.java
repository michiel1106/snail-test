package biker.snailz.entities;

import com.mojang.serialization.Dynamic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.World;

import java.util.UUID;

public class SnailEntity extends HostileEntity {


    private UUID targetPlayerUUID;

    protected void initGoals() {

        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(1, new PowderSnowJumpGoal(this, this.getWorld()));

        this.goalSelector.add(1, new SnailTargetPlayerGoal(this));

        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.5, false));

        //this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));

        //this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
    }

    public static final float WALKING_SPEED = 0.2f;

    public SnailEntity(EntityType<? extends SnailEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createsnailattributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.MAX_HEALTH, 50.0).add(EntityAttributes.MOVEMENT_SPEED, 0.15).add(EntityAttributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);


        if (targetPlayerUUID != null) {
            nbt.putUuid("TargetPlayerUUID", targetPlayerUUID);
        }

    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("TargetPlayerUUID")) {
            targetPlayerUUID = nbt.getUuid("TargetPlayerUUID");
        }
    }

    public void setTargetPlayer(UUID player) {
        this.targetPlayerUUID = player;
        this.markEffectsDirty();  // Make sure the NBT gets saved.
    }

    public PlayerEntity getTargetPlayer() {
        if (targetPlayerUUID != null) {
            Entity entity = getWorld().getPlayerByUuid(targetPlayerUUID);
            if (entity instanceof PlayerEntity) {
                return (PlayerEntity) entity;
            }
        }
        return null;
    }


    //brain
    @Override
    protected Brain.Profile<SnailEntity> createBrainProfile() {
        return SnailBrain.createBrainProfile();
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return SnailBrain.create(this, this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Brain<SnailEntity> getBrain() {
        return (Brain<SnailEntity>) super.getBrain();
    }


    @Override
    protected void mobTick(ServerWorld world) {
        Profiler profiler = Profilers.get();
        profiler.push("snailBrain");

        this.getBrain().tick((ServerWorld) this.getWorld(), this);

        profiler.pop();

        profiler.push("snailActivityTick");
        SnailBrain.tickActivities(this);
        profiler.pop();

        super.mobTick(world);

           // if (!getWorld().isClient) { // Ensure this is only executed server-side
           //     PlayerEntity nearestPlayer = world.getClosestPlayer(this, 16); // 16 is the radius, adjust as needed
           //     if (nearestPlayer != null) {
           //         this.setTargetPlayer(nearestPlayer);
           //     }
           // }



    }




}
