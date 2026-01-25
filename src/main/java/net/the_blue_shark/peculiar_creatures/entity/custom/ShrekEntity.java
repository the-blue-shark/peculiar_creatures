package net.the_blue_shark.peculiar_creatures.entity.custom;

import de.tomalbrc.bil.api.AnimatedEntity;
import de.tomalbrc.bil.core.holder.entity.EntityHolder;
import de.tomalbrc.bil.core.holder.entity.living.LivingEntityHolder;
import de.tomalbrc.bil.core.model.Model;
import de.tomalbrc.bil.file.loader.BbModelLoader;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.tracker.EntityTrackedData;
import eu.pb4.polymer.virtualentity.mixin.accessors.EntityAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.the_blue_shark.peculiar_creatures.mixin.ZombieAccessor;
import net.the_blue_shark.peculiar_creatures.util.AnimationHelper;
import net.the_blue_shark.peculiar_creatures.util.Util;
import org.jspecify.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.Optional;

public class ShrekEntity extends PathfinderMob implements /*PolymerEntity,*/ NeutralMob, AnimatedEntity {
    public static final Identifier ID = Util.id("shrek");
    public static final Model MODEL = Util.loadBbModel(ID);
    private final EntityHolder<ShrekEntity> holder;
    private final EntityAttachment attachment;


    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private long persistentAngerEndTime;
    @Nullable
    private EntityReference<LivingEntity> persistentAngerTarget;
    @Nullable
    private LivingEntity target;

    @Override
    public EntityHolder<ShrekEntity> getHolder() {
        return this.holder;
    }

    public ShrekEntity(EntityType<ShrekEntity> entityEntityType, Level level) {
        super(entityEntityType, level);
        this.holder = new LivingEntityHolder<>(this, MODEL) {
            @Override
            public boolean startWatching(ServerGamePacketListenerImpl handler) {
                ServerPlayer player = handler.player;
                if (PolymerResourcePackUtils.hasMainPack(player)) {
                    return super.startWatching(handler);
                }
                return false;
            }
        }; // create a holder for living entities, for head rotations and other features
        this.attachment = new EntityAttachment(this.holder, this, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.ATTACK_DAMAGE, 30.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ResetUniversalAngerTargetGoal<>(this, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
    }


    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        if(PolymerResourcePackUtils.hasMainPack(context.getPlayer())) {
            return EntityType.ARMOR_STAND;
        } else {
            return EntityType.WARDEN;
        }
    }

    @Override
    public void modifyRawTrackedData(List<SynchedEntityData.DataValue<?>> data, ServerPlayer player, boolean initial) {
        if(PolymerResourcePackUtils.hasMainPack(player)) {
            data.add(SynchedEntityData.DataValue.create(EntityTrackedData.FLAGS, (byte) (1 << EntityTrackedData.INVISIBLE_FLAG_INDEX)));
            data.add(SynchedEntityData.DataValue.create(ArmorStand.DATA_CLIENT_FLAGS, (byte) (ArmorStand.CLIENT_FLAG_SMALL | ArmorStand.CLIENT_FLAG_MARKER)));
            data.add(new SynchedEntityData.DataValue<>(EntityAccessor.getDATA_NO_GRAVITY().id(), EntityAccessor.getDATA_NO_GRAVITY().serializer(), true));
        } else {
            data.add(SynchedEntityData.DataValue.create(
                    EntityTrackedData.CUSTOM_NAME,
                    Optional.of(Component.literal("Shrek"))
            ));
            data.add(SynchedEntityData.DataValue.create(
                    EntityTrackedData.SILENT,
                    true
            ));
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount % 2 == 0) {
            AnimationHelper.updateWalkAnimation(this, this.holder); // util methods, see below
            AnimationHelper.updateHurtVariant(this, this.holder); // util methods
            AnimationHelper.updateHurtColor(this, this.holder);
        }
        this.holder.tick();
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerEndTime(long time) {
        this.persistentAngerEndTime = time;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public @Nullable LivingEntity getLastHurtByMob() {
        return null;
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity livingEntity) {

    }
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        boolean result = super.hurtServer(level, source, amount);

        if (result) {
            Entity attacker = source.getEntity();
            if (attacker instanceof LivingEntity living) {
                this.setTarget(living);
                this.setPersistentAngerTarget(EntityReference.of(living));
                this.startPersistentAngerTimer();
            }
        }

        return result;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        this.addPersistentAngerSaveData(valueOutput);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.readPersistentAngerSaveData(this.level(), valueInput);
    }

    /*@Override
    public boolean canAttack(LivingEntity target) {
        return this.isAngryAt(target);
    }*/


    @Override
    public void setTarget(@Nullable LivingEntity target) {
        this.target = target;
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.target;
    }

}
