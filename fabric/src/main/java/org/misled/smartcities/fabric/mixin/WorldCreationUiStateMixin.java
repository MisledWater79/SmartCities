package org.misled.smartcities.fabric.mixin;

import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.misled.smartcities.client.SmartCitiesEditor;
import org.misled.smartcities.worldgen.SmartCitiesPresets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla looks up PresetEditor.EDITORS, which is an ImmutableMap. Forge patches this
 * method to consult its own registry, so this mixin is Fabric-only on purpose.
 */
@Mixin(WorldCreationUiState.class)
public abstract class WorldCreationUiStateMixin {
    @Shadow
    public abstract WorldCreationUiState.WorldTypeEntry getWorldType();

    @Inject(method = "getPresetEditor", at = @At("HEAD"), cancellable = true)
    private void smartcities$presetEditor(CallbackInfoReturnable<PresetEditor> cir) {
        Holder<WorldPreset> preset = getWorldType().preset();
        if (preset != null && preset.is(SmartCitiesPresets.SMARTCITIES)) {
            cir.setReturnValue(SmartCitiesEditor.INSTANCE);
        }
    }
}
