package net.the_blue_shark.peculiar_creatures.entity.goal;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.the_blue_shark.peculiar_creatures.effect.ModEffects;

import java.util.EnumSet;

public class FearPanicGoal extends PanicGoal {

    public FearPanicGoal(PathfinderMob mob, double speed) {
        super(mob, speed);
    }

    @Override
    protected boolean shouldPanic() {
        return this.mob.hasEffect(ModEffects.FEAR);
    }
}
