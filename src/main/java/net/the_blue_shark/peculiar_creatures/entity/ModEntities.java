package net.the_blue_shark.peculiar_creatures.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;
import net.the_blue_shark.peculiar_creatures.entity.custom.ShrekEntity;
import net.the_blue_shark.peculiar_creatures.entity.custom.SmurfCatEntity;

public class ModEntities {

    public static final EntityType<SmurfCatEntity> SMURF_CAT =
            ModEntities.register(
                    Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, "smurf_cat"),
                    EntityType.Builder.of(SmurfCatEntity::new, MobCategory.CREATURE)
                            .sized(0.3f, 0.6f)
            );

    public static final EntityType<ShrekEntity> SHREK =
            ModEntities.register(
                    Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, "shrek"),
                    EntityType.Builder.of(ShrekEntity::new, MobCategory.MONSTER)
                            .sized(2f, 3f)
            );



    private static <T extends Entity> EntityType<T> register(Identifier provoker, EntityType.Builder<T> build) {
        var type = Registry.register(BuiltInRegistries.ENTITY_TYPE, provoker, build.build(ResourceKey.create(Registries.ENTITY_TYPE, provoker)));
        PolymerEntityUtils.registerType(type);
        return type;
    }

    public static void registerModEntities() {
        FabricDefaultAttributeRegistry.register(ModEntities.SMURF_CAT, SmurfCatEntity.createAttributes());

        PeculiarCreaturesMod.LOGGER.info("Registering Mod Entities for " + PeculiarCreaturesMod.MOD_ID);
    }
}
