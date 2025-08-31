package net.the_blue_shark.peculiar_creatures.entity.custom;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.common.impl.CompatStatus;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.utils.PolymerUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.VirtualEntityUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import eu.pb4.polymer.virtualentity.api.elements.MobAnchorElement;
import eu.pb4.polymer.virtualentity.api.tracker.EntityTrackedData;
import eu.pb4.polymer.virtualentity.mixin.accessors.EntityAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.the_blue_shark.peculiar_creatures.entity.ModEntities;
import net.the_blue_shark.peculiar_creatures.mixin.ZombieEntityAccessor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4x3fStack;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;

public class SmurfCatEntity extends AnimalEntity implements PolymerEntity {
    private static final UUID SIZE_MODIFIER_UUID = UUID.fromString("6f7d6b0c-dc69-4c3e-a2c4-8b2d2d2e2b2b");

    private final ElementHolder holder;
    private final EntityAttachment attachment;
    private final ItemDisplayElement leftLeg = new ItemDisplayElement(Items.RED_CONCRETE);
    private final ItemDisplayElement rightLeg = new ItemDisplayElement(Items.RED_CONCRETE);
    private final ItemDisplayElement torso = new ItemDisplayElement(PolymerUtils.createPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjYyYzQ4NWIxODg2ZGJjZTZjMWNhZDE0MGMwZWY4NzYzNTU5ZDQzYTc4NTY0NDY2NGM2ZDVmMzZlMjc1NGVlOCJ9fX0="));
    private final InteractionElement interaction = InteractionElement.redirect(this);
    private final MobAnchorElement rideAnchor = new MobAnchorElement();

    private Matrix4x3fStack stack = new Matrix4x3fStack(8);
    private float previousSpeed = Float.MIN_NORMAL;
    private float previousLimbPos = Float.MIN_NORMAL;
    private float deathAngle;

    public SmurfCatEntity(EntityType<SmurfCatEntity> entityEntityType, World world) {
        super(entityEntityType, world);
        this.holder = new ElementHolder() {
            @Override
            protected void notifyElementsOfPositionUpdate(Vec3d newPos, Vec3d delta) {
                SmurfCatEntity.this.rideAnchor.notifyMove(this.currentPos, newPos, delta);
            }
            @Override
            public boolean startWatching(ServerPlayNetworkHandler handler) {
                ServerPlayerEntity player = handler.player;
                if (PolymerResourcePackUtils.hasMainPack(player)) {
                    return super.startWatching(handler);
                }
                return false;
            }

            @Override
            public Vec3d getPos() {
                return this.getAttachment().getPos();
            }
        };
        this.rideAnchor.setOffset(new Vec3d(0, 1.3f, 0));

        leftLeg.setInterpolationDuration(2);
        leftLeg.ignorePositionUpdates();
        rightLeg.setInterpolationDuration(2);
        rightLeg.ignorePositionUpdates();
        torso.setInterpolationDuration(2);
        torso.ignorePositionUpdates();
        leftLeg.setItemDisplayContext(ItemDisplayContext.FIXED);
        rightLeg.setItemDisplayContext(ItemDisplayContext.FIXED);
        torso.setItemDisplayContext(ItemDisplayContext.FIXED);
        this.interaction.setSize(0f, 0f);
        this.interaction.ignorePositionUpdates();
        this.rideAnchor.ignorePositionUpdates();
        this.updateAnimation();

        this.holder.addPassengerElement(interaction);
        this.holder.addPassengerElement(leftLeg);
        this.holder.addPassengerElement(rightLeg);
        this.holder.addPassengerElement(torso);
        this.holder.addElement(rideAnchor);
        this.attachment = new EntityAttachment(this.holder, this, false);
    }
    private void updateAnimation() {
        var speed = this.limbAnimator.getSpeed();
        var limbPos = this.limbAnimator.getAnimationProgress();
        float f = ((float)this.deathTime) / 20.0F * 1.6F;
        f = MathHelper.sqrt(f);
        if (f > 1.0F) {
            f = 1.0F;
        }
        if (this.deathAngle == f && speed == this.previousSpeed && limbPos == this.previousLimbPos) {
            return;
        }

        this.deathAngle = f;
        this.previousSpeed = speed;
        this.previousLimbPos = limbPos;

        this.leftLeg.startInterpolation();
        this.rightLeg.startInterpolation();
        this.torso.startInterpolation();

        stack.clear();
        stack.translate(0, -0.2f, 0);
        stack.rotateY((float) Math.toRadians(- MathHelper.lerpAngleDegrees(0.5f, this.lastYaw, this.getYaw())) + (float) (0.00001f * Math.random()));
        if (this.deathTime > 0) {
            stack.rotate(RotationAxis.POSITIVE_Z.rotation(f * MathHelper.HALF_PI));
        }
        stack.scale(2);
        stack.pushMatrix();

        stack.translate(0, 0.5f, 0);
        torso.setTransformation(stack);

        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(0.15f, 0.4f, 0).rotateX(MathHelper.cos(limbPos * 0.6662F) * 1.4F * speed).translate(0, -0.125f, 0).scale(0.5f, 0.8f, 0.5f);
        leftLeg.setTransformation(stack);
        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(-0.15f, 0.4f, 0).rotateX(MathHelper.cos(limbPos * 0.6662F + 3.1415927F) * 1.4F * speed).translate(0, -0.125f, 0).scale(0.5f, 0.8f, 0.5f);
        rightLeg.setTransformation(stack);
        stack.popMatrix();
    }



    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(2, new TemptGoal(this, 1.0, (stack) -> {
            return stack.isOf(Items.SWEET_BERRIES);
        }, true));
        this.goalSelector.add(3, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 10)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.ATTACK_DAMAGE, 1)
                .add(EntityAttributes.FOLLOW_RANGE, 20)
                .add(EntityAttributes.TEMPT_RANGE, 12);
    }

    @Override
    public void tick() {
        super.tick();

        this.updateLimbs(false);
        this.updateAnimation();

        this.holder.tick();
    }

    /* SOUNDS */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CAT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    @Override
    protected void mobTick(ServerWorld world) {
        super.mobTick(world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.BEDROCK);
    }

    @Nullable

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.SMURF_CAT.create(world, SpawnReason.BREEDING);
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext packetContext) {
        if(PolymerResourcePackUtils.hasMainPack(packetContext.getPlayer())) {
            return EntityType.ARMOR_STAND;
        } else {
            return EntityType.ZOMBIE;
        }

    }

    @Override
    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
        if(PolymerResourcePackUtils.hasMainPack(player)) {
            data.add(DataTracker.SerializedEntry.of(EntityTrackedData.FLAGS, (byte) (1 << EntityTrackedData.INVISIBLE_FLAG_INDEX)));
            data.add(DataTracker.SerializedEntry.of(ArmorStandEntity.ARMOR_STAND_FLAGS, (byte) (ArmorStandEntity.SMALL_FLAG | ArmorStandEntity.MARKER_FLAG)));
            data.add(new DataTracker.SerializedEntry<>(EntityAccessor.getNO_GRAVITY().id(), EntityAccessor.getNO_GRAVITY().dataType(), true));
        } else {
            data.add(DataTracker.SerializedEntry.of(ZombieEntityAccessor.getBabyFlag(), true));
        }
    }

    @Override
    public List<Pair<EquipmentSlot, ItemStack>> getPolymerVisibleEquipment(List<Pair<EquipmentSlot, ItemStack>> items, ServerPlayerEntity player) {
        if(!PolymerResourcePackUtils.hasMainPack(player)) {
            return List.of(new Pair<>(EquipmentSlot.HEAD, PolymerUtils.createPlayerHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTM2Y2E0ZTA5YmJmYzVhMjFhMGNhZWIzZTUzYjIwMWE4YWJlNWUxNTk3ZjA3MTg0NGUzNjgwMmQ2MGQ0Y2M2OCJ9fX0=")));
        } else {
            return List.of();
        }
    }

    @Override
    public void modifyRawEntityAttributeData(List<EntityAttributesS2CPacket.Entry> data, ServerPlayerEntity player, boolean initial) {
        if(!PolymerResourcePackUtils.hasMainPack(player)) {
            data.add(new EntityAttributesS2CPacket.Entry(
                    EntityAttributes.SCALE,
                    0.6,
                    List.of()
            ));
        }
    }

}
