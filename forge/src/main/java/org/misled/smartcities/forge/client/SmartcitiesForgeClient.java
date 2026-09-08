package org.misled.smartcities.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterPresetEditorsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.misled.smartcities.Smartcities;
import org.misled.smartcities.client.SmartCitiesEditor;
import org.misled.smartcities.worldgen.SmartCitiesPresets;

/**
 * Forge exposes a first-party hook for this, so no mixin is needed on this loader.
 */
@Mod.EventBusSubscriber(modid = Smartcities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SmartcitiesForgeClient {
    @SubscribeEvent
    public static void registerPresetEditors(RegisterPresetEditorsEvent event) {
        event.register(SmartCitiesPresets.SMARTCITIES, SmartCitiesEditor.INSTANCE);
    }

    private SmartcitiesForgeClient() {
    }
}
