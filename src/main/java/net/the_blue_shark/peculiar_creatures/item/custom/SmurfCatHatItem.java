package net.the_blue_shark.peculiar_creatures.item.custom;

import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class SmurfCatHatItem extends SimplePolymerItem {
    public SmurfCatHatItem(Settings settings) {
        super(settings
                        .maxCount(1)
                        .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.HEAD).swappable(true).damageOnHurt(false).build())
                        .component(DataComponentTypes.ATTRIBUTE_MODIFIERS, new AttributeModifiersComponent(AttributeModifiersComponent.builder().add(EntityAttributes.SCALE,
                                new EntityAttributeModifier(
                                        Identifier.of("peculiar_creatures", "smurf_cat_cap_armor"),
                                        -0.7,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.HEAD
                        )
                        .build().modifiers()))

        );
    }
    @Override
    public Item getPolymerItem(ItemStack stack, PacketContext context) {
        ItemStack whiteLeatherHat = new ItemStack(Items.LEATHER_HELMET);
        DyeColor color = DyeColor.WHITE;
        whiteLeatherHat.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(3));
        if(!PolymerResourcePackUtils.hasMainPack(context)) {
            return whiteLeatherHat.getItem();
        } else {
            return Items.DIRT;
        }
    }



}
