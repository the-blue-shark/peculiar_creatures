package net.the_blue_shark.peculiar_creatures.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;
import net.the_blue_shark.peculiar_creatures.entity.client.animation.SmurfCatAnimations;
import net.the_blue_shark.peculiar_creatures.entity.custom.SmurfCatEntity;

public class SmurfCatModel extends EntityModel<SmurfCatRenderState> {
    public static final EntityModelLayer SMURF_CAT = new EntityModelLayer(Identifier.of(PeculiarCreaturesMod.MOD_ID, "body"), "main");
    private final ModelPart body;
    private final ModelPart base;
    private final ModelPart back;
    private final ModelPart arms;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart torso;
    private final ModelPart legs;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart head;
    private final ModelPart face;
    private final ModelPart cap;

    public SmurfCatModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.base = this.body.getChild("base");
        this.back = this.base.getChild("back");
        this.arms = this.base.getChild("arms");
        this.left_arm = this.arms.getChild("left_arm");
        this.right_arm = this.arms.getChild("right_arm");
        this.torso = this.base.getChild("torso");
        this.legs = this.base.getChild("legs");
        this.right_leg = this.legs.getChild("right_leg");
        this.left_leg = this.legs.getChild("left_leg");
        this.head = this.body.getChild("head");
        this.face = this.head.getChild("face");
        this.cap = this.head.getChild("cap");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.rotation(0.25F, 24.0F, -1.0F));

        ModelPartData base = body.addChild("base", ModelPartBuilder.create(), ModelTransform.rotation(0.0F, 0.0F, 0.0F));

        ModelPartData back = base.addChild("back", ModelPartBuilder.create().uv(16, 8).cuboid(-0.75F, -7.0F, 1.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.rotation(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = back.addChild("cube_r1", ModelPartBuilder.create().uv(1, 1).cuboid(0.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -8.5F, 4.0F, 0.0F, 0.0F, 1.5708F));

        ModelPartData cube_r2 = back.addChild("cube_r2", ModelPartBuilder.create().uv(24, 8).cuboid(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -6.0F, 3.0F, -0.5236F, 0.0F, 0.6981F));

        ModelPartData arms = base.addChild("arms", ModelPartBuilder.create(), ModelTransform.rotation(0.25F, -7.0F, 0.5F));

        ModelPartData left_arm = arms.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.rotation(0.0F, 0.0F, 0.0F));

        ModelPartData left_arm_r1 = left_arm.addChild("left_arm_r1", ModelPartBuilder.create().uv(18, 24).cuboid(-1.0F, -2.0F, -1.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, 2.0F, 0.5F, 0.0F, 0.0F, -0.1309F));

        ModelPartData right_arm = arms.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.rotation(0.0F, 0.0F, 0.0F));

        ModelPartData right_arm_r1 = right_arm.addChild("right_arm_r1", ModelPartBuilder.create().uv(22, 24).cuboid(-1.0F, -2.0F, -1.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 2.0F, 0.5F, 0.0F, 0.0F, 0.1309F));

        ModelPartData torso = base.addChild("torso", ModelPartBuilder.create().uv(16, 15).cuboid(-1.0F, 0.0F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(8, 26).cuboid(-1.0F, -3.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.rotation(-0.25F, -4.0F, 0.5F));

        ModelPartData legs = base.addChild("legs", ModelPartBuilder.create(), ModelTransform.rotation(0.0F, 0.0F, 0.0F));

        ModelPartData right_leg = legs.addChild("right_leg", ModelPartBuilder.create().uv(0, 26).cuboid(-0.5F, 0.25F, -0.25F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(20, 12).cuboid(-0.5F, 2.25F, -1.25F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.rotation(-0.5F, -3.25F, 0.25F));

        ModelPartData left_leg = legs.addChild("left_leg", ModelPartBuilder.create().uv(0, 23).cuboid(-0.5F, 2.0F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(4, 26).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.rotation(1.0F, -3.0F, 0.5F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create(), ModelTransform.rotation(0.0F, 0.0F, 0.0F));

        ModelPartData face = head.addChild("face", ModelPartBuilder.create().uv(0, 15).cuboid(-5.0F, -4.0F, -1.5F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.rotation(3.25F, -7.0F, 0.0F));

        ModelPartData left_ear_r1 = face.addChild("left_ear_r1", ModelPartBuilder.create().uv(12, 23).cuboid(-1.0F, -3.0F, 1.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_ear_r1 = face.addChild("right_ear_r1", ModelPartBuilder.create().uv(6, 23).cuboid(-1.0F, -3.0F, 1.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cap = head.addChild("cap", ModelPartBuilder.create().uv(17, 19).cuboid(-2.0F, -6.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 8).cuboid(-3.0F, -4.0F, -2.5F, 5.0F, 2.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-4.0F, -2.0F, -3.5F, 7.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.rotation(0.75F, -10.0F, 0.5F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    public void setAngles(SmurfCatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(netHeadYaw, headPitch);

        this.animateWalking(SmurfCatAnimations.WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, SmurfCatAnimations.IDLE, ageInTicks, 1f);
    }

    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -25F, 45F);

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch *  0.017453292F;

    }


    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, int color) {
        body.render(matrices, vertexConsumer, light, overlay, color);
    }

    public ModelPart getPart() {
        return body;
    }
}