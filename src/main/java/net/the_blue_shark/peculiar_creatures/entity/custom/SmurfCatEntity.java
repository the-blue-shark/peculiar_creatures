package net.the_blue_shark.peculiar_creatures.entity.custom;

import com.mojang.datafixers.util.Pair;
import de.tomalbrc.bil.api.AnimatedEntity;
import de.tomalbrc.bil.api.AnimatedEntityHolder;
import de.tomalbrc.bil.core.holder.entity.EntityHolder;
import de.tomalbrc.bil.core.holder.entity.living.LivingEntityHolder;
import de.tomalbrc.bil.core.model.Model;
import eu.pb4.polymer.core.api.utils.PolymerUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.tracker.DisplayTrackedData;
import eu.pb4.polymer.virtualentity.api.tracker.EntityTrackedData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.the_blue_shark.peculiar_creatures.mixin.ZombieAccessor;
import net.the_blue_shark.peculiar_creatures.util.AnimationHelper;
import net.the_blue_shark.peculiar_creatures.util.Util;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.UUID;

import static net.minecraft.core.component.DataComponents.DYED_COLOR;

public class SmurfCatEntity extends Animal implements AnimatedEntity {
    public static final Identifier ID = Util.id("smurf_cat");
    public static final Model MODEL = Util.loadBbModel(ID);
    private final EntityHolder<SmurfCatEntity> holder;

    public SmurfCatEntity(EntityType<SmurfCatEntity> entityEntityType, Level level) {
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
        };
        EntityAttachment.ofTicking(this.holder, this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0, (stack) -> {
            return stack.is(Items.SWEET_BERRIES);
        }, true));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.TEMPT_RANGE, 12);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 2 == 0) {
            AnimationHelper.updateWalkAnimation(this, this.holder);
            AnimationHelper.updateHurtColor(this, this.holder);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CAT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    @Override
    protected void customServerAiStep(ServerLevel world) {
        super.customServerAiStep(world);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.SWEET_BERRIES);
    }

    @Override
    public AnimatedEntityHolder getHolder() {
        return this.holder;
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext packetContext) {
        if(PolymerResourcePackUtils.hasMainPack(packetContext.getPlayer())) {
            return EntityType.BLOCK_DISPLAY;
        } else {
            return EntityType.ZOMBIE;
        }

    }

    @Override
    public void modifyRawTrackedData(List<SynchedEntityData.DataValue<?>> data, ServerPlayer player, boolean initial) {
        if(PolymerResourcePackUtils.hasMainPack(player)) {
            if (this instanceof Entity entity) {
                data.add(SynchedEntityData.DataValue.create(DisplayTrackedData.WIDTH, entity.getBbWidth()));
                data.add(SynchedEntityData.DataValue.create(DisplayTrackedData.HEIGHT, entity.getBbHeight()));
            }

            data.add(SynchedEntityData.DataValue.create(DisplayTrackedData.SHADOW_RADIUS, this.getShadowRadius()));
            data.add(SynchedEntityData.DataValue.create(DisplayTrackedData.TELEPORTATION_DURATION, Math.max(0, this.getTeleportDuration())));

            data.add(SynchedEntityData.DataValue.create(EntityTrackedData.SILENT, true));
            data.add(SynchedEntityData.DataValue.create(EntityTrackedData.NO_GRAVITY, true));
            data.add(SynchedEntityData.DataValue.create(EntityTrackedData.NAME_VISIBLE, false));
        } else {
            data.add(SynchedEntityData.DataValue.create(ZombieAccessor.getBabyFlag(), true));
        }
    }

    @Override
    public List<Pair<EquipmentSlot, ItemStack>> getPolymerVisibleEquipment(List<Pair<EquipmentSlot, ItemStack>> items, ServerPlayer player) {
        if(!PolymerResourcePackUtils.hasMainPack(player)) {
            ItemStack head = PolymerUtils.createPlayerHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTM2Y2E0ZTA5YmJmYzVhMjFhMGNhZWIzZTUzYjIwMWE4YWJlNWUxNTk3ZjA3MTg0NGUzNjgwMmQ2MGQ0Y2M2OCJ9fX0=");
            ItemStack chest = new ItemStack(Items.LEATHER_CHESTPLATE);
            chest.set(DYED_COLOR, new DyedItemColor(3847130));
            ItemStack leggings = new ItemStack(Items.LEATHER_LEGGINGS);
            leggings.set(DYED_COLOR, new DyedItemColor(16777215));
            ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);
            boots.set(DYED_COLOR, new DyedItemColor(16777215));

            return List.of(
                    new Pair<>(EquipmentSlot.HEAD, head),
                    new Pair<>(EquipmentSlot.CHEST, chest),
                    new Pair<>(EquipmentSlot.LEGS, leggings),
                    new Pair<>(EquipmentSlot.FEET, boots)
            );
        }
        return List.of();
    }

    @Override
    public void modifyRawEntityAttributeData(List<ClientboundUpdateAttributesPacket.AttributeSnapshot> data, ServerPlayer player, boolean initial) {
        if(!PolymerResourcePackUtils.hasMainPack(player)) {
            data.add(new ClientboundUpdateAttributesPacket.AttributeSnapshot(
                    Attributes.SCALE,
                    0.6,
                    List.of()
            ));
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
    

}
