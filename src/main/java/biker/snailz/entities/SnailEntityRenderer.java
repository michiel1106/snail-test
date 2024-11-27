package biker.snailz.entities;

import biker.snailz.Snailz;
import biker.snailz.SnailzClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class SnailEntityRenderer<T extends SnailEntity> extends MobEntityRenderer<SnailEntity, SnailEntityRenderState, SnailEntityModel<SnailEntity>> {

    public SnailEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SnailEntityModel(context.getPart(SnailzClient.SNAILMODEL)), 0.2f);
    }
    private final EntityModel normalModel = this.getModel();

    @Override
    public SnailEntityRenderState createRenderState() {
        return new SnailEntityRenderState();
    }


    @Override
    public Identifier getTexture(SnailEntityRenderState state) {
        return Identifier.of(Snailz.MOD_ID, "textures/entity/snails/snail.png");
    }

//    public void updateRenderState(SnailEntity snailentity, SnailEntityRenderState snailEntityRenderState, float f) {
//        super.updateRenderState(snailentity, snailEntityRenderState, f);
//        snailEntityRenderState.idleAnimationState.copyFrom(SnailEntity.MO);
//    }

}