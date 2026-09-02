package org.misled.smartcities.forge;

import org.misled.smartcities.Smartcities;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Smartcities.MOD_ID)
public final class SmartcitiesForge {
    public SmartcitiesForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Smartcities.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        Smartcities.init();
    }
}
