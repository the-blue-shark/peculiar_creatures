package net.the_blue_shark.peculiar_creatures.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.the_blue_shark.peculiar_creatures.entity.goal.FearPanicGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin {

    @Shadow
    protected GoalSelector goalSelector;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void peculiar_creatures$inject(CallbackInfo ci) {
        if ((Object)this instanceof PathfinderMob mob) {
            this.goalSelector.addGoal(0, new FearPanicGoal(mob, 1.4));
        }
    }
}