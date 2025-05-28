package net.the_blue_shark.peculiar_creatures;

import net.fabricmc.api.ModInitializer;

import net.the_blue_shark.peculiar_creatures.item.ModItems;
import net.the_blue_shark.peculiar_creatures.sound.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeculiarCreaturesMod implements ModInitializer {

	public static final String MOD_ID = "peculiar_creatures";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModSounds.registerSounds();
	}
}