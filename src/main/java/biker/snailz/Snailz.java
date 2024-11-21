package biker.snailz;

import biker.snailz.entities.SnailEntity;
import biker.snailz.entities.SnailTargetPlayerGoal;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

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
					World world = context.getSource().getWorld();
					AStarPathFinder.Node start = new AStarPathFinder.Node(32, 70, 32);
					AStarPathFinder.Node end = new AStarPathFinder.Node(10, 76, 90);

					System.out.println("Starting pathfinding...");

					// Find the path, passing the world to ensure walkability checks
					List<AStarPathFinder.Node> path = AStarPathFinder.findPath(start, end, world);
					System.out.println(path);


					return 1;
				})));




		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			PathCommand.register(dispatcher);
		});







	}


}