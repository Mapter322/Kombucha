package com.mapter.kombucha.entity;

import java.util.EnumSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public class KombuchaFollowPlayerGoal extends Goal {
    private final Monster mob;
    private final double speedModifier;
    private final double startDistance;
    private final double stopDistance;
    private Player target;
    private int timeToRecalculatePath;

    public KombuchaFollowPlayerGoal(Monster mob, double speedModifier, double startDistance, double stopDistance) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null || !(this.mob.level() instanceof ServerLevel level)) {
            return false;
        }

        TargetingConditions conditions = TargetingConditions.forNonCombat()
                .range(this.stopDistance)
                .ignoreLineOfSight()
                .selector((entity, ignored) -> KombuchaRelations.isIdolOfKombuchas(entity));
        this.target = level.getNearestPlayer(conditions, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
        return this.target != null && this.mob.distanceToSqr(this.target) >= this.startDistance * this.startDistance;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null
                && this.target.isAlive()
                && KombuchaRelations.isIdolOfKombuchas(this.target)
                && this.mob.getTarget() == null
                && this.mob.distanceToSqr(this.target) <= this.stopDistance * this.stopDistance;
    }

    @Override
    public void start() {
        this.timeToRecalculatePath = 0;
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.target, 10.0F, this.mob.getMaxHeadXRot());
        if (this.timeToRecalculatePath-- <= 0) {
            this.timeToRecalculatePath = 10;
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.mob.getNavigation().stop();
    }
}
