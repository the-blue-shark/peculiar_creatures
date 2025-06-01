package net.the_blue_shark.peculiar_creatures.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;
import net.the_blue_shark.peculiar_creatures.entity.custom.SmurfCatEntity;

public class SmurfCatRenderer  extends MobEntityRenderer<SmurfCatEntity, SmurfCatRenderState, SmurfCatModel> {

    public SmurfCatRenderer(EntityRendererFactory.Context context) {
        super(context, new SmurfCatModel(context.getPart(SmurfCatModel.SMURF_CAT)), 0.75f);
    }

    @Override
    public Identifier getTexture(SmurfCatRenderState state) {
        return Identifier.of(PeculiarCreaturesMod.MOD_ID, "textures/entity/smurf_cat.png");
    }

    @Override
    public void render(SmurfCatRenderState state, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(state.baby) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(state, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public SmurfCatRenderState createRenderState() {
        return new SmurfCatRenderState();
    }

    @Override
    public void updateRenderState(SmurfCatEntity livingEntity, SmurfCatRenderState livingEntityRenderState, float f) {
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.idleAnimationState.copyFrom(livingEntity.idleAnimationState);
    }
}
