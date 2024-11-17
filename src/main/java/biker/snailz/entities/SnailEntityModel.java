package biker.snailz.entities;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

public class SnailEntityModel extends EntityModel<LivingEntityRenderState> {


    protected SnailEntityModel(ModelPart root) {
        super(root);
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData bb_main = modelPartData.addChild("snail", ModelPartBuilder.create().uv(-2, -2).cuboid(-1.0F, -4.0F, -1.0F, 3.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-0.5F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        return TexturedModelData.of(modelData, 16, 16);
    }

}
