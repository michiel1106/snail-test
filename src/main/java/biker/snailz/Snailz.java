package biker.snailz;

import biker.snailz.entities.SnailEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Snailz implements ModInitializer {
	public static final String MOD_ID = "snailz";


	public static final EntityType<SnailEntity> SNAIL = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of("snailz", "bikersnail"),
			EntityType.Builder.create(SnailEntity::new, SpawnGroup.CREATURE).dimensions(0.35f, 0.35f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Snailz.MOD_ID, "bikersnail"))));



	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(SNAIL, SnailEntity.createMobAttributes());
	}
}