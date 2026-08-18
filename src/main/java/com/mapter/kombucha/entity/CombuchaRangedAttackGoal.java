package com.mapter.kombucha.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class CombuchaRangedAttackGoal extends RangedAttackGoal {
    private static final double MELEE_DISTANCE_SQR = 2.5D * 2.5D;
    private final Mob mob;

    public CombuchaRangedAttackGoal(RangedAttackMob mob, double speedModifier, int attackInterval, float attackRadius) {
        super(mob, speedModifier, attackInterval, attackRadius);
        this.mob = (Mob) mob;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && this.mob.distanceToSqr(target) > MELEE_DISTANCE_SQR && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && this.mob.distanceToSqr(target) > MELEE_DISTANCE_SQR && super.canContinueToUse();
    }
}
