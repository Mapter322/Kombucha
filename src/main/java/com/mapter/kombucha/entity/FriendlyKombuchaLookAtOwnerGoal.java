package com.mapter.kombucha.entity;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;

public class FriendlyKombuchaLookAtOwnerGoal extends Goal {
    private final TamableAnimal mob;
    private @Nullable LivingEntity owner;

    public FriendlyKombuchaLookAtOwnerGoal(TamableAnimal mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null || !this.mob.isTame()) {
            return false;
        }

        LivingEntity owner = this.mob.getOwner();
        if (owner == null || !owner.isAlive() || this.mob.distanceToSqr(owner) > 32.0D * 32.0D) {
            return false;
        }

        this.owner = owner;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.owner != null
                && this.owner.isAlive()
                && this.mob.getTarget() == null
                && this.mob.distanceToSqr(this.owner) <= 32.0D * 32.0D;
    }

    @Override
    public void tick() {
        if (this.owner != null) {
            this.mob.getLookControl().setLookAt(this.owner, 10.0F, this.mob.getMaxHeadXRot());
        }
    }

    @Override
    public void stop() {
        this.owner = null;
    }
}
