package net.the_blue_shark.peculiar_creatures.effect;

import eu.pb4.polymer.core.api.other.PolymerMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class FearEffect extends MobEffect implements PolymerMobEffect {
    protected FearEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }
}
