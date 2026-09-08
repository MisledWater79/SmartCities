package org.misled.smartcities.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.misled.smartcities.worldgen.CityChunkGenerator;
import org.misled.smartcities.worldgen.config.CityProfile;
import org.misled.smartcities.worldgen.config.CitySetting;
import org.misled.smartcities.worldgen.config.CitySettings;

/**
 * The customize screen itself. Built from {@link CitySettings#ALL}, so it grows
 * automatically as settings are added.
 */
@Environment(EnvType.CLIENT)
public class CustomizeCityScreen extends Screen {
    private static final int HEADER_HEIGHT = 56;
    private static final int FOOTER_HEIGHT = 36;

    private final CreateWorldScreen parent;
    private final WorldCreationContext context;

    /** Working copy; only written back to the world settings on Done. */
    private CityProfile working;
    private OptionsList list;

    public CustomizeCityScreen(CreateWorldScreen parent, WorldCreationContext context) {
        super(Component.translatable("smartcities.gui.customize.title"));
        this.parent = parent;
        this.context = context;
        this.working = readProfile(context);
    }

    private static CityProfile readProfile(WorldCreationContext context) {
        return context.selectedDimensions().overworld() instanceof CityChunkGenerator city
                ? city.profile()
                : CityProfile.DEFAULT;
    }

    @Override
    protected void init() {
        addRenderableWidget(CycleButton
                .<CitySettings.Named>builder(named -> Component.translatable(named.translationKey()))
                .withValues(CitySettings.PROFILES)
                .withInitialValue(CitySettings.PROFILES.get(0))
                .create(width / 2 - 100, 28, 200, 20,
                        Component.translatable("smartcities.gui.customize.profile"),
                        (button, named) -> {
                            working = named.profile();
                            rebuildOptions();
                        }));

        rebuildOptions();

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> apply())
                .bounds(width / 2 - 155, height - 28, 150, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, b -> onClose())
                .bounds(width / 2 + 5, height - 28, 150, 20).build());
    }

    private void rebuildOptions() {
        if (list != null) {
            removeWidget(list);
        }
        list = new OptionsList(minecraft, width, height, HEADER_HEIGHT, height - FOOTER_HEIGHT, 25);
        for (CitySetting<?> setting : CitySettings.ALL) {
            list.addBig(bind(setting));
        }
        addWidget(list);
    }

    private <T> OptionInstance<T> bind(CitySetting<T> setting) {
        return setting.bind(working, value -> working = setting.with().apply(working, value));
    }

    /** Swaps the overworld generator for one carrying the edited profile. */
    private void apply() {
        CityProfile result = working;
        parent.getUiState().updateDimensions((frozen, dimensions) -> {
            ChunkGenerator current = dimensions.overworld();
            if (current instanceof CityChunkGenerator city) {
                return dimensions.replaceOverworldGenerator(frozen, city.withProfile(result));
            }
            if (current instanceof NoiseBasedChunkGenerator noise) {
                return dimensions.replaceOverworldGenerator(frozen, new CityChunkGenerator(noise, result));
            }
            return dimensions;
        });
        onClose();
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        list.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
