package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum MushroomType implements StringRepresentable {
    REGULAR("regular"),
    GOLDEN("golden"),
    NETHER("nether"),
    ENDER("ender");

    public static final Codec<MushroomType> CODEC = StringRepresentable.fromEnum(MushroomType::values);

    private final String name;

    MushroomType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public static MushroomType fromStack(ItemStack stack) {
        if (stack.is(Kombucha.KOMBUCHA_SHROOM.get())) {
            return REGULAR;
        }
        if (stack.is(Kombucha.GOLDEN_KOMBUCHA_SHROOM.get())) {
            return GOLDEN;
        }
        if (stack.is(Kombucha.NETHER_KOMBUCHA_SHROOM.get())) {
            return NETHER;
        }
        if (stack.is(Kombucha.ENDER_KOMBUCHA_SHROOM.get())) {
            return ENDER;
        }
        return null;
    }

    public Item getItem() {
        return switch (this) {
            case REGULAR -> Kombucha.KOMBUCHA_SHROOM.get();
            case GOLDEN -> Kombucha.GOLDEN_KOMBUCHA_SHROOM.get();
            case NETHER -> Kombucha.NETHER_KOMBUCHA_SHROOM.get();
            case ENDER -> Kombucha.ENDER_KOMBUCHA_SHROOM.get();
        };
    }
}
