package net.the_blue_shark.peculiar_creatures.sound;

import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;

import java.util.Optional;

public class ModSounds {

    public static SoundEvent SHREK_ROAR = registerSoundEvent("shrek_roar", SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(5).value());
    public static ResourceKey<JukeboxSong> SPECTRE_KEY = of("spectre");
    public static ResourceKey<JukeboxSong> ALL_STAR_KEY = of("all_star");

    private static ResourceKey<JukeboxSong> of(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, name));
    }
    private static SoundEvent registerSoundEvent(String name, SoundEvent soundEvent) {
        Identifier id = Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, name);
        var event = Registry.register(BuiltInRegistries.SOUND_EVENT, id, new SoundEvent(id, Optional.empty()));
        PolymerSoundEvent.registerOverlay(event, soundEvent, PolymerResourcePackUtils.getMainUuid());
        return event;
    }

    public static void registerSounds() {
        PeculiarCreaturesMod.LOGGER.info("Registering Mod Sounds for " + PeculiarCreaturesMod.MOD_ID);
    }
}
