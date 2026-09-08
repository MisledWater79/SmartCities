package org.misled.smartcities.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.misled.smartcities.Smartcities;

/**
 * Identifies the world preset that our customize screen attaches to.
 * Matches data/smartcities/worldgen/world_preset/smartcities.json.
 */
public final class SmartCitiesPresets {
    public static final ResourceKey<WorldPreset> SMARTCITIES = ResourceKey.create(
            Registries.WORLD_PRESET, new ResourceLocation(Smartcities.MOD_ID, "smartcities"));

    private SmartCitiesPresets() {
    }
}
