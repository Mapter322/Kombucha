package com.mapter.kombucha.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import java.util.function.IntSupplier;

public class CombuchaMeleeAttackGoal extends MeleeAttackGoal {
    private final IntSupplier attackInterval;
    private int ticksUntilNextAttack;

    public CombuchaMeleeAttackGoal(PathfinderMob mob, double speedModifier,
                                   boolean followingTargetEvenIfNotSeen, IntSupplier attackInterval) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.attackInterval = attackInterval;
    }

    @Override
    public void start() {
        super.start();
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.ticksUntilNextAttack = 0;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        if (this.ticksUntilNextAttack <= 0 && this.mob.isWithinMeleeAttackRange(target)
                && this.mob.getSensing().hasLineOfSight(target)) {
            this.ticksUntilNextAttack = Math.max(1, this.attackInterval.getAsInt());
            this.mob.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            if (this.mob.level() instanceof net.minecraft.server.level.ServerLevel level) {
                this.mob.doHurtTarget(level, target);
            }
        }
    }
}
