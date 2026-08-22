package com.mapter.kombucha.block;

/**
 * Fermentation progress of a jar, derived purely from the ticks.
 * The stage boundaries are cumulative durations from the three config values.
 */
public enum FermentationStage {
    ONE,    // sealed tea, no mushroom yet
    TWO,    // mushroom is growing
    THREE,  // matured, ready to bottle
    SPOILED;

    public static FermentationStage of(int fermentationTicks, int ticksToInfested,
                                       int ticksToFermented, int ticksToSpoiled) {
        long infestedAt = ticksToInfested;
        long fermentedAt = infestedAt + ticksToFermented;
        long spoiledAt = fermentedAt + ticksToSpoiled;

        if (fermentationTicks < infestedAt) return ONE;
        if (fermentationTicks < fermentedAt) return TWO;
        if (fermentationTicks < spoiledAt) return THREE;
        return SPOILED;
    }
}
