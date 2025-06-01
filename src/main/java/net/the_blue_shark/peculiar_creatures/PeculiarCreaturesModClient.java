package net.the_blue_shark.peculiar_creatures;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.the_blue_shark.peculiar_creatures.entity.ModEntities;
import net.the_blue_shark.peculiar_creatures.entity.client.SmurfCatModel;
import net.the_blue_shark.peculiar_creatures.entity.client.SmurfCatRenderer;
import net.the_blue_shark.peculiar_creatures.entity.custom.SmurfCatEntity;

public class PeculiarCreaturesModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(SmurfCatModel.SMURF_CAT, SmurfCatModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.SMURF_CAT, SmurfCatRenderer::new);

    }
}
