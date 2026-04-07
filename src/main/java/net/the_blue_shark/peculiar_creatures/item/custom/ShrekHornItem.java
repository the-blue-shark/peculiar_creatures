package net.the_blue_shark.peculiar_creatures.item.custom;

import java.util.List;

import eu.pb4.polymer.common.api.PolymerCommonUtils;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.the_blue_shark.peculiar_creatures.effect.ModEffects;
import net.the_blue_shark.peculiar_creatures.entity.custom.ShrekEntity;
import net.the_blue_shark.peculiar_creatures.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

public class ShrekHornItem extends Item implements PolymerItem {

    public ShrekHornItem(Properties settings) {
        super(settings.stacksTo(1));
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof Player playerEntity)) {
            return false;
        }
        ItemStack itemStack = playerEntity.getProjectile(stack);
        if (itemStack.isEmpty()) {
            return false;
        }
        if ((double) (BowItem.getPowerForTime(this.getUseDuration(stack, user) - remainingUseTicks)) < 0.1) {
            return false;
        }
        playerEntity.awardStat(Stats.ITEM_USED.get(this));
        playerEntity.getCooldowns().addCooldown(stack, 400);
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 60;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.SPYGLASS;
    }

    private List<LivingEntity> getTargets(Player user) {
        return user.level().getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(30), entity -> entity instanceof PathfinderMob && !(entity instanceof ShrekEntity));
    }

    private void fear(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(ModEffects.FEAR, 300, 0));
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        world.playSound(null, user.getX(), user.getEyeY(), user.getZ(), ModSounds.SHREK_ROAR, user.getSoundSource(), 30.0f, 1.0f);
        ItemStack itemStack = user.getItemInHand(hand);
        getTargets(user).forEach(this::fear);
        user.startUsingItem(hand);
        user.getCooldowns().addCooldown(itemStack, 400);
        return InteractionResult.CONSUME;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.GOAT_HORN;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return PolymerResourcePackUtils.hasMainPack(context)
                ? PolymerItem.super.getPolymerItemModel(stack, context, lookup)
                : null;
    }

    @Override
    public boolean handleMiningOnServer(ItemStack tool, BlockState targetBlock, BlockPos pos, ServerPlayer player) {
        return false;
    }
}
