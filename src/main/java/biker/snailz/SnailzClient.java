package biker.snailz;

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


        /*
         * Registers our Cube Entity's renderer, which provides a model and texture for the entity.
         *
         * Entity Renderers can also manipulate the model before it renders based on entity context (EndermanEntityRenderer#render).
         */
        EntityRendererRegistry.register(Snailz.SNAIL, (context) -> {
            return new SnailEntityRenderer(context);
        });
        // In 1.17, use EntityRendererRegistry.register (seen below) instead of EntityRendererRegistry.INSTANCE.register (seen above)
        EntityRendererRegistry.register(Snailz.SNAIL, (context) -> {
            return new SnailEntityRenderer(context);
        });

        EntityModelLayerRegistry.registerModelLayer(SNAILMODEL, SnailEntityModel::getTexturedModelData);
    }
}
