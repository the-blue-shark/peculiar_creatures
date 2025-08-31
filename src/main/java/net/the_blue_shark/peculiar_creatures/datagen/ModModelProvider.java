package net.the_blue_shark.peculiar_creatures.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.the_blue_shark.peculiar_creatures.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SPECTRE_DISC, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMURF_CAT_SPAWN_EGG, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMURF_CAT_HAT, Models.GENERATED);
    }
}
