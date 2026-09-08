package org.misled.smartcities.registry;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.misled.smartcities.Smartcities;
import org.misled.smartcities.worldgen.CityChunkGenerator;

public final class SmartCitiesRegistries {
    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS =
            DeferredRegister.create(Smartcities.MOD_ID, Registries.CHUNK_GENERATOR);

    public static final RegistrySupplier<Codec<? extends ChunkGenerator>> CITY =
            CHUNK_GENERATORS.register("city", () -> CityChunkGenerator.CODEC);

    public static void init() {
        CHUNK_GENERATORS.register();
    }

    private SmartCitiesRegistries() {
    }
}
