package org.misled.smartcities.worldgen.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Describes one editable setting: how to read it off a profile, how to write it back,
 * and what widget represents it. The screen renders whatever settings exist, so adding
 * an option means adding an entry to {@link CitySettings} rather than touching any GUI code.
 *
 * <p>OptionInstance.ValueSet is package-private in vanilla, so the widget shape is captured
 * by a {@link Factory} built through the static helpers below rather than stored directly.
 */
public record CitySetting<T>(
        String translationKey,
        Factory<T> factory,
        Function<CityProfile, T> get,
        BiFunction<CityProfile, T, CityProfile> with
) {
    @FunctionalInterface
    public interface Factory<T> {
        OptionInstance<T> create(String translationKey, T initial, Consumer<T> onChange);
    }

    public OptionInstance<T> bind(CityProfile current, Consumer<T> onChange) {
        return factory.create(translationKey, get.apply(current), onChange);
    }

    /** Integer slider between two inclusive bounds. */
    public static CitySetting<Integer> intRange(
            String translationKey, int min, int max,
            Function<CityProfile, Integer> get, BiFunction<CityProfile, Integer, CityProfile> with) {
        return new CitySetting<>(translationKey, (key, initial, onChange) -> new OptionInstance<>(
                key,
                OptionInstance.noTooltip(),
                (caption, value) -> Options.genericValueLabel(caption, Component.literal(String.valueOf(value))),
                new OptionInstance.IntRange(min, max),
                Codec.INT,
                initial,
                onChange), get, with);
    }

    /** Slider over 0.0 - 1.0, shown as a percentage. */
    public static CitySetting<Double> unitDouble(
            String translationKey,
            Function<CityProfile, Double> get, BiFunction<CityProfile, Double, CityProfile> with) {
        return new CitySetting<>(translationKey, (key, initial, onChange) -> new OptionInstance<>(
                key,
                OptionInstance.noTooltip(),
                (caption, value) -> Options.genericValueLabel(
                        caption, Component.literal(Math.round(value * 100.0D) + "%")),
                OptionInstance.UnitDouble.INSTANCE,
                Codec.DOUBLE,
                initial,
                onChange), get, with);
    }

    /** On/off toggle. */
    public static CitySetting<Boolean> bool(
            String translationKey,
            Function<CityProfile, Boolean> get, BiFunction<CityProfile, Boolean, CityProfile> with) {
        return new CitySetting<>(translationKey,
                (key, initial, onChange) -> OptionInstance.createBoolean(key, initial, onChange::accept),
                get, with);
    }
}
