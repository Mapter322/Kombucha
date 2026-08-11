package com.mapter.kombucha.block;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum TeaType implements StringRepresentable {
    TEA("tea"),
    APPLE("apple"),
    MELON("melon"),
    NETHER("nether"),
    ENDER("ender"),
    GOLDEN("golden");

    public static final Codec<TeaType> CODEC = StringRepresentable.fromEnum(TeaType::values);

    private final String name;

    TeaType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
