package net.the_blue_shark.peculiar_creatures.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;

public class ModEffects {
    public static final Holder<MobEffect> FEAR = registerMobEffect("fear",
            new FearEffect(MobEffectCategory.NEUTRAL, 0x5f9609));


    private static Holder<MobEffect> registerMobEffect(String name, MobEffect effect) {
        return Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT,
                Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, name),
                effect
        );
    }

    public static void registerEffects() {
        PeculiarCreaturesMod.LOGGER.info("Registering Mod Effects for " + PeculiarCreaturesMod.MOD_ID);
    }
}
