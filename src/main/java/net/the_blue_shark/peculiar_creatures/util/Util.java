package net.the_blue_shark.peculiar_creatures.util;

import de.tomalbrc.bil.core.model.Model;
import de.tomalbrc.bil.file.loader.AjBlueprintLoader;
import de.tomalbrc.bil.file.loader.BbModelLoader;
import net.minecraft.resources.Identifier;
import net.the_blue_shark.peculiar_creatures.PeculiarCreaturesMod;

public class Util {
    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(PeculiarCreaturesMod.MOD_ID, path);
    }

    public static Model loadModel(Identifier resourceLocation) {
        return AjBlueprintLoader.load(resourceLocation);
    }

    public static Model loadBbModel(Identifier resourceLocation) {
        return BbModelLoader.load(resourceLocation);
    }
}
