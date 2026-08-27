package com.mapter.kombucha.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class KombuchaConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue TICKS_TO_INFESTED = BUILDER
            .comment("Ticks until the mushroom appears (24000 = one Minecraft day; 12000 = half a day).")
            .defineInRange("ticksToInfested", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue TICKS_TO_FERMENTED = BUILDER
            .comment("Ticks while the mushroom is fermenting (24000 = one Minecraft day; 12000 = half a day).")
            .defineInRange("ticksToFermented", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue TICKS_TO_SPOILED = BUILDER
            .comment("Ticks until the mushroom spoils (24000 = one Minecraft day; 12000 = half a day).")
            .defineInRange("ticksToSpoiled", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue TICKS_TO_MONSTER = BUILDER
            .comment("Ticks while spoiled kombucha is turning into a monster (24000 = one Minecraft day; 12000 = half a day).")
            .defineInRange("ticksToMonster", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BABY_GROWTH_TICKS = BUILDER
            .comment("Ticks until a baby friendly kombucha grows up (24000 = one Minecraft day; 12000 = half a day).")
            .defineInRange("babyGrowthTicks", 24000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> EXPERIENCE_BY_MOB = BUILDER
            .comment("Experience per killed mob or entity tag. The default list contains hostile vanilla mobs only. Format: 'minecraft:zombie=10'. Prefix tags with '#', for example '#minecraft:undead=5'. Use '*=amount' as a default for all mobs. A specific mob overrides a tag, and a tag overrides '*'. Mobs without a matching entry give no experience.")
            .defineList("experienceByMob", List.of(
                    "#minecraft:undead=5",
                    "#minecraft:raiders=8",
                    "minecraft:skeleton=2",
                    "minecraft:zombie=2",
                    "minecraft:blaze=8",
                    "minecraft:breeze=7",
                    "minecraft:cave_spider=5",
                    "minecraft:creaking=10",
                    "minecraft:creeper=7",
                    "minecraft:elder_guardian=15",
                    "minecraft:enderman=8",
                    "minecraft:endermite=3",
                    "minecraft:ender_dragon=30",
                    "minecraft:ghast=10",
                    "minecraft:guardian=8",
                    "minecraft:hoglin=8",
                    "minecraft:magma_cube=5",
                    "minecraft:piglin=6",
                    "minecraft:piglin_brute=10",
                    "minecraft:shulker=10",
                    "minecraft:silverfish=3",
                    "minecraft:slime=3",
                    "minecraft:spider=5",
                    "minecraft:vex=7",
                    "minecraft:warden=25",
                    "minecraft:wither=30",
                    "minecraft:evoker=12",
                    "minecraft:ravager=12",
                    "minecraft:camel_husk=0",
                    "minecraft:skeleton_horse=0",
                    "minecraft:zombie_horse=0"
            ), value -> value instanceof String);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
