package com.mapter.kombucha.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;
import java.util.function.IntSupplier;

public class KombuchaRangedAttackGoal extends Goal {
    private static final double MELEE_DISTANCE_SQR = 2.5D * 2.5D;
    private final Mob mob;
    private final double speedModifier;
    private final RangedAttackMob rangedAttackMob;
    private final IntSupplier attackInterval;
    private final float attackRadius;
    private final float attackRadiusSqr;
    private LivingEntity target;
    private int attackTime = -1;
    private int seeTime;

    public KombuchaRangedAttackGoal(RangedAttackMob mob, double speedModifier, int attackInterval, float attackRadius) {
        this(mob, speedModifier, () -> attackInterval, attackRadius);
    }

    public KombuchaRangedAttackGoal(RangedAttackMob mob, double speedModifier,
                                    IntSupplier attackInterval, float attackRadius) {
        this.rangedAttackMob = mob;
        this.mob = (Mob) mob;
        this.speedModifier = speedModifier;
        this.attackInterval = attackInterval;
        this.attackRadius = attackRadius;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive() || this.mob.distanceToSqr(target) <= MELEE_DISTANCE_SQR) {
            return false;
        }
        if (this.mob instanceof FriendlyKombuchaMonster friendly
                && friendly.getAttackMode() == FriendlyKombuchaMonster.AttackMode.MELEE
                && this.mob.getNavigation().createPath(target, 0) != null) {
            return false;
        }
        this.target = target;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }
        return this.mob.distanceToSqr(this.target) > MELEE_DISTANCE_SQR;
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        double targetDistSqr = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(this.target);
        if (hasLineOfSight) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }
        if (!(targetDistSqr > this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0 && hasLineOfSight) {
            float distance = (float) Math.sqrt(targetDistSqr) / this.attackRadius;
            this.rangedAttackMob.performRangedAttack(this.target, Mth.clamp(distance, 0.1F, 1.0F));
            this.attackTime = Math.max(1, Mth.floor(distance * this.attackInterval.getAsInt()));
        } else if (this.attackTime < 0) {
            this.attackTime = Math.max(1, Mth.floor(Mth.lerp(
                    Math.sqrt(targetDistSqr) / this.attackRadius,
                    this.attackInterval.getAsInt(), this.attackInterval.getAsInt())));
        }
    }

}
