package biker.snailz;

import biker.snailz.entities.ModEntityModelLayers;
import biker.snailz.entities.SnailEntityModel;
import biker.snailz.entities.SnailEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class SnailzClient implements ClientModInitializer {
    public static final EntityModelLayer SNAILMODEL = new EntityModelLayer(Identifier.of("snailz", "snails"), "bikersnail");
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Snailz.SNAIL, SnailEntityRenderer::new);

        EntityRendererRegistry.register(Snailz.SNAIL, (context) -> {
            return new SnailEntityRenderer(context);
        });

        ModEntityModelLayers.registerEntityModelLayers();

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SNAIL, SnailEntityModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(SNAILMODEL, SnailEntityModel::getTexturedModelData);
    }
}
