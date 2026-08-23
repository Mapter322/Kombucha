package com.mapter.kombucha.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FriendlyKombuchaPerkData(int increasedJumpLevel, int fallImmunityLevel, int regenerationLevel,
                                       int vampirismLevel) {
    public static final FriendlyKombuchaPerkData DEFAULT = new FriendlyKombuchaPerkData(0, 0, 0, 0);

    public static final Codec<FriendlyKombuchaPerkData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("increased_jump_level", 0)
                    .forGetter(FriendlyKombuchaPerkData::increasedJumpLevel),
            Codec.INT.optionalFieldOf("fall_immunity_level", 0)
                    .forGetter(FriendlyKombuchaPerkData::fallImmunityLevel),
            Codec.INT.optionalFieldOf("regeneration_level", 0)
                    .forGetter(FriendlyKombuchaPerkData::regenerationLevel),
            Codec.INT.optionalFieldOf("vampirism_level", 0)
                    .forGetter(FriendlyKombuchaPerkData::vampirismLevel)
    ).apply(instance, FriendlyKombuchaPerkData::new));
}
