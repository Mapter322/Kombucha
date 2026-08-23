package com.mapter.kombucha.entity;

import com.mapter.kombucha.Kombucha;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SlimeCombuchaProjectile extends CombuchaBlobProjectile {
    public static final float DAMAGE = 4.0F;

    public SlimeCombuchaProjectile(EntityType<? extends SlimeCombuchaProjectile> type, Level level) {
        super(type, level);
    }

    public SlimeCombuchaProjectile(Level level, LivingEntity owner) {
        super(Kombucha.SLIME_COMBUCHA_PROJECTILE.get(), owner, level, Items.SLIME_BALL);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    protected float getDamage() {
        return this.getOwner() instanceof FriendlyKombuchaMonster friendly
                ? friendly.getRangedDamage() : DAMAGE;
    }
}
