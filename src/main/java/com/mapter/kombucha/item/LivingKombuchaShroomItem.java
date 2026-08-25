package com.mapter.kombucha.item;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LivingKombuchaShroomItem extends Item {

    public LivingKombuchaShroomItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canBeHurtBy(ItemStack stack, DamageSource source) {
        // the monster lives inside — lava and explosions cannot destroy the shroom
        return false;
    }
}
