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

        ModelPartData bb_main = modelPartData.addChild("snail", ModelPartBuilder.create().uv(0, 0).cuboid(-0.95F, -6.0F, -6.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(3, 3).cuboid(1.05F, -6.0F, -6.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 1).cuboid(-0.95F, -5.0F, -6.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(3, 4).cuboid(1.05F, -5.0F, -6.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 2).cuboid(-0.95F, -4.0F, -6.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(3, 5).cuboid(1.05F, -4.0F, -6.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 16).cuboid(1.05F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 16).cuboid(0.05F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 16).cuboid(-0.95F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(14, 17).cuboid(1.05F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 17).cuboid(0.05F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 17).cuboid(-0.95F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.05F, -7.0F, -3.0F, 5.0F, 7.0F, 7.0F, new Dilation(0.0F))
                .uv(1, 15).cuboid(-0.95F, -3.0F, -6.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }


}
