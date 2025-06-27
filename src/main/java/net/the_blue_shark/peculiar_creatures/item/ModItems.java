package net.the_blue_shark.peculiar_creatures.item;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;
import net.the_blue_shark.peculiar_creatures.sound.ModSounds;

import java.util.function.Function;

public class ModItems {
    public static final Item SPECTRE_DISC = registerItem("spectre_music_disc", setting -> new SimplePolymerItem(setting/*.jukeboxPlayable(ModSounds.SPECTRE_KEY)*/.maxCount(1)));





    public static final ItemGroup ITEM_GROUP = PolymerItemGroupUtils.builder()
            .displayName(Text.translatable("itemGroup.peculiar_creatures.item_group"))
            .icon(ModItems.SPECTRE_DISC::getDefaultStack).entries((context, entries) -> {
                entries.add(ModItems.SPECTRE_DISC);
            }).build();


    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(PeculiarCreaturesMod.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PeculiarCreaturesMod.MOD_ID, name)))));
    }
    public static void registerModItems() {
        PolymerItemGroupUtils.registerPolymerItemGroup(Identifier.of(PeculiarCreaturesMod.MOD_ID, "item_group"), ITEM_GROUP);
        PeculiarCreaturesMod.LOGGER.info("Registering Mod Items for " + PeculiarCreaturesMod.MOD_ID);
    }
}
