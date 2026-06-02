package net.the_blue_shark.peculiar_creatures.mixin;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;
import work.lclpnet.notica.Notica;
import work.lclpnet.notica.api.CheckedSong;
import work.lclpnet.notica.api.PlaybackOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.the_blue_shark.peculiar_creatures.item.custom.PolymerMusicDisc;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.notica.api.SongHandle;
import work.lclpnet.notica.api.Speaker;
import work.lclpnet.notica.util.ServerSongLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Mixin(JukeboxSongPlayer.class)
public class JukeboxSongPlayerMixin {



    @Unique
    private SongHandle peculiar_creatures$currentHandle;

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void peculiar_creatures$playCustomSong(LevelAccessor level, Holder<JukeboxSong> song, CallbackInfo ci) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        JukeboxSongPlayerAccessor accessor = (JukeboxSongPlayerAccessor) this;
        BlockPos pos = accessor.getBlockPos();
        if (!(level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox)) {
            return;
        }
        ItemStack stack = jukebox.getTheItem();
        if (!(stack.getItem() instanceof PolymerMusicDisc disc)) {
            return;
        }

        Notica api = Notica.getInstance(serverLevel.getServer());
        Identifier id = Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, disc.getSongPath());
        CheckedSong checkedSong;

        try (InputStream in = serverLevel.getServer().getResourceManager().open(id)) {
            checkedSong = ServerSongLoader.load(in, id);
        } catch (IOException e) {
            //I dont want to
            return;
        }

        Speaker speaker = Speaker.fixed(Vec3.atCenterOf(pos), serverLevel);
        this.peculiar_creatures$currentHandle = api.playSongWithSpeaker(checkedSong, new PlaybackOptions(1.0f), 0, speaker, List.of());

        accessor.setSong(song);
        accessor.setTicksSinceSongStarted(0L);

        int songId = level.registryAccess().lookupOrThrow(Registries.JUKEBOX_SONG).getId(song.value());
        level.levelEvent(null, 1010, pos, songId);
        accessor.getOnSongChanged().notifyChange();

        ci.cancel();
    }

    @Inject(method = "stop", at = @At("HEAD"))
    private void peculiar_creatures$stopCustomSong(LevelAccessor level, @Nullable BlockState state, CallbackInfo ci) {
        if (this.peculiar_creatures$currentHandle != null) {
            this.peculiar_creatures$currentHandle.stop();
            this.peculiar_creatures$currentHandle = null;
        }
    }
}
