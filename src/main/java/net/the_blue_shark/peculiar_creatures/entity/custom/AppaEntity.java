package net.the_blue_shark.peculiar_creatures.entity.custom;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.math.Axis;
import de.tomalbrc.bil.BIL;
import de.tomalbrc.bil.api.AnimatedEntity;
import de.tomalbrc.bil.api.AnimatedEntityHolder;
import de.tomalbrc.bil.core.holder.entity.EntityHolder;
import de.tomalbrc.bil.core.holder.entity.living.LivingEntityHolder;
import de.tomalbrc.bil.core.model.Model;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.data.DisplayEntityData;
import eu.pb4.polymer.virtualentity.api.data.EntityData;
import eu.pb4.polymer.virtualentity.api.elements.GenericEntityElement;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.the_blue_shark.peculiar_creatures.util.AnimationHelper;
import net.the_blue_shark.peculiar_creatures.util.Util;
import org.joml.Matrix4x3fStack;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

import java.util.List;
import java.util.Optional;

public class AppaEntity extends HappyGhast implements AnimatedEntity {
    public static final Identifier ID = Util.id("appa");
    public static final Model MODEL = Util.loadModel(ID);
    private final EntityHolder<AppaEntity> holder;
    private final GenericEntityElement HappyGhast = new GenericEntityElement() {
        @Override
        protected EntityType<? extends Entity> getEntityType() {
            return EntityTypes.HAPPY_GHAST;
        }
    };

    public AppaEntity(EntityType<? extends HappyGhast> entityType, Level level) {
        super(entityType, level);
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
        this.holder.addElement(HappyGhast);
        EntityAttachment.ofTicking(this.holder, this);

    }

    @Override
    public AnimatedEntityHolder getHolder() {
        return this.holder;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 2 == 0) {
            AnimationHelper.updateWalkAnimation(this, this.holder);
            AnimationHelper.updateHurtColor(this, this.holder);
        }
        this.HappyGhast.setRotation(this.getXRot(), this.getYRot());
    }



    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        if(PolymerResourcePackUtils.hasMainPack(context)) {
            return EntityTypes.BLOCK_DISPLAY;
        } else {
            return EntityTypes.HAPPY_GHAST;
        }// does not conflict with Happy ghast element on the virtual entity
    }

    @Override
    public void modifyRawTrackedData(List<SynchedEntityData.DataValue<?>> data, ServerPlayer player, boolean initial) {
        if(PolymerResourcePackUtils.hasMainPack(player)) {
            if (this instanceof Entity entity) {
                data.add(SynchedEntityData.DataValue.create(DisplayEntityData.WIDTH, entity.getBbWidth()));
                data.add(SynchedEntityData.DataValue.create(DisplayEntityData.HEIGHT, entity.getBbHeight()));
            }

            data.add(SynchedEntityData.DataValue.create(DisplayEntityData.SHADOW_RADIUS, this.getShadowRadius()));
            data.add(SynchedEntityData.DataValue.create(DisplayEntityData.TELEPORTATION_DURATION, Math.max(0, this.getTeleportDuration())));

            data.add(SynchedEntityData.DataValue.create(EntityData.SILENT, true));
            data.add(SynchedEntityData.DataValue.create(EntityData.NO_GRAVITY, true));
            data.add(SynchedEntityData.DataValue.create(EntityData.NAME_VISIBLE, false));
        } else {
            data.add(SynchedEntityData.DataValue.create(
                    EntityData.CUSTOM_NAME,
                    Optional.of(Component.translatable("entity.peculiar_creatures.appa"))
            ));
            data.add(SynchedEntityData.DataValue.create(
                    EntityData.SILENT,
                    true
            ));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, (double)30.0F)
                .add(Attributes.TEMPT_RANGE, (double)16.0F)
                .add(Attributes.FLYING_SPEED, 0.06)
                .add(Attributes.MOVEMENT_SPEED, 0.06)
                .add(Attributes.FOLLOW_RANGE, (double)16.0F)
                .add(Attributes.CAMERA_DISTANCE, (double)9.0F);
    }

}
