package net.the_blue_shark.peculiar_creatures.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.the_blue_shark.peculiar_creatures.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.SPECTRE_DISC, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SMURF_CAT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SHREK_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.APPA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SHREK_HORN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SHREK_DISC, ModelTemplates.FLAT_ITEM);
    }
}
