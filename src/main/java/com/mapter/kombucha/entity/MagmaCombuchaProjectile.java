package com.mapter.kombucha.entity;

import com.mapter.kombucha.Kombucha;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MagmaCombuchaProjectile extends CombuchaBlobProjectile {
    public MagmaCombuchaProjectile(EntityType<? extends MagmaCombuchaProjectile> type, Level level) {
        super(type, level);
    }

    public MagmaCombuchaProjectile(Level level, LivingEntity owner) {
        super(Kombucha.MAGMA_COMBUCHA_PROJECTILE.get(), owner, level, Items.MAGMA_CREAM);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.MAGMA_CREAM;
    }

    @Override
    protected float getDamage() {
        return 6.0F;
    }

    @Override
    protected boolean setsTargetOnFire() {
        return true;
    }
}
