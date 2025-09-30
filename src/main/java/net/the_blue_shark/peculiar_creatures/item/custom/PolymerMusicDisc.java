package net.the_blue_shark.peculiar_creatures.item.custom;

import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class PolymerMusicDisc extends SimplePolymerItem {

    public PolymerMusicDisc(Item.Settings settings, RegistryKey<JukeboxSong> song) {
        this(settings, Items.TRIAL_KEY, true, song);
    }

    public PolymerMusicDisc(Item.Settings settings, Item polymerItem, RegistryKey<JukeboxSong> song) {
        this(settings, polymerItem, false, song);
    }

    public PolymerMusicDisc(Item.Settings settings, Item polymerItem, boolean useModel, RegistryKey<JukeboxSong> song) {
        super(settings.maxCount(1).jukeboxPlayable(song), polymerItem, useModel);
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return PolymerResourcePackUtils.hasMainPack(context) ? super.getPolymerItemModel(stack, context) : null;
    }

    @Override
    public void appendTooltip(
            ItemStack stack,
            Item.TooltipContext context,
            TooltipDisplayComponent displayComponent,
            java.util.function.Consumer<Text> textConsumer,
            TooltipType type
    ) {
        if(!PolymerResourcePackUtils.isRequired()) {
            textConsumer.accept(Text.translatable("item.peculiar_creatures.music_disc.tooltip").formatted(Formatting.RED));
        }
    }

}
