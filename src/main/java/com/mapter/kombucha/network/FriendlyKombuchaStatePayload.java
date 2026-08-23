package com.mapter.kombucha.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record FriendlyKombuchaStatePayload(int entityId, int category, int state)
        implements CustomPacketPayload {
    public static final Type<FriendlyKombuchaStatePayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath("kombucha", "friendly_kombucha_state"));
    public static final StreamCodec<FriendlyByteBuf, FriendlyKombuchaStatePayload> STREAM_CODEC =
            CustomPacketPayload.codec(FriendlyKombuchaStatePayload::write, FriendlyKombuchaStatePayload::new);

    private FriendlyKombuchaStatePayload(FriendlyByteBuf input) {
        this(input.readVarInt(), input.readVarInt(), input.readVarInt());
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(entityId);
        output.writeVarInt(category);
        output.writeVarInt(state);
    }

    @Override
    public Type<FriendlyKombuchaStatePayload> type() {
        return TYPE;
    }
}
