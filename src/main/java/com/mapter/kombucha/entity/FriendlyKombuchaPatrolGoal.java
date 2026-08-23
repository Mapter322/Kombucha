package com.mapter.kombucha.entity;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;

public class FriendlyKombuchaPatrolGoal extends Goal {
    private final FriendlyKombuchaMonster mob;
    private final double speedModifier;
    private final PathNavigation navigation;
    private Path patrolPath;

    public FriendlyKombuchaPatrolGoal(FriendlyKombuchaMonster mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.navigation = mob.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getMovementMode() != FriendlyKombuchaMonster.MovementMode.PATROL
                || this.mob.getTarget() != null || !this.navigation.isDone()
                || this.mob.getRandom().nextInt(40) != 0) {
            return false;
        }

        BlockPos center = this.mob.getPatrolCenter();
        for (int attempt = 0; attempt < 10; attempt++) {
            int xOffset = this.mob.getRandom().nextInt(FriendlyKombuchaMonster.PATROL_RADIUS * 2 + 1)
                    - FriendlyKombuchaMonster.PATROL_RADIUS;
            int zOffset = this.mob.getRandom().nextInt(FriendlyKombuchaMonster.PATROL_RADIUS * 2 + 1)
                    - FriendlyKombuchaMonster.PATROL_RADIUS;
            if (xOffset * xOffset + zOffset * zOffset
                    > FriendlyKombuchaMonster.PATROL_RADIUS * FriendlyKombuchaMonster.PATROL_RADIUS) {
                continue;
            }
            int x = center.getX() + xOffset;
            int z = center.getZ() + zOffset;
            int y = this.mob.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            this.patrolPath = this.navigation.createPath(new BlockPos(x, y, z), 0);
            if (this.patrolPath != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getMovementMode() == FriendlyKombuchaMonster.MovementMode.PATROL
                && this.mob.getTarget() == null && !this.navigation.isDone();
    }

    @Override
    public void start() {
        if (this.patrolPath != null) {
            this.navigation.moveTo(this.patrolPath, this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.patrolPath = null;
        this.navigation.stop();
    }
}
