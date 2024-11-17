package biker.snailz.entities;

import biker.snailz.Snailz;
import com.mojang.serialization.Dynamic;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.World;

public class SnailEntity extends PathAwareEntity {

    public SnailEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }


    public static final float WALKING_SPEED = 0.2f;

    @Override
    public boolean cannotDespawn() {
        return true;
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

        this.getBrain().tick((ServerWorld)this.getWorld(), this);

        profiler.pop();

        profiler.push("snailActivityTick");
        SnailBrain.tickActivities(this);
        profiler.pop();

        super.mobTick(world);
    }




}
