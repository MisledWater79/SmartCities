package org.misled.smartcities.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;

/**
 * Hooked up to the vanilla "Customize" button. Forge registers this through
 * RegisterPresetEditorsEvent; Fabric reaches it through WorldCreationUiStateMixin.
 */
@Environment(EnvType.CLIENT)
public class SmartCitiesEditor implements PresetEditor {
    public static final SmartCitiesEditor INSTANCE = new SmartCitiesEditor();

    @Override
    public Screen createEditScreen(CreateWorldScreen screen, WorldCreationContext context) {
        return new CustomizeCityScreen(screen, context);
    }
}
