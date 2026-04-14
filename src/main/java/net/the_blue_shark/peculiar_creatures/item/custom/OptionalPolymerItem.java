package net.the_blue_shark.peculiar_creatures.item.custom;

import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class OptionalPolymerItem extends SimplePolymerItem {

    public OptionalPolymerItem(Item.Properties settings) {
        this(settings, Items.TRIAL_KEY, true);
    }

    public OptionalPolymerItem(Item.Properties settings, Item polymerItem) {
        this(settings, polymerItem, false);
    }

    public OptionalPolymerItem(Item.Properties settings, Item polymerItem, boolean useModel) {
        super(settings, polymerItem, useModel);
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return PolymerResourcePackUtils.hasMainPack(context) ? super.getPolymerItemModel(stack, context, lookup) : null;
    }
}
