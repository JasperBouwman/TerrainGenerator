package com.spaceman.terrainGenerator.terrain.terrainNoise;

@FunctionalInterface
public interface NoiseCreator {
    TerrainNoise create();
}
