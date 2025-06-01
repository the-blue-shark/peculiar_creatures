package net.the_blue_shark.peculiar_creatures.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;
import net.the_blue_shark.peculiar_creatures.entity.custom.SmurfCatEntity;

public class ModEntities {

    public static final EntityType<SmurfCatEntity> SMURF_CAT = register(Identifier.of(PeculiarCreaturesMod.MOD_ID, "smurf_cat"),
            EntityType.Builder.<SmurfCatEntity>create(SmurfCatEntity::new, SpawnGroup.CREATURE).dimensions(0.4f, 0.5f)
    );


    private static <T extends Entity> EntityType<T> register(Identifier provoker, EntityType.Builder<T> build) {
        var type = Registry.register(Registries.ENTITY_TYPE, provoker, build.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, provoker)));
        PolymerEntityUtils.registerType(type);
        return type;
    }

    public static void registerModEntities() {
        PeculiarCreaturesMod.LOGGER.info("Registering Mod Entities for " + PeculiarCreaturesMod.MOD_ID);
    }
}
