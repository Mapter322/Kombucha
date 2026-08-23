package com.mapter.kombucha.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FriendlyKombuchaPerkData(int increasedJumpLevel) {
    public static final FriendlyKombuchaPerkData DEFAULT = new FriendlyKombuchaPerkData(0);

    public static final Codec<FriendlyKombuchaPerkData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("increased_jump_level", 0)
                    .forGetter(FriendlyKombuchaPerkData::increasedJumpLevel)
    ).apply(instance, FriendlyKombuchaPerkData::new));
}
