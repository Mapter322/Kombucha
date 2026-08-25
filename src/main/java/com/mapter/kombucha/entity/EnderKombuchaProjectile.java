package com.mapter.kombucha.entity;

import com.mapter.kombucha.Kombucha;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class EnderKombuchaProjectile extends KombuchaBlobProjectile {
    public EnderKombuchaProjectile(EntityType<? extends EnderKombuchaProjectile> type, Level level) {
        super(type, level);
    }

    public EnderKombuchaProjectile(Level level, LivingEntity owner) {
        super(Kombucha.ENDER_KOMBUCHA_PROJECTILE.get(), owner, level, Items.ENDER_PEARL);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ENDER_PEARL;
    }

    @Override
    protected float getDamage() {
        return 6.0F;
    }
}
