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
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;


public class Snailz implements ModInitializer {
	public static final String MOD_ID = "snailz";


	public static final EntityType<SnailEntity> SNAIL = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of("snailz", "bikersnail"),
			EntityType.Builder.create(SnailEntity::new, SpawnGroup.CREATURE).dimensions(0.35f, 0.35f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Snailz.MOD_ID, "bikersnail"))));



	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(SNAIL, SnailEntity.createsnailattributes());

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("foo")
				.executes(context -> {
					// For versions below 1.19, replace "Text.literal" with "new LiteralText".
					// For versions below 1.20, remode "() ->" directly.
					ServerPlayerEntity whatever = context.getSource().getPlayer();
					System.out.println(whatever);

					return 1;
				})));






	}


}