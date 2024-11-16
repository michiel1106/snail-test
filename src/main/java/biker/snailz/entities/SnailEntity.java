package biker.snailz.entities;

import biker.snailz.Snailz;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SnailEntity extends PathAwareEntity {

    public SnailEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }





}
