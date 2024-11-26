package biker.snailz.entities;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;


public class SnailEntityModel<T extends SnailEntity> extends EntityModel<SnailEntityRenderState> {


    protected SnailEntityModel(ModelPart root) {
        super(root);
    }


    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData shell = modelPartData.addChild("shell", ModelPartBuilder.create().uv(0, 0).cuboid(-2.75F, -4.0F, -3.0F, 5.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.25F, 21.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(3, 3).cuboid(0.5F, -4.0F, -2.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.5F, -4.0F, -2.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 14).cuboid(-1.5F, -1.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(14, 14).cuboid(-1.5F, 0.0F, 6.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 22.0F, -4.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }



    @Override
    public void setAngles(SnailEntityRenderState snailEntityRenderState) {
        super.setAngles(snailEntityRenderState);
        this.getRootPart().traverse().forEach(ModelPart::resetTransform);



        //this.animate(SnailEntityRenderState., SnailEntityModelAnimation.movement, SnailEntityRenderState, 1f);
        this.animate(SnailEntityModelAnimation.movement);
        this.
        //this.animateWalking(SnailEntityModelAnimation.movement, snailEntityRenderState.limbFrequency, snailEntityRenderState.limbAmplitudeMultiplier, 1, 2);


    }





}

//public class SnailEntityModel extends EntityModel<Entity> {
//    private final ModelPart shell;
//    private final ModelPart body;
//    public SnailEntityModel(ModelPart root) {
//        this.shell = root.getChild("shell");
//        this.body = root.getChild("body");
//    }
//    public static TexturedModelData getTexturedModelData() {
//        ModelData modelData = new ModelData();
//        ModelPartData modelPartData = modelData.getRoot();
//        ModelPartData shell = modelPartData.addChild("shell", ModelPartBuilder.create().uv(0, 0).cuboid(-2.75F, -4.0F, -3.0F, 5.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.25F, 21.0F, 0.0F));
//
//        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(3, 3).cuboid(0.5F, -4.0F, -2.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
//                .uv(0, 0).cuboid(-1.5F, -4.0F, -2.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
//                .uv(0, 14).cuboid(-1.5F, -1.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
//                .uv(14, 14).cuboid(-1.5F, 0.0F, 6.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 22.0F, -4.0F));
//        return TexturedModelData.of(modelData, 64, 64);
//    }
//    @Override
//    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//    }
//    @Override
//    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
//        shell.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
//        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
//    }
//}
