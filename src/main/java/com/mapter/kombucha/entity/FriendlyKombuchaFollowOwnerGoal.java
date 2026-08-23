package com.mapter.kombucha.entity;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;

public class FriendlyKombuchaFollowOwnerGoal extends Goal {
    private final TamableAnimal mob;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private @Nullable LivingEntity owner;
    private int timeToRecalculatePath;
    private boolean directMovement;

    public FriendlyKombuchaFollowOwnerGoal(TamableAnimal mob, double speedModifier,
                                           float startDistance, float stopDistance) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (((FriendlyKombuchaMonster) this.mob).getMovementMode() != FriendlyKombuchaMonster.MovementMode.FOLLOW
                || this.mob.getTarget() != null) {
            return false;
        }
        LivingEntity owner = this.mob.getOwner();
        if (owner == null || this.mob.unableToMoveToOwner()) {
            return false;
        }

        this.owner = owner;
        return this.mob.distanceToSqr(owner) >= this.startDistance * this.startDistance;
    }

    @Override
    public boolean canContinueToUse() {
        return ((FriendlyKombuchaMonster) this.mob).getMovementMode() == FriendlyKombuchaMonster.MovementMode.FOLLOW
                && this.mob.getTarget() == null
                && this.owner != null
                && this.owner.isAlive()
                && !this.mob.unableToMoveToOwner()
                && this.mob.distanceToSqr(this.owner) > this.stopDistance * this.stopDistance;
    }

    @Override
    public void start() {
        this.timeToRecalculatePath = 0;
        this.directMovement = false;
    }

    @Override
    public void tick() {
        if (this.owner == null) {
            return;
        }

        if (this.mob.shouldTryTeleportToOwner()) {
            this.mob.tryToTeleportToOwner();
            return;
        }

        if (this.timeToRecalculatePath-- <= 0) {
            this.timeToRecalculatePath = 5;
            this.directMovement = !this.mob.getNavigation().moveTo(this.owner, this.speedModifier);
        }
        if (this.directMovement) {
            this.mob.getMoveControl().setWantedPosition(
                    this.owner.getX(), this.owner.getY(), this.owner.getZ(), this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.owner = null;
        this.directMovement = false;
        this.mob.getNavigation().stop();
    }
}
