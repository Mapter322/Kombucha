package com.mapter.kombucha.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record FriendlyKombuchaUpgradePayload(int entityId, int stat) implements CustomPacketPayload {
    public static final Type<FriendlyKombuchaUpgradePayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath("kombucha", "friendly_kombucha_upgrade"));
    public static final StreamCodec<FriendlyByteBuf, FriendlyKombuchaUpgradePayload> STREAM_CODEC =
            CustomPacketPayload.codec(FriendlyKombuchaUpgradePayload::write, FriendlyKombuchaUpgradePayload::new);

    private FriendlyKombuchaUpgradePayload(FriendlyByteBuf input) {
        this(input.readVarInt(), input.readVarInt());
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(entityId);
        output.writeVarInt(stat);
    }

    @Override
    public Type<FriendlyKombuchaUpgradePayload> type() {
        return TYPE;
    }
}
