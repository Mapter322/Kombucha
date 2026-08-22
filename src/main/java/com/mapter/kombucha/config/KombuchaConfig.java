package com.mapter.kombucha.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class KombuchaConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue TICKS_TO_INFESTED = BUILDER
            .comment("Ticks until the mushroom appears (24000 = one Minecraft day).")
            .defineInRange("ticksToInfested", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue TICKS_TO_FERMENTED = BUILDER
            .comment("Ticks while the mushroom is fermenting (24000 = one Minecraft day).")
            .defineInRange("ticksToFermented", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue TICKS_TO_SPOILED = BUILDER
            .comment("Ticks until the mushroom spoils (24000 = one Minecraft day).")
            .defineInRange("ticksToSpoiled", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
