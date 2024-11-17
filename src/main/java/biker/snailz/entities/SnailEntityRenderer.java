package biker.snailz.entities;

import biker.snailz.Snailz;
import biker.snailz.SnailzClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class SnailEntityRenderer extends MobEntityRenderer<SnailEntity, LivingEntityRenderState, SnailEntityModel> {

    public SnailEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SnailEntityModel(context.getPart(SnailzClient.SNAILMODEL)), 0.2f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }


    @Override
    public Identifier getTexture(LivingEntityRenderState state) {
        return Identifier.of(Snailz.MOD_ID, "textures/entity/snails/snail.png");
    }
}