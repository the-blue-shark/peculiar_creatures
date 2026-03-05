package net.the_blue_shark.peculiar_creatures.entity.custom;

import de.tomalbrc.bil.api.AnimatedEntity;
import de.tomalbrc.bil.api.AnimatedEntityHolder;
import de.tomalbrc.bil.core.holder.entity.EntityHolder;
import de.tomalbrc.bil.core.holder.entity.living.LivingEntityHolder;
import de.tomalbrc.bil.core.model.Model;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.level.Level;
import net.the_blue_shark.peculiar_creatures.util.Util;

public class AppaEntity extends HappyGhast implements AnimatedEntity {
    public static final Identifier ID = Util.id("shrek");
    public static final Model MODEL = Util.loadBbModel(ID);
    private final EntityHolder<AppaEntity> holder;

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
    }

    @Override
    public AnimatedEntityHolder getHolder() {
        return null;
    }
}
