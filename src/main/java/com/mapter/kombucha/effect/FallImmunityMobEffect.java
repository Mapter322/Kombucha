package com.mapter.kombucha.effect;

import com.mapter.kombucha.Kombucha;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FallImmunityMobEffect extends MobEffect {
    public FallImmunityMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x6FC3DF);
        addAttributeModifier(
                Attributes.FALL_DAMAGE_MULTIPLIER,
                Identifier.fromNamespaceAndPath(Kombucha.MODID, "effect.fall_immunity"),
                -1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
