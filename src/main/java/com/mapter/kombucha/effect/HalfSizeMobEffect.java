package com.mapter.kombucha.effect;

import com.mapter.kombucha.Kombucha;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HalfSizeMobEffect extends MobEffect {
    public HalfSizeMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x7A5C9E);
        addAttributeModifier(
                Attributes.SCALE,
                Identifier.fromNamespaceAndPath(Kombucha.MODID, "effect.half_size"),
                -0.5,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
