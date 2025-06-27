package net.the_blue_shark.peculiar_creatures.sound;

import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;

import java.util.Optional;

public class ModSounds {

    public static final SoundEvent SPECTRE = registerSoundEvent("spectre", SoundEvents.ENTITY_WARDEN_DEATH);
    /*public static final RegistryKey<JukeboxSong> SPECTRE_KEY = of("spectre");


    private static RegistryKey<JukeboxSong> of(String name) {
        return RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(PeculiarCreaturesMod.MOD_ID, name));
    }*/
    private static SoundEvent registerSoundEvent(String name, SoundEvent soundEvent) {
        Identifier id = Identifier.of(PeculiarCreaturesMod.MOD_ID, name);
        var event = Registry.register(Registries.SOUND_EVENT, id, new SoundEvent(id, Optional.empty()));
        PolymerSoundEvent.registerOverlay(event, soundEvent);
        RegistrySyncUtils.setServerEntry(Registries.SOUND_EVENT, event);
        return event;
    }
    private static SoundEvent registerSoundEvent(String name, RegistryEntry<SoundEvent> reference) {
        return registerSoundEvent(name, reference.value());
    }

    public static void registerSounds() {
        PeculiarCreaturesMod.LOGGER.info("Registering Mod Sounds for " + PeculiarCreaturesMod.MOD_ID);
    }
}
