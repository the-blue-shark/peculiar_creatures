package net.the_blue_shark.peculiar_creatures.sound;

import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;

public class ModSounds {
    public static final PolymerSoundEvent SPECTRE = PolymerSoundEvent.of(registerSoundEvent("spectre"));
    public static final RegistryKey<JukeboxSong> SPECTRE_KEY =
            RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(PeculiarCreaturesMod.MOD_ID, "spectre"));

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(PeculiarCreaturesMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        PeculiarCreaturesMod.LOGGER.info("Registering Mod Sounds for " + PeculiarCreaturesMod.MOD_ID);
    }
}
