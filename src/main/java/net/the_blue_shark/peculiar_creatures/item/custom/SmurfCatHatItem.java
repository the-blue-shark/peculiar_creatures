package net.the_blue_shark.peculiar_creatures.item.custom;

import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.core.api.item.VanillaModeledPolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.Equippable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

import static net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA;

public class SmurfCatHatItem extends SimplePolymerItem {
    public SmurfCatHatItem(Properties settings) {
        super(settings
                .stacksTo(1)
                .durability(363)
                .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD).setSwappable(true).setDamageOnHurt(true).build())
                .component(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(ItemAttributeModifiers.builder().add(Attributes.SCALE,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath("peculiar_creatures", "smurf_cat_cap_armor"),
                                -0.7,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.HEAD
                ).build().modifiers()))

        );
    }

    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return PolymerResourcePackUtils.hasMainPack(context) ? super.getPolymerItemModel(stack, context, lookup) : null;
    }

    @Override
    public Item getPolymerItem(ItemStack stack, PacketContext context) {
        return Items.WHITE_STAINED_GLASS;
    }



}
