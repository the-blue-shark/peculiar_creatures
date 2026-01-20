package net.the_blue_shark.peculiar_creatures.entity.custom;

import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.utils.PolymerUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import eu.pb4.polymer.virtualentity.api.elements.MobAnchorElement;
import eu.pb4.polymer.virtualentity.api.tracker.EntityTrackedData;
import eu.pb4.polymer.virtualentity.mixin.accessors.EntityAccessor;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.the_blue_shark.peculiar_creatures.entity.ModEntities;
import net.the_blue_shark.peculiar_creatures.mixin.ZombieAccessor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4x3fStack;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.UUID;

import static net.minecraft.core.component.DataComponents.DYED_COLOR;

public class SmurfCatEntity extends Animal implements PolymerEntity {
    private final ElementHolder holder;
    private final EntityAttachment attachment;
    private final ItemDisplayElement head = new ItemDisplayElement(createModelItem("smurf_cat_head"));
    private final ItemDisplayElement chest = new ItemDisplayElement(createModelItem("smurf_cat_chest"));
    private final ItemDisplayElement rightLeg = new ItemDisplayElement(createModelItem("smurf_cat_leg_right"));
    private final ItemDisplayElement leftLeg = new ItemDisplayElement(createModelItem("smurf_cat_leg_left"));
    private final ItemDisplayElement rightArm = new ItemDisplayElement(createModelItem("smurf_cat_arm_right"));
    private final ItemDisplayElement leftArm = new ItemDisplayElement(createModelItem("smurf_cat_arm_left"));
    private final InteractionElement interaction = InteractionElement.redirect(this);
    private final MobAnchorElement rideAnchor = new MobAnchorElement();

    private Matrix4x3fStack stack = new Matrix4x3fStack(8);
    private float previousSpeed = Float.MIN_NORMAL;
    private float previousLimbPos = Float.MIN_NORMAL;
    private float deathAngle;

    public SmurfCatEntity(EntityType<SmurfCatEntity> entityEntityType, Level world) {
        super(entityEntityType, world);
        this.holder = new ElementHolder() {
            @Override
            protected void notifyElementsOfPositionUpdate(Vec3 newPos, Vec3 delta) {
                SmurfCatEntity.this.rideAnchor.notifyMove(this.currentPos, newPos, delta);
            }
            @Override
            public boolean startWatching(ServerGamePacketListenerImpl handler) {
                ServerPlayer player = handler.player;
                if (PolymerResourcePackUtils.hasMainPack(player)) {
                    return super.startWatching(handler);
                }
                return false;
            }

            @Override
            public Vec3 getPos() {
                return this.getAttachment().getPos();
            }
        };

        leftLeg.setInterpolationDuration(2);
        leftLeg.ignorePositionUpdates();
        rightLeg.setInterpolationDuration(2);
        rightLeg.ignorePositionUpdates();
        leftArm.setInterpolationDuration(2);
        leftArm.ignorePositionUpdates();
        rightArm.setInterpolationDuration(2);
        rightArm.ignorePositionUpdates();
        chest.setInterpolationDuration(2);
        chest.ignorePositionUpdates();
        head.setInterpolationDuration(2);
        head.ignorePositionUpdates();
        leftLeg.setItemDisplayContext(ItemDisplayContext.FIXED);
        rightLeg.setItemDisplayContext(ItemDisplayContext.FIXED);
        leftArm.setItemDisplayContext(ItemDisplayContext.FIXED);
        rightArm.setItemDisplayContext(ItemDisplayContext.FIXED);
        chest.setItemDisplayContext(ItemDisplayContext.FIXED);
        head.setItemDisplayContext(ItemDisplayContext.FIXED);
        this.interaction.setSize(0.3f, 0.6f);
        this.interaction.ignorePositionUpdates();
        this.rideAnchor.ignorePositionUpdates();
        this.updateAnimation();

        this.holder.addPassengerElement(interaction);
        this.holder.addPassengerElement(leftLeg);
        this.holder.addPassengerElement(rightLeg);
        this.holder.addPassengerElement(leftArm);
        this.holder.addPassengerElement(rightArm);
        this.holder.addPassengerElement(chest);
        this.holder.addPassengerElement(head);
        this.holder.addElement(rideAnchor);
        this.attachment = new EntityAttachment(this.holder, this, false);
    }
    private void updateAnimation() {
        var speed = this.walkAnimation.speed();
        var limbPos = this.walkAnimation.position();
        float f = ((float)this.deathTime) / 20.0F * 1.6F;
        f = Mth.sqrt(f);
        if (f > 1.0F) {
            f = 1.0F;
        }
        if (this.deathAngle == f && speed == this.previousSpeed && limbPos == this.previousLimbPos) {
            return;
        }

        this.interaction.setOnFire(this.isOnFire());

        this.deathAngle = f;
        this.previousSpeed = speed;
        this.previousLimbPos = limbPos;

        this.leftLeg.startInterpolation();
        this.rightLeg.startInterpolation();
        this.leftArm.startInterpolation();
        this.rightArm.startInterpolation();
        this.chest.startInterpolation();
        this.head.startInterpolation();
        stack.clear();

        stack.rotateY((float) Math.toRadians(- Mth.rotLerp(0.5f, this.yRotO, this.getYRot())) + (float) (0.00001f * Math.random()));
        if (this.deathTime > 0) {
            stack.rotate(Axis.ZP.rotation(f * Mth.HALF_PI));
        }

        stack.pushMatrix();
        stack.translate(0f, 0.5625f, 0f);
        stack.rotateY((float)Math.toRadians(this.getYHeadRot() - this.getYRot()));
        stack.rotateX((float)Math.toRadians(this.getXRot()));
        head.setTransformation(stack);
        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(0, 0.6875f, 0);
        chest.setTransformation(stack);
        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(0.0390625f, 0.5f, 0.03125f).rotateX(Mth.cos(limbPos * 0.6662F) * 0.7F * speed);
        leftLeg.setTransformation(stack);
        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(-0.0390625f,0.5f, 0.03125f).rotateX(Mth.cos(limbPos * 0.6662F + 3.1415927F) * 0.7F * speed);
        rightLeg.setTransformation(stack);
        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(0.171875f,0.1875f, 0f).rotateX(Mth.cos(limbPos * 0.6662F) * 0.7F * speed);
        leftArm.setTransformation(stack);
        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(-0.171875f,0.1875f, 0f).rotateX(Mth.cos(limbPos * 0.6662F + 3.1415927F) * 0.7F * speed);
        rightArm.setTransformation(stack);
        stack.popMatrix();
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

        this.calculateEntityAnimation(false);
        this.updateAnimation();

        this.holder.tick();
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
    public EntityType<?> getPolymerEntityType(PacketContext packetContext) {
        if(PolymerResourcePackUtils.hasMainPack(packetContext.getPlayer())) {
            return EntityType.ARMOR_STAND;
        } else {
            return EntityType.ZOMBIE;
        }

    }

    @Override
    public void modifyRawTrackedData(List<SynchedEntityData.DataValue<?>> data, ServerPlayer player, boolean initial) {
        if(PolymerResourcePackUtils.hasMainPack(player)) {
            data.add(SynchedEntityData.DataValue.create(EntityTrackedData.FLAGS, (byte) (1 << EntityTrackedData.INVISIBLE_FLAG_INDEX)));
            data.add(SynchedEntityData.DataValue.create(ArmorStand.DATA_CLIENT_FLAGS, (byte) (ArmorStand.CLIENT_FLAG_SMALL | ArmorStand.CLIENT_FLAG_MARKER)));
            data.add(new SynchedEntityData.DataValue<>(EntityAccessor.getNO_GRAVITY().id(), EntityAccessor.getNO_GRAVITY().serializer(), true));
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

    private ItemStack createModelItem(String modelKey) { ItemStack stack = new ItemStack(Items.WHITE_DYE);
        List<String> keys = List.of(modelKey);
        CustomModelData cmd = new CustomModelData(
                List.of(),
                List.of(),
                keys,
                List.of());
        stack.set(DataComponents.CUSTOM_MODEL_DATA, cmd);
        return stack; }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
