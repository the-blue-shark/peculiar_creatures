package net.the_blue_shark.peculiar_creatures;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.the_blue_shark.peculiar_creatures.entity.ModEntities;
import net.the_blue_shark.peculiar_creatures.entity.custom.SmurfCatEntity;
import net.the_blue_shark.peculiar_creatures.item.ModItems;
import net.the_blue_shark.peculiar_creatures.sound.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeculiarCreaturesMod implements ModInitializer {

	public static final String MOD_ID = "peculiar_creatures";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModSounds.registerSounds();
		ModItems.registerModItems();
		ModEntities.registerModEntities();

		PolymerResourcePackUtils.addModAssets(PeculiarCreaturesMod.MOD_ID);
	}
}