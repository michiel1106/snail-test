package biker.snailz.entities;

import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.World;

import java.util.UUID;

public class SnailEntity extends HostileEntity {

    private World world;
    private String TargetPlayer1;


    protected void initGoals() {

        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(1, new PowderSnowJumpGoal(this, this.getWorld()));

        this.goalSelector.add(1, new SnailTargetPlayerGoal(this));

        this.goalSelector.add(2, new MeleeAttackGoal(this, 1, false));


        //this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));

        //this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
    }




    public static final float WALKING_SPEED = 0.25f;

    public SnailEntity(EntityType<? extends SnailEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createsnailattributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.MAX_HEALTH, 50.0).add(EntityAttributes.MOVEMENT_SPEED, 0.6).add(EntityAttributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    public String TargetPlayerUsername;

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);



        //nbt.putString("TargetPlayerUsername", TargetPlayerUsername);

        if (this.TargetPlayerUsername != null) {
            nbt.putString("Target", this.TargetPlayerUsername);
        }

    }

    @Override
    public void slowMovement(BlockState state, Vec3d multiplier) {
        if (!state.isOf(Blocks.COBWEB)) {
            super.slowMovement(state, multiplier);
        }


    }



    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);


        //if (nbt.contains("Target")) {
        //    String TargetPlayer = (nbt.getString("Target"));
        //    UUID TargetPlayer1 = UUID.fromString(TargetPlayer);

        //}
        if (nbt.contains("Target")) {
            this.TargetPlayerUsername = nbt.getString("Target");
            updateTargetFromNbt();
        }
    }

    public void setTargetPlayer(String playerUUID) {
        this.TargetPlayerUsername = playerUUID;
        this.markEffectsDirty(); // Ensure the NBT data is marked for saving
    }

    public void updateTargetFromNbt() {
        if (this.TargetPlayerUsername != null) {
            try {
                UUID playerUUID = UUID.fromString(this.TargetPlayerUsername);
                Entity entity = this.getWorld().getPlayerByUuid(playerUUID);

                if (entity instanceof PlayerEntity) {
                    // Use this player as the target
                    PlayerEntity targetPlayer = (PlayerEntity) entity;

                    // Debug log to ensure the target is being set
                    //System.out.println("Target resolved from NBT: " + targetPlayer.getName().getString());

                    // Logic to actually target this player
                    this.setTargetPlayer(targetPlayer.getUuidAsString());
                } else {
                    //System.out.println("Player UUID not found in the world: " + playerUUID);
                }
            } catch (IllegalArgumentException e) {
                //System.err.println("Invalid UUID format in NBT: " + this.TargetPlayerUsername);
            }
        }
    }

    public Vec3d getTargetPlayer() {
        if (this.TargetPlayerUsername != null) {
            try {
                UUID playerUUID = UUID.fromString(TargetPlayerUsername);
                Entity entity = this.getWorld().getPlayerByUuid(playerUUID);

                if (entity instanceof PlayerEntity player) {
                    return player.getPos();
                }
            } catch (IllegalArgumentException e) {
                //System.err.println("Invalid UUID format: " + TargetPlayerUsername);
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

        //System.out.println(getTargetPlayer());
        this.getBrain().tick((ServerWorld) this.getWorld(), this);

        profiler.pop();

        profiler.push("snailActivityTick");
        SnailBrain.tickActivities(this);
        profiler.pop();

        updateTargetFromNbt();
        super.mobTick(world);

        //PlayerEntity nearestPlayer = world.getClosestPlayer(this, 16);
        //if (nearestPlayer != null) {
        //    this.setTargetPlayer(nearestPlayer.getUuidAsString());
        //}


        //    if (!getWorld().isClient) { // Ensure this is only executed server-side
        //        PlayerEntity nearestPlayer = world.getClosestPlayer(this, 16); // 16 is the radius, adjust as needed
        //        if (nearestPlayer != null) {
        //            String nearestplayeruuid = nearestPlayer.getUuidAsString();
        //           System.out.println(nearestplayeruuid);
        //       }


        // System.out.println(nearestPlayer);
               // if (nearestPlayer != null) {
               //     this.setTargetPlayer(nearestPlayer);
               // }
            }



    }





