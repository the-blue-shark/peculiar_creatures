package net.the_blue_shark.peculiar_creatures.entity.custom;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class ShrekEntity extends Mob implements PolymerEntity, NeutralMob {
    public ShrekEntity(EntityType<ShrekEntity> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.WARDEN;
    }

    @Override
    public long getPersistentAngerEndTime() {
        return 0;
    }

    @Override
    public void setPersistentAngerEndTime(long l) {

    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> entityReference) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    @Override
    public Level level() {
        return null;
    }

    @Override
    public @Nullable LivingEntity getLastHurtByMob() {
        return null;
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity livingEntity) {

    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {

    }

    @Override
    public boolean canAttack(LivingEntity livingEntity) {
        return false;
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return null;
    }
}
