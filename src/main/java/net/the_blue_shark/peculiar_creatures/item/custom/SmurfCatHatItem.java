package net.the_blue_shark.peculiar_creatures.item.custom;

import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.core.api.item.VanillaModeledPolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.component.Component;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.logging.Level;

import static net.minecraft.component.DataComponentTypes.CUSTOM_MODEL_DATA;

public class SmurfCatHatItem extends SimplePolymerItem {
    public SmurfCatHatItem(Settings settings) {
        super(settings
                .maxCount(1)
                .maxDamage(363)
                .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.HEAD).swappable(true).damageOnHurt(true).build())
                .component(DataComponentTypes.ATTRIBUTE_MODIFIERS, new AttributeModifiersComponent(AttributeModifiersComponent.builder().add(EntityAttributes.SCALE,
                        new EntityAttributeModifier(
                                Identifier.of("peculiar_creatures", "smurf_cat_cap_armor"),
                                -0.7,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.HEAD
                ).build().modifiers()))

        );
    }

    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return PolymerResourcePackUtils.hasMainPack(context) ? super.getPolymerItemModel(stack, context) : null;
    }

    @Override
    public Item getPolymerItem(ItemStack stack, PacketContext context) {
        return Items.WHITE_STAINED_GLASS;
    }



}
