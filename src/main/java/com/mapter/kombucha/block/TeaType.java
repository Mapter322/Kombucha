package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

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

    /** The tea mix item that produces this type, or null if the stack is not a tea mix. */
    public static TeaType fromStack(ItemStack stack) {
        if (stack.is(Kombucha.TEA_MIX.get())) return TEA;
        if (stack.is(Kombucha.APPLE_TEA_MIX.get())) return APPLE;
        if (stack.is(Kombucha.MELON_TEA_MIX.get())) return MELON;
        if (stack.is(Kombucha.NETHER_TEA_MIX.get())) return NETHER;
        if (stack.is(Kombucha.ENDER_TEA_MIX.get())) return ENDER;
        if (stack.is(Kombucha.GOLDEN_TEA_MIX.get())) return GOLDEN;
        return null;
    }
}
