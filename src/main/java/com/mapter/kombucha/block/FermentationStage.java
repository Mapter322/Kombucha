package com.mapter.kombucha.block;

/**
 * Fermentation progress of a jar, derived purely from the ticks.
 * Stage N ends after N * ticksPerStage; past 3 stages the brew is spoiled.
 */
public enum FermentationStage {
    ONE,    // sealed tea, no mushroom yet
    TWO,    // mushroom is growing
    THREE,  // matured, ready to bottle
    SPOILED;

    public static FermentationStage of(int fermentationTicks, int ticksPerStage) {
        if (fermentationTicks < ticksPerStage) return ONE;
        if (fermentationTicks < 2 * ticksPerStage) return TWO;
        if (fermentationTicks < 3 * ticksPerStage) return THREE;
        return SPOILED;
    }
}
