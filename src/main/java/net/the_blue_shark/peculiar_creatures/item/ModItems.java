package net.the_blue_shark.peculiar_creatures.item;

import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.core.api.item.PolymerSpawnEggItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;
import net.the_blue_shark.peculiar_creatures.entity.ModEntities;
import net.the_blue_shark.peculiar_creatures.item.custom.OptionalPolymerItem;
import net.the_blue_shark.peculiar_creatures.item.custom.PolymerMusicDisc;
import net.the_blue_shark.peculiar_creatures.item.custom.ShrekHornItem;
import net.the_blue_shark.peculiar_creatures.item.custom.SmurfCatHatItem;
import net.the_blue_shark.peculiar_creatures.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ModItems {
    public static final Item SPECTRE_DISC = registerItem("spectre_music_disc", settings ->
            new PolymerMusicDisc(settings, Items.MUSIC_DISC_CAT, true, ModSounds.SPECTRE_KEY));

    public static final Item SHREK_DISC = registerItem("shrek_music_disc", settings ->
            new PolymerMusicDisc(settings, Items.MUSIC_DISC_CREATOR, true, ModSounds.ALL_STAR_KEY));

    public static final Item SMURF_CAT_SPAWN_EGG = registerItem("smurf_cat_spawn_egg", setting -> new PolymerSpawnEggItem(Items.DOLPHIN_SPAWN_EGG, true, setting.spawnEgg(ModEntities.SMURF_CAT)) {
        @Override
        public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
            return PolymerResourcePackUtils.hasMainPack(context) ? super.getPolymerItemModel(stack, context, lookup) : null;
        }
    });

    public static final Item SHREK_SPAWN_EGG = registerItem("shrek_spawn_egg", setting -> new PolymerSpawnEggItem(Items.TURTLE_SPAWN_EGG, true, setting.spawnEgg(ModEntities.SHREK)) {
        @Override
        public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
            return PolymerResourcePackUtils.hasMainPack(context) ? super.getPolymerItemModel(stack, context, lookup) : null;
        }
    });

    public static final Item APPA_SPAWN_EGG = registerItem("appa_spawn_egg", setting -> new PolymerSpawnEggItem(Items.HAPPY_GHAST_SPAWN_EGG, true, setting.spawnEgg(ModEntities.APPA)) {
        @Override
        public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
            return PolymerResourcePackUtils.hasMainPack(context) ? super.getPolymerItemModel(stack, context, lookup) : null;
        }
    });

    public static final Item SMURF_CAT_HAT = registerItem("smurf_cat_hat", SmurfCatHatItem::new);
    public static final Item SHREK_HORN = registerItem("shrek_horn", ShrekHornItem::new);




    public static final CreativeModeTab ITEM_GROUP = PolymerCreativeModeTabUtils.builder()
            .title(Component.translatable("itemGroup.peculiar_creatures.item_group"))
            .icon(ModItems.SPECTRE_DISC::getDefaultInstance).displayItems((context, entries) -> {
                entries.accept(ModItems.SPECTRE_DISC);
                entries.accept(ModItems.SMURF_CAT_SPAWN_EGG);
                entries.accept(ModItems.SMURF_CAT_HAT);
                entries.accept(ModItems.SHREK_DISC);
                entries.accept(ModItems.SHREK_SPAWN_EGG);
                entries.accept(ModItems.SHREK_HORN);
            }).build();


    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, name)))));
    }
    public static void registerModItems() {
        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, "item_group"), ITEM_GROUP);
        PeculiarCreaturesMod.LOGGER.info("Registering Mod Items for " + PeculiarCreaturesMod.MOD_ID);
    }
}
