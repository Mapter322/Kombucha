package com.mapter.kombucha.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FriendlyKombuchaStateData(
        boolean sitting,
        int movementMode,
        int combatMode,
        int attackMode) {

    public static final FriendlyKombuchaStateData DEFAULT = new FriendlyKombuchaStateData(false, 0, 0, 0);

    public static final Codec<FriendlyKombuchaStateData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("sitting", false).forGetter(FriendlyKombuchaStateData::sitting),
            Codec.INT.optionalFieldOf("movement_mode", 0).forGetter(FriendlyKombuchaStateData::movementMode),
            Codec.INT.optionalFieldOf("combat_mode", 0).forGetter(FriendlyKombuchaStateData::combatMode),
            Codec.INT.optionalFieldOf("attack_mode", 0).forGetter(FriendlyKombuchaStateData::attackMode)
    ).apply(instance, FriendlyKombuchaStateData::new));
}
