package biker.snailz;

import biker.snailz.entities.SnailEntity;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.UUID;


public class Snailz implements ModInitializer {
	public static final String MOD_ID = "snailz";


	public static final EntityType<SnailEntity> SNAIL = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of("snailz", "bikersnail"),
			EntityType.Builder.create(SnailEntity::new, SpawnGroup.CREATURE).dimensions(0.35f, 0.35f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Snailz.MOD_ID, "bikersnail"))));



	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(SNAIL, SnailEntity.createsnailattributes());


		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("target")
					.then(CommandManager.argument("playerUsername", StringArgumentType.string())
							.executes(Snailz::execute)));
		});





	}

	private static int execute(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerWorld world = source.getWorld();
		String playerUsername = StringArgumentType.getString(context, "playerUsername");

		// Find the player by username manually
		PlayerEntity player = world.getServer().getPlayerManager().getPlayer(playerUsername);

		UUID playeruserid = player.getUuid();

		if (player == null) {
			//source.sendError(Text.literal("Player not found: " + playerUsername));
			return 0;
		}

		// Find the nearest SnailEntity to the command executor
		SnailEntity nearestSnail = world.getClosestEntity(SnailEntity.class, null,
                (LivingEntity) source.getEntity(), source.getPosition().x, source.getPosition().y, source.getPosition().z,
				new Box(source.getPosition().subtract(10, 10, 10), source.getPosition().add(10, 10, 10)));

		if (nearestSnail == null) {
			source.sendError(Text.literal("No snail entities found nearby!"));
			return 0;
		}

		// Set the snail's target to the specified player
		nearestSnail.setTargetPlayer(playeruserid);
		//source.sendFeedback(Text.literal("Snail is now targeting: " + player.getName().getString()), true);

		return 1;
	}
}