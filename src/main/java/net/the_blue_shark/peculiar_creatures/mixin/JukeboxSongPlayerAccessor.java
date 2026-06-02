package net.the_blue_shark.peculiar_creatures.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JukeboxSongPlayer.class)
public interface JukeboxSongPlayerAccessor {
    @Accessor("blockPos")
    BlockPos getBlockPos();

    @Accessor("song")
    void setSong(Holder<JukeboxSong> song);

    @Accessor("ticksSinceSongStarted")
    void setTicksSinceSongStarted(long ticks);

    @Accessor("onSongChanged")
    JukeboxSongPlayer.OnSongChanged getOnSongChanged();
}
