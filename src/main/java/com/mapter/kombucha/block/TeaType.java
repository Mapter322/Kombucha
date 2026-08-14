package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

public enum TeaType implements StringRepresentable {
    TEA("tea", "tea_mix", "kombucha_drink", 0xFFB07200),
    APPLE("apple", "apple_tea_mix", "apple_kombucha_drink", 0xFFC7AE6C),
    MELON("melon", "melon_tea_mix", "melon_kombucha_drink", 0xFFB96565),
    NETHER("nether", "nether_tea_mix", "nether_kombucha_drink", 0xFFA13F63),
    ENDER("ender", "ender_tea_mix", "ender_kombucha_drink", 0xFF734C94),
    GOLDEN("golden", "golden_tea_mix", "golden_kombucha_drink", 0xFFF7CE16);

    public static final Codec<TeaType> CODEC = StringRepresentable.fromEnum(TeaType::values);

    private final String name;
    private final String mixId;
    private final String drinkId;
    private final int color;

    TeaType(String name, String mixId, String drinkId, int color) {
        this.name = name;
        this.mixId = mixId;
        this.drinkId = drinkId;
        this.color = color;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public String getMixId() {
        return mixId;
    }

    public String getDrinkId() {
        return drinkId;
    }

    public int getColor() {
        return color;
    }

    // which tea this stack is, or null if it's not a tea mix
    public static TeaType fromStack(ItemStack stack) {
        for (TeaType type : values()) {
            if (stack.is(Kombucha.TEA_MIXES.get(type).get())) {
                return type;
            }
        }
        return null;
    }
}
