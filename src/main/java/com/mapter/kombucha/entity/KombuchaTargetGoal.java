package com.mapter.kombucha.entity;

import java.util.EnumSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

public class KombuchaTargetGoal extends Goal {
    private final Mob mob;
    private LivingEntity target;

    public KombuchaTargetGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof FriendlyKombuchaMonster friendly
                && friendly.getCombatMode() != FriendlyKombuchaMonster.CombatMode.AGGRESSIVE) {
            return false;
        }
        if (this.mob.getTarget() != null) {
            if (CombuchaRelations.isFriendOfCombuchas(this.mob.getTarget())) {
                this.mob.setTarget(null);
            }
            return false;
        }

        ServerLevel level = (ServerLevel) this.mob.level();
        double x = this.mob.getX();
        double y = this.mob.getEyeY();
        double z = this.mob.getZ();
        double followRange = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        TargetingConditions conditions = TargetingConditions.forCombat()
                .range(followRange)
                .selector((entity, ignored) -> !CombuchaRelations.isFriendOfCombuchas(entity)
                        && (!(this.mob instanceof FriendlyKombuchaMonster friendly)
                        || (entity != friendly.getOwner() && !(entity instanceof FriendlyKombuchaMonster))));
        if (!(this.mob instanceof FriendlyKombuchaMonster)) {
            conditions.ignoreLineOfSight();
        }
        LivingEntity playerTarget = level.getNearestPlayer(conditions, this.mob, x, y, z);
        Mob mobTarget = level.getNearestEntity(
                level.getEntitiesOfClass(
                        Mob.class,
                        new AABB(x - followRange, y - followRange, z - followRange,
                                 x + followRange, y + followRange, z + followRange),
                                 candidate -> candidate != this.mob
                                  && (!(this.mob instanceof FriendlyKombuchaMonster)
                                  || !(candidate instanceof FriendlyKombuchaMonster))
                                   && !(candidate instanceof EnderKombuchaMonster)
                                  && !(candidate instanceof NetherKombuchaMonster)
                                  && !(candidate instanceof SpoiledKombuchaMonster)
                                  && !(candidate instanceof CaveKombuchaMonster)),
                conditions, this.mob, x, y, z);

        if (playerTarget == null) {
            this.target = mobTarget;
        } else if (mobTarget == null || this.mob.distanceToSqr(playerTarget) <= this.mob.distanceToSqr(mobTarget)) {
            this.target = playerTarget;
        } else {
            this.target = mobTarget;
        }
        return this.target != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob instanceof FriendlyKombuchaMonster friendly
                && friendly.getCombatMode() != FriendlyKombuchaMonster.CombatMode.AGGRESSIVE) {
            return false;
        }
        LivingEntity currentTarget = this.mob.getTarget();
        return currentTarget != null
                && currentTarget.isAlive()
                && !CombuchaRelations.isFriendOfCombuchas(currentTarget)
                && this.mob.canAttack(currentTarget)
                && (!(this.mob instanceof FriendlyKombuchaMonster)
                        || this.mob.getSensing().hasLineOfSight(currentTarget))
                && this.mob.distanceToSqr(currentTarget)
                <= this.mob.getAttributeValue(Attributes.FOLLOW_RANGE) * this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.target);
    }

    @Override
    public void stop() {
        this.mob.setTarget(null);
        this.target = null;
    }
}
