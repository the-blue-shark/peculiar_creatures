package net.the_blue_shark.peculiar_creatures.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Zombie.class)
public interface ZombieAccessor {
    @Accessor("DATA_BABY_ID")
    static EntityDataAccessor<Boolean> getBabyFlag() {
        throw new AssertionError();
    }
}
