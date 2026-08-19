package com.mapter.kombucha.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;

public class CombuchaHurtByTargetGoal extends HurtByTargetGoal {
    public CombuchaHurtByTargetGoal(Monster mob) {
        super(mob);
    }

    @Override
    public boolean canUse() {
        return !CombuchaRelations.isFriendOfCombuchas(this.mob.getLastHurtByMob()) && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        return !CombuchaRelations.isFriendOfCombuchas(target) && super.canContinueToUse();
    }
}
