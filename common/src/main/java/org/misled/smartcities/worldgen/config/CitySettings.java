package org.misled.smartcities.worldgen.config;

import java.util.List;

/**
 * The full config surface. Adding a setting is: a field on {@link CityProfile},
 * one entry here, and a lang key. Nothing in the screen changes.
 */
public final class CitySettings {
    public static final CitySetting<Integer> CITY_RADIUS = CitySetting.intRange(
            "smartcities.setting.city_radius", 32, 512,
            CityProfile::cityRadius, CityProfile::withCityRadius);

    public static final CitySetting<Double> BUILDING_DENSITY = CitySetting.unitDouble(
            "smartcities.setting.building_density",
            CityProfile::buildingDensity, CityProfile::withBuildingDensity);

    public static final CitySetting<Integer> MAX_FLOORS = CitySetting.intRange(
            "smartcities.setting.max_floors", 1, 32,
            CityProfile::maxFloors, CityProfile::withMaxFloors);

    public static final CitySetting<Boolean> GENERATE_STREETS = CitySetting.bool(
            "smartcities.setting.generate_streets",
            CityProfile::generateStreets, CityProfile::withGenerateStreets);

    public static final List<CitySetting<?>> ALL = List.of(
            CITY_RADIUS, BUILDING_DENSITY, MAX_FLOORS, GENERATE_STREETS);

    /** Named starting points, like The Lost Cities' profile dropdown. */
    public record Named(String translationKey, CityProfile profile) {
    }

    public static final List<Named> PROFILES = List.of(
            new Named("smartcities.profile.default", CityProfile.DEFAULT),
            new Named("smartcities.profile.dense", new CityProfile(192, 0.9D, 16, true)),
            new Named("smartcities.profile.sparse", new CityProfile(96, 0.25D, 5, true)),
            new Named("smartcities.profile.buildings_only", new CityProfile(128, 0.6D, 8, false)));

    private CitySettings() {
    }
}
