package net.the_blue_shark.peculiar_creatures.item.custom;

import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PolymerMusicDisc extends SimplePolymerItem {
    private final String noticaPath;

    public PolymerMusicDisc(Item.Properties settings, ResourceKey<JukeboxSong> song, String noticaPath) {
        this(settings, Items.TRIAL_KEY, true, song, noticaPath);
    }

    public PolymerMusicDisc(Item.Properties settings, Item polymerItem, ResourceKey<JukeboxSong> song, String noticaPath) {
        this(settings, polymerItem, false, song, noticaPath);
    }

    public PolymerMusicDisc(Item.Properties settings, Item polymerItem, boolean useModel, ResourceKey<JukeboxSong> song, String noticaPath) {
        super(settings.stacksTo(1).jukeboxPlayable(song), polymerItem, useModel);
        this.noticaPath = noticaPath;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return PolymerResourcePackUtils.hasMainPack(context) ? super.getPolymerItemModel(stack, context, lookup) : null;
    }

    public String getSongPath() {
        return "songs/" + this.noticaPath + ".nbs";
    }

}
