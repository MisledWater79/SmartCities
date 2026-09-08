package org.misled.smartcities;

import org.misled.smartcities.registry.SmartCitiesRegistries;

public final class Smartcities {
    public static final String MOD_ID = "smartcities";

    public static void init() {
        SmartCitiesRegistries.init();
    }
}
