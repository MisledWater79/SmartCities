package org.misled.smartcities.worldgen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * The settings the customize screen edits. Persisted inside the chunk generator,
 * so a value chosen at world creation survives into level.dat.
 *
 * <p>Every field uses {@code optionalFieldOf} with a default, so adding a new setting
 * in a later version still loads worlds created before it existed.
 */
public record CityProfile(int cityRadius, double buildingDensity, int maxFloors, boolean generateStreets) {
    public static final CityProfile DEFAULT = new CityProfile(128, 0.6D, 8, true);

    public static final Codec<CityProfile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("city_radius", DEFAULT.cityRadius()).forGetter(CityProfile::cityRadius),
            Codec.DOUBLE.optionalFieldOf("building_density", DEFAULT.buildingDensity()).forGetter(CityProfile::buildingDensity),
            Codec.INT.optionalFieldOf("max_floors", DEFAULT.maxFloors()).forGetter(CityProfile::maxFloors),
            Codec.BOOL.optionalFieldOf("generate_streets", DEFAULT.generateStreets()).forGetter(CityProfile::generateStreets)
    ).apply(instance, CityProfile::new));

    public CityProfile withCityRadius(int value) {
        return new CityProfile(value, buildingDensity, maxFloors, generateStreets);
    }

    public CityProfile withBuildingDensity(double value) {
        return new CityProfile(cityRadius, value, maxFloors, generateStreets);
    }

    public CityProfile withMaxFloors(int value) {
        return new CityProfile(cityRadius, buildingDensity, value, generateStreets);
    }

    public CityProfile withGenerateStreets(boolean value) {
        return new CityProfile(cityRadius, buildingDensity, maxFloors, value);
    }
}
