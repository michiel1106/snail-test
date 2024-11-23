package biker.snailz;

import biker.snailz.entities.SnailEntity;
import biker.snailz.entities.SnailTargetPlayerGoal;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.literal;


public class Snailz implements ModInitializer {
	public static final String MOD_ID = "snailz";


	public static final EntityType<SnailEntity> SNAIL = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of("snailz", "bikersnail"),
			EntityType.Builder.create(SnailEntity::new, SpawnGroup.CREATURE).dimensions(0.55f, 0.55f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Snailz.MOD_ID, "bikersnail"))));



	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(SNAIL, SnailEntity.createsnailattributes());

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("foo")
				.executes(context -> {

					System.out.println(context.getSource().getPlayer().getUuidAsString());

					return 1;
				})));





		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			PathCommand.register(dispatcher, registryAccess);
		});







	}


}