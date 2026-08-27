package com.mapter.kombucha.block;

/**
 * Fermentation progress of a jar, derived purely from the ticks.
 * The stage boundaries are cumulative durations from the four config values.
 */
public enum FermentationStage {
    ONE,    // sealed tea, no mushroom yet
    TWO,    // mushroom is growing
    THREE,  // matured, ready to bottle
    SPOILED, // the mushroom is spoiled
    MONSTER; // the spoiled kombucha is ready to emerge

    public static FermentationStage of(int fermentationTicks, int ticksToInfested,
                                       int ticksToFermented, int ticksToSpoiled,
                                       int ticksToMonster) {
        long infestedAt = ticksToInfested;
        long fermentedAt = infestedAt + ticksToFermented;
        long spoiledAt = fermentedAt + ticksToSpoiled;
        long monsterAt = spoiledAt + ticksToMonster;

        if (fermentationTicks < infestedAt) return ONE;
        if (fermentationTicks < fermentedAt) return TWO;
        if (fermentationTicks < spoiledAt) return THREE;
        if (fermentationTicks < monsterAt) return SPOILED;
        return MONSTER;
    }
}
