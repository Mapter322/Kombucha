package com.mapter.kombucha.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;

public class KombuchaHurtByTargetGoal extends HurtByTargetGoal {
    public KombuchaHurtByTargetGoal(Monster mob) {
        super(mob);
    }

    @Override
    public boolean canUse() {
        return !KombuchaRelations.isFriendOfKombuchas(this.mob.getLastHurtByMob()) && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        return !KombuchaRelations.isFriendOfKombuchas(target) && super.canContinueToUse();
    }
}
