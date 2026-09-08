package org.misled.smartcities.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.misled.smartcities.worldgen.config.CityProfile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Wraps vanilla noise generation and carries the {@link CityProfile} chosen in the
 * customize screen. NoiseBasedChunkGenerator is final in 1.20.1, so this delegates
 * rather than extends.
 *
 * <p>City generation hooks in by overriding {@link #buildSurface} or
 * {@link #applyBiomeDecoration} instead of forwarding them.
 */
public class CityChunkGenerator extends ChunkGenerator {
    public static final Codec<CityChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            NoiseBasedChunkGenerator.CODEC.fieldOf("base").forGetter(CityChunkGenerator::base),
            CityProfile.CODEC.optionalFieldOf("city_profile", CityProfile.DEFAULT).forGetter(CityChunkGenerator::profile)
    ).apply(instance, CityChunkGenerator::new));

    private final NoiseBasedChunkGenerator base;
    private final CityProfile profile;

    public CityChunkGenerator(NoiseBasedChunkGenerator base, CityProfile profile) {
        super(base.getBiomeSource());
        this.base = base;
        this.profile = profile;
    }

    public NoiseBasedChunkGenerator base() {
        return base;
    }

    public CityProfile profile() {
        return profile;
    }

    /** Same terrain, different city settings. */
    public CityChunkGenerator withProfile(CityProfile newProfile) {
        return new CityChunkGenerator(base, newProfile);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager,
                             StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {
        base.applyCarvers(region, seed, randomState, biomeManager, structureManager, chunk, step);
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
        base.buildSurface(region, structureManager, randomState, chunk);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        base.spawnOriginalMobs(region);
    }

    @Override
    public int getGenDepth() {
        return base.getGenDepth();
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState,
                                                        StructureManager structureManager, ChunkAccess chunk) {
        return base.fillFromNoise(executor, blender, randomState, structureManager, chunk);
    }

    @Override
    public int getSeaLevel() {
        return base.getSeaLevel();
    }

    @Override
    public int getMinY() {
        return base.getMinY();
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState randomState) {
        return base.getBaseHeight(x, z, type, level, randomState);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        return base.getBaseColumn(x, z, level, randomState);
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {
        base.addDebugScreenInfo(info, randomState, pos);
        info.add("SmartCities radius: " + profile.cityRadius() + " density: " + profile.buildingDensity());
    }
}
