package com.mapter.kombucha.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class KombuchaConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue TICKS_PER_STAGE = BUILDER
            .comment("Number of ticks per fermentation stage. Default: 24000 (1 Minecraft day).")
            .defineInRange("ticksPerStage", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
