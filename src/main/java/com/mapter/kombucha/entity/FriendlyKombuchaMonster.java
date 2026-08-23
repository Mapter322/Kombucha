package com.mapter.kombucha.entity;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.component.FriendlyKombuchaPerkData;
import com.mapter.kombucha.component.FriendlyKombuchaStateData;
import com.mapter.kombucha.component.LivingShroomData;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.Optional;
import java.util.UUID;

public class FriendlyKombuchaMonster extends TamableAnimal implements RangedAttackMob {
    public enum MovementMode {
        FOLLOW,
        STAY,
        PATROL
    }

    public enum CombatMode {
        DEFEND,
        PASSIVE,
        AGGRESSIVE
    }

    public enum AttackMode {
        MELEE,
        RANGED
    }

    public static final int STATE_MOVEMENT = 0;
    public static final int STATE_COMBAT = 1;
    public static final int STATE_ATTACK = 2;
    public static final int PATROL_RADIUS = 20;

    public static final int FEED_COOLDOWN_TICKS = 6000;
    public static final int EXPERIENCE_PER_KILL = 5;
    public static final int EXPERIENCE_PER_FEED = 2;
    private static final int BASE_LEVEL_EXPERIENCE = 10;
    public static final int MAX_LEVEL = 40;
    private static final double BASE_HEALTH = 20.0D;
    private static final double MAX_HEALTH = 80.0D;
    private static final int MAX_HEALTH_UPGRADES = 30;
    private static final double BASE_SPEED = 0.20D;
    private static final double[] MOVEMENT_SPEED_BY_UPGRADE = {
            0.20D, 0.25D, 0.29D, 0.32D, 0.34D, 0.35D,
            0.36D, 0.37D, 0.38D, 0.39D, 0.40D
    };
    private static final FriendlyKombuchaPerk[] MUSHROOM_PERKS = {
            FriendlyKombuchaPerk.INCREASED_JUMP,
            FriendlyKombuchaPerk.FALL_IMMUNITY
    };
    private static final double BASE_MELEE_DAMAGE = 6.0D;
    private static final int MAX_MELEE_DAMAGE_UPGRADES = 10;
    private static final int MAX_RANGED_DAMAGE_UPGRADES = 24;
    private static final int MAX_MELEE_SPEED_UPGRADES = 5;
    private static final int MAX_RANGED_SPEED_UPGRADES = 10;
    private static final int MAX_PROJECTILE_SPEED_UPGRADES = 5;
    public static final int STAT_HEALTH = 0;
    public static final int STAT_SPEED = 1;
    public static final int STAT_MELEE_DAMAGE = 2;
    public static final int STAT_RANGED_DAMAGE = 3;
    public static final int STAT_MELEE_SPEED = 4;
    public static final int STAT_RANGED_SPEED = 5;
    public static final int STAT_PROJECTILE_SPEED = 6;

    public static final int MELEE_ATTACK_INTERVAL_TICKS = 20;
    public static final int RANGED_ATTACK_INTERVAL_TICKS = 45;
    public static final float RANGED_PROJECTILE_BASE_SPEED = 0.8F;
    public static final float RANGED_PROJECTILE_POWER_SCALE = 0.2667F;
    public static final float RANGED_PROJECTILE_MIN_POWER = 0.1F;
    public static final float RANGED_PROJECTILE_MAX_POWER = 1.0F;
    public static final float MIN_PROJECTILE_SPEED = 0.05F;

    private static final EntityDataAccessor<Integer> SHOOT_TIME =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EXPERIENCE =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LEVEL =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> UPGRADE_POINTS =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEALTH_UPGRADES =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SPEED_UPGRADES =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MELEE_DAMAGE_UPGRADES =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RANGED_DAMAGE_UPGRADES =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MELEE_SPEED_UPGRADES =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RANGED_SPEED_UPGRADES =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PROJECTILE_SPEED_UPGRADES =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> INCREASED_JUMP_PERK_LEVEL =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FALL_IMMUNITY_PERK_LEVEL =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> REGENERATION_PERK_LEVEL =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MOVEMENT_MODE =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMBAT_MODE =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_MODE =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
    private int feedCooldown;
    private BlockPos patrolCenter = BlockPos.ZERO;
    private double rangedTargetX;
    private double rangedTargetY;
    private double rangedTargetZ;
    private float rangedPower;

    public FriendlyKombuchaMonster(EntityType<? extends FriendlyKombuchaMonster> type, Level level) {
        super(type, level);
        this.moveControl = new MoveControl(this) {
            private double smoothedX;
            private double smoothedY;
            private double smoothedZ;
            private boolean hasTarget;
            private int ticksSinceCommand;

            @Override
            public void setWantedPosition(double x, double y, double z, double speed) {
                if (!this.hasTarget || this.ticksSinceCommand > 2) {
                    this.smoothedX = FriendlyKombuchaMonster.this.getX();
                    this.smoothedY = FriendlyKombuchaMonster.this.getY();
                    this.smoothedZ = FriendlyKombuchaMonster.this.getZ();
                    this.hasTarget = true;
                }

                this.smoothedX += (x - this.smoothedX) * 0.65D;
                this.smoothedY += (y - this.smoothedY) * 0.65D;
                this.smoothedZ += (z - this.smoothedZ) * 0.65D;
                this.ticksSinceCommand = 0;
                super.setWantedPosition(this.smoothedX, this.smoothedY, this.smoothedZ, speed);
            }

            @Override
            public void tick() {
                this.ticksSinceCommand++;
                if (this.ticksSinceCommand > 2) {
                    this.hasTarget = false;
                }
                super.tick();
            }
        };
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new KombuchaRangedAttackGoal(this, 1.0,
                this::getRangedAttackIntervalTicks, 12.0F));
        this.goalSelector.addGoal(2, new KombuchaMeleeAttackGoal(this, 1.0, true,
                this::getMeleeAttackIntervalTicks));
        this.goalSelector.addGoal(3, new FriendlyKombuchaFollowOwnerGoal(this, 1.0, 3.0F, 2.0F));
        this.goalSelector.addGoal(4, new FriendlyKombuchaPatrolGoal(this, 1.0));
        this.goalSelector.addGoal(5, new FriendlyKombuchaLookAtOwnerGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this) {
            @Override
            public boolean canUse() {
                return FriendlyKombuchaMonster.this.getCombatMode() == CombatMode.DEFEND && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return FriendlyKombuchaMonster.this.getCombatMode() == CombatMode.DEFEND
                        && super.canContinueToUse();
            }
        });
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return FriendlyKombuchaMonster.this.getCombatMode() == CombatMode.DEFEND && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return FriendlyKombuchaMonster.this.getCombatMode() == CombatMode.DEFEND
                        && super.canContinueToUse();
            }
        });
        this.targetSelector.addGoal(3, new KombuchaTargetGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!this.isTame() && itemStack.is(Items.SUGAR)) {
            if (!this.level().isClientSide()) {
                this.tame(player);
                this.navigation.stop();
                this.level().broadcastEntityEvent(this, (byte) 7);
                itemStack.consume(1, player);
                playEatingSound();
                this.feedCooldown = FEED_COOLDOWN_TICKS;
                this.addExperience(EXPERIENCE_PER_FEED);
            }
            return InteractionResult.SUCCESS;
        }
        if (!this.isTame()) {
            if (!this.level().isClientSide()) {
                player.sendOverlayMessage(Component.translatable("kombucha.hint.tame"));
            }
            return InteractionResult.SUCCESS;
        }
        if (itemStack.is(Items.SUGAR)) {
            if (this.feedCooldown > 0) {
                if (!this.level().isClientSide()) {
                    player.sendOverlayMessage(Component.translatable("kombucha.hint.feed_cooldown"));
                }
                return InteractionResult.SUCCESS;
            }
            if (!this.level().isClientSide()) {
                itemStack.consume(1, player);
                playEatingSound();
                this.feedCooldown = FEED_COOLDOWN_TICKS;
                this.addExperience(EXPERIENCE_PER_FEED);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.SUCCESS;
        }
        if (isPerkMushroom(itemStack)) {
            if (!this.level().isClientSide()) {
                itemStack.consume(1, player);
                playEatingSound();
                this.rollPerk(player);
            }
            return InteractionResult.SUCCESS;
        }
        if (isRegenerationMushroom(itemStack)) {
            if (!this.level().isClientSide()) {
                itemStack.consume(1, player);
                playEatingSound();
                this.rollPerk(player, FriendlyKombuchaPerk.REGENERATION);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(SHOOT_TIME, 0);
        entityData.define(EXPERIENCE, 0);
        entityData.define(LEVEL, 1);
        entityData.define(UPGRADE_POINTS, 0);
        entityData.define(HEALTH_UPGRADES, 0);
        entityData.define(SPEED_UPGRADES, 0);
        entityData.define(MELEE_DAMAGE_UPGRADES, 0);
        entityData.define(RANGED_DAMAGE_UPGRADES, 0);
        entityData.define(MELEE_SPEED_UPGRADES, 0);
        entityData.define(RANGED_SPEED_UPGRADES, 0);
        entityData.define(PROJECTILE_SPEED_UPGRADES, 0);
        entityData.define(INCREASED_JUMP_PERK_LEVEL, 0);
        entityData.define(FALL_IMMUNITY_PERK_LEVEL, 0);
        entityData.define(REGENERATION_PERK_LEVEL, 0);
        entityData.define(MOVEMENT_MODE, MovementMode.FOLLOW.ordinal());
        entityData.define(COMBAT_MODE, CombatMode.DEFEND.ordinal());
        entityData.define(ATTACK_MODE, AttackMode.MELEE.ordinal());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.feedCooldown > 0) {
            this.feedCooldown--;
        }
        if (!this.level().isClientSide() && getRegenerationPerkLevel() > 0
                && this.tickCount % getRegenerationIntervalTicks() == 0) {
            this.heal(1.0F);
        }
        if (!this.level().isClientSide() && this.getShootTime() > 0) {
            int shootTime = this.getShootTime();
            if (shootTime == 4 && this.level() instanceof ServerLevel serverLevel) {
                this.launchSlimeBlob(serverLevel);
            }
            this.entityData.set(SHOOT_TIME, shootTime - 1);
        }
    }

    public int getShootTime() {
        return this.entityData.get(SHOOT_TIME);
    }

    public static float getRangedProjectileSpeed(float power) {
        return RANGED_PROJECTILE_BASE_SPEED + power * RANGED_PROJECTILE_POWER_SCALE;
    }

    public float getRangedProjectileSpeedWithUpgrades(float power) {
        int upgrades = Math.min(getProjectileSpeedUpgrades(), MAX_PROJECTILE_SPEED_UPGRADES);
        return Math.max(MIN_PROJECTILE_SPEED, getRangedProjectileSpeed(power) + upgrades * 0.05F);
    }

    public boolean canUpgradeProjectileSpeed() {
        return getProjectileSpeedUpgrades() < MAX_PROJECTILE_SPEED_UPGRADES;
    }

    @Override
    public void performRangedAttack(net.minecraft.world.entity.LivingEntity target, float power) {
        if (this.level() instanceof ServerLevel) {
            this.rangedTargetX = target.getX();
            this.rangedTargetY = target.getEyeY() - 1.1F;
            this.rangedTargetZ = target.getZ();
            this.rangedPower = power;
            this.entityData.set(SHOOT_TIME, 8);
        }
    }

    private void launchSlimeBlob(ServerLevel serverLevel) {
        double xd = this.rangedTargetX - this.getX();
        double zd = this.rangedTargetZ - this.getZ();
        double yo = Math.sqrt(xd * xd + zd * zd) * 0.2F;
        SlimeCombuchaProjectile projectile = new SlimeCombuchaProjectile(serverLevel, this);
        Projectile.spawnProjectileUsingShoot(projectile, serverLevel, new ItemStack(Items.SLIME_BALL),
                xd, this.rangedTargetY + yo - projectile.getY(), zd,
                 getRangedProjectileSpeedWithUpgrades(this.rangedPower), 4.0F);
        this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, 0.8F + this.random.nextFloat() * 0.2F);
    }

    @Override
    public FriendlyKombuchaMonster getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return Kombucha.FRIENDLY_KOMBUCHA_MONSTER.get().create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    protected void playStepSound(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState blockState) {
        this.playSound(SoundEvents.SLIME_SQUISH, 0.4F, 0.9F + this.random.nextFloat() * 0.2F);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    public int getExperience() {
        return this.entityData.get(EXPERIENCE);
    }

    public int getLevel() {
        return this.entityData.get(LEVEL);
    }

    public int getExperienceToNextLevel() {
        if (getLevel() >= MAX_LEVEL) {
            return 0;
        }
        return Math.max(1, (int) Math.ceil(BASE_LEVEL_EXPERIENCE * Math.pow(1.1D, getLevel() - 1)));
    }

    public int getAvailableUpgradePoints() {
        return this.entityData.get(UPGRADE_POINTS);
    }

    public int getHealthUpgrades() {
        return this.entityData.get(HEALTH_UPGRADES);
    }

    public boolean canUpgradeStat(int stat) {
        return switch (stat) {
            case STAT_HEALTH -> getHealthUpgrades() < MAX_HEALTH_UPGRADES;
            case STAT_SPEED -> canUpgradeSpeed();
            case STAT_MELEE_DAMAGE -> getMeleeDamageUpgrades() < MAX_MELEE_DAMAGE_UPGRADES;
            case STAT_RANGED_DAMAGE -> getRangedDamageUpgrades() < MAX_RANGED_DAMAGE_UPGRADES;
            case STAT_MELEE_SPEED -> getMeleeSpeedUpgrades() < MAX_MELEE_SPEED_UPGRADES;
            case STAT_RANGED_SPEED -> getRangedSpeedUpgrades() < MAX_RANGED_SPEED_UPGRADES;
            case STAT_PROJECTILE_SPEED -> canUpgradeProjectileSpeed();
            default -> false;
        };
    }

    public int getSpeedUpgrades() {
        return this.entityData.get(SPEED_UPGRADES);
    }

    public boolean canUpgradeSpeed() {
        return getSpeedUpgrades() < MOVEMENT_SPEED_BY_UPGRADE.length - 1;
    }

    public int getMeleeDamageUpgrades() {
        return this.entityData.get(MELEE_DAMAGE_UPGRADES);
    }

    public int getRangedDamageUpgrades() {
        return this.entityData.get(RANGED_DAMAGE_UPGRADES);
    }

    public int getMeleeSpeedUpgrades() {
        return this.entityData.get(MELEE_SPEED_UPGRADES);
    }

    public int getRangedSpeedUpgrades() {
        return this.entityData.get(RANGED_SPEED_UPGRADES);
    }

    public int getProjectileSpeedUpgrades() {
        return this.entityData.get(PROJECTILE_SPEED_UPGRADES);
    }

    public int getPerkLevel(FriendlyKombuchaPerk perk) {
        return switch (perk) {
            case INCREASED_JUMP -> this.entityData.get(INCREASED_JUMP_PERK_LEVEL);
            case FALL_IMMUNITY -> this.entityData.get(FALL_IMMUNITY_PERK_LEVEL);
            case REGENERATION -> this.entityData.get(REGENERATION_PERK_LEVEL);
        };
    }

    public int getIncreasedJumpPerkLevel() {
        return getPerkLevel(FriendlyKombuchaPerk.INCREASED_JUMP);
    }

    public float getPerkStepHeight() {
        return switch (getIncreasedJumpPerkLevel()) {
            case 1 -> 1.5F;
            case 2 -> 2.0F;
            case 3 -> 2.5F;
            default -> 0.6F;
        };
    }

    public int getFallImmunityPerkLevel() {
        return getPerkLevel(FriendlyKombuchaPerk.FALL_IMMUNITY);
    }

    public int getRegenerationPerkLevel() {
        return getPerkLevel(FriendlyKombuchaPerk.REGENERATION);
    }

    public int getRegenerationIntervalTicks() {
        return switch (getRegenerationPerkLevel()) {
            case 1 -> 10 * 20;
            case 2 -> 7 * 20;
            case 3 -> 5 * 20;
            default -> Integer.MAX_VALUE;
        };
    }

    public MovementMode getMovementMode() {
        return enumValue(MovementMode.values(), this.entityData.get(MOVEMENT_MODE));
    }

    public CombatMode getCombatMode() {
        return enumValue(CombatMode.values(), this.entityData.get(COMBAT_MODE));
    }

    public AttackMode getAttackMode() {
        return enumValue(AttackMode.values(), this.entityData.get(ATTACK_MODE));
    }

    public boolean setState(int category, int state) {
        if (this.level().isClientSide()) {
            return false;
        }
        return switch (category) {
            case STATE_MOVEMENT -> setMovementMode(state);
            case STATE_COMBAT -> setCombatMode(state);
            case STATE_ATTACK -> setAttackMode(state);
            default -> false;
        };
    }

    private boolean setMovementMode(int state) {
        if (state < 0 || state >= MovementMode.values().length) {
            return false;
        }
        MovementMode mode = MovementMode.values()[state];
        this.entityData.set(MOVEMENT_MODE, state);
        this.setOrderedToSit(mode == MovementMode.STAY);
        this.setInSittingPose(mode == MovementMode.STAY);
        this.setTarget(null);
        this.setAggressive(false);
        if (mode == MovementMode.PATROL) {
            this.patrolCenter = this.blockPosition();
        }
        this.getNavigation().stop();
        return true;
    }

    private boolean setCombatMode(int state) {
        if (state < 0 || state >= CombatMode.values().length) {
            return false;
        }
        this.entityData.set(COMBAT_MODE, state);
        this.setTarget(null);
        this.getNavigation().stop();
        return true;
    }

    private boolean setAttackMode(int state) {
        if (state < 0 || state >= AttackMode.values().length) {
            return false;
        }
        this.entityData.set(ATTACK_MODE, state);
        this.setTarget(null);
        this.getNavigation().stop();
        return true;
    }

    public BlockPos getPatrolCenter() {
        return this.patrolCenter;
    }

    private static <T> T enumValue(T[] values, int index) {
        return values[Math.max(0, Math.min(index, values.length - 1))];
    }

    public float getRangedDamage() {
        int upgrades = Math.min(getRangedDamageUpgrades(), MAX_RANGED_DAMAGE_UPGRADES);
        return SlimeCombuchaProjectile.DAMAGE + upgrades * 0.5F;
    }

    public int getMeleeAttackIntervalTicks() {
        int upgrades = Math.min(getMeleeSpeedUpgrades(), MAX_MELEE_SPEED_UPGRADES);
        return Math.max(1, MELEE_ATTACK_INTERVAL_TICKS - upgrades * 2);
    }

    public int getRangedAttackIntervalTicks() {
        int upgrades = Math.min(getRangedSpeedUpgrades(), MAX_RANGED_SPEED_UPGRADES);
        return Math.max(1, RANGED_ATTACK_INTERVAL_TICKS - upgrades * 2);
    }

    public void addExperience(int amount) {
        if (this.level().isClientSide() || amount <= 0 || getLevel() >= MAX_LEVEL) {
            return;
        }
        int experience = getExperience() + amount;
        int level = getLevel();
        int points = getAvailableUpgradePoints();
        while (level < MAX_LEVEL && experience >= getExperienceToNextLevel(level)) {
            experience -= getExperienceToNextLevel(level);
            level++;
            points++;
        }
        if (level >= MAX_LEVEL) {
            experience = 0;
        }
        this.entityData.set(EXPERIENCE, experience);
        this.entityData.set(LEVEL, level);
        this.entityData.set(UPGRADE_POINTS, points);
    }

    private int getExperienceToNextLevel(int level) {
        return Math.max(1, (int) Math.ceil(BASE_LEVEL_EXPERIENCE * Math.pow(1.1D, level - 1)));
    }

    public boolean upgradeStat(int stat) {
        if (this.level().isClientSide() || getAvailableUpgradePoints() <= 0
                || stat < STAT_HEALTH || stat > STAT_PROJECTILE_SPEED) {
            return false;
        }
        EntityDataAccessor<Integer> accessor = switch (stat) {
            case STAT_HEALTH -> HEALTH_UPGRADES;
            case STAT_SPEED -> SPEED_UPGRADES;
            case STAT_MELEE_DAMAGE -> MELEE_DAMAGE_UPGRADES;
            case STAT_RANGED_DAMAGE -> RANGED_DAMAGE_UPGRADES;
            case STAT_MELEE_SPEED -> MELEE_SPEED_UPGRADES;
            case STAT_RANGED_SPEED -> RANGED_SPEED_UPGRADES;
            case STAT_PROJECTILE_SPEED -> PROJECTILE_SPEED_UPGRADES;
            default -> null;
        };
        if (accessor == null) {
            return false;
        }
        if (!canUpgradeStat(stat)) {
            return false;
        }
        this.entityData.set(accessor, this.entityData.get(accessor) + 1);
        this.entityData.set(UPGRADE_POINTS, getAvailableUpgradePoints() - 1);
        applyUpgradedAttributes();
        return true;
    }

    private void applyUpgradedAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(
                Math.min(MAX_HEALTH, BASE_HEALTH + getHealthUpgrades() * 2.0D));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(getMovementSpeed());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(
                BASE_MELEE_DAMAGE + Math.min(getMeleeDamageUpgrades(), MAX_MELEE_DAMAGE_UPGRADES));
        this.setHealth(Math.min(this.getHealth(), this.getMaxHealth()));
    }

    private double getMovementSpeed() {
        int upgradeLevel = Math.max(0, Math.min(getSpeedUpgrades(), MOVEMENT_SPEED_BY_UPGRADE.length - 1));
        return MOVEMENT_SPEED_BY_UPGRADE[upgradeLevel];
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        int level = clampLevel(input.getIntOr("KombuchaLevel", 1));
        this.entityData.set(EXPERIENCE, level >= MAX_LEVEL
                ? 0 : Math.max(0, input.getIntOr("KombuchaExperience", 0)));
        this.entityData.set(LEVEL, level);
        this.entityData.set(UPGRADE_POINTS, input.getIntOr("KombuchaUpgradePoints", 0));
        this.entityData.set(HEALTH_UPGRADES,
                clampUpgradeCount(input.getIntOr("KombuchaHealthUpgrades", 0), MAX_HEALTH_UPGRADES));
        this.entityData.set(SPEED_UPGRADES,
                clampUpgradeCount(input.getIntOr("KombuchaSpeedUpgrades", 0), MOVEMENT_SPEED_BY_UPGRADE.length - 1));
        this.entityData.set(MELEE_DAMAGE_UPGRADES,
                clampUpgradeCount(input.getIntOr("KombuchaMeleeDamageUpgrades", 0), MAX_MELEE_DAMAGE_UPGRADES));
        this.entityData.set(RANGED_DAMAGE_UPGRADES,
                clampUpgradeCount(input.getIntOr("KombuchaRangedDamageUpgrades", 0), MAX_RANGED_DAMAGE_UPGRADES));
        this.entityData.set(MELEE_SPEED_UPGRADES,
                clampUpgradeCount(input.getIntOr("KombuchaMeleeSpeedUpgrades", 0), MAX_MELEE_SPEED_UPGRADES));
        this.entityData.set(RANGED_SPEED_UPGRADES,
                clampUpgradeCount(input.getIntOr("KombuchaRangedSpeedUpgrades", 0), MAX_RANGED_SPEED_UPGRADES));
        this.entityData.set(PROJECTILE_SPEED_UPGRADES,
                clampUpgradeCount(input.getIntOr("KombuchaProjectileSpeedUpgrades", 0), MAX_PROJECTILE_SPEED_UPGRADES));
        this.entityData.set(INCREASED_JUMP_PERK_LEVEL,
                Math.max(0, Math.min(FriendlyKombuchaPerk.INCREASED_JUMP.getMaxLevel(),
                        input.getIntOr("KombuchaIncreasedJumpPerkLevel", 0))));
        this.entityData.set(FALL_IMMUNITY_PERK_LEVEL,
                Math.max(0, Math.min(FriendlyKombuchaPerk.FALL_IMMUNITY.getMaxLevel(),
                        input.getIntOr("KombuchaFallImmunityPerkLevel", 0))));
        this.entityData.set(REGENERATION_PERK_LEVEL,
                Math.max(0, Math.min(FriendlyKombuchaPerk.REGENERATION.getMaxLevel(),
                        input.getIntOr("KombuchaRegenerationPerkLevel", 0))));
        this.entityData.set(MOVEMENT_MODE, input.getIntOr("KombuchaMovementMode", MovementMode.FOLLOW.ordinal()));
        this.entityData.set(COMBAT_MODE, input.getIntOr("KombuchaCombatMode", CombatMode.DEFEND.ordinal()));
        this.entityData.set(ATTACK_MODE, input.getIntOr("KombuchaAttackMode", AttackMode.MELEE.ordinal()));
        this.setOrderedToSit(getMovementMode() == MovementMode.STAY);
        this.patrolCenter = new BlockPos(input.getIntOr("KombuchaPatrolX", this.blockPosition().getX()),
                input.getIntOr("KombuchaPatrolY", this.blockPosition().getY()),
                input.getIntOr("KombuchaPatrolZ", this.blockPosition().getZ()));
        this.feedCooldown = input.getIntOr("KombuchaFeedCooldown", 0);
        applyUpgradedAttributes();
        applyPerkAttributes();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("KombuchaExperience", getExperience());
        output.putInt("KombuchaLevel", getLevel());
        output.putInt("KombuchaUpgradePoints", getAvailableUpgradePoints());
        output.putInt("KombuchaHealthUpgrades", getHealthUpgrades());
        output.putInt("KombuchaSpeedUpgrades", getSpeedUpgrades());
        output.putInt("KombuchaMeleeDamageUpgrades", getMeleeDamageUpgrades());
        output.putInt("KombuchaRangedDamageUpgrades", getRangedDamageUpgrades());
        output.putInt("KombuchaMeleeSpeedUpgrades", getMeleeSpeedUpgrades());
        output.putInt("KombuchaRangedSpeedUpgrades", getRangedSpeedUpgrades());
        output.putInt("KombuchaProjectileSpeedUpgrades", getProjectileSpeedUpgrades());
        output.putInt("KombuchaIncreasedJumpPerkLevel", getIncreasedJumpPerkLevel());
        output.putInt("KombuchaFallImmunityPerkLevel", getFallImmunityPerkLevel());
        output.putInt("KombuchaRegenerationPerkLevel", getRegenerationPerkLevel());
        output.putInt("KombuchaMovementMode", getMovementMode().ordinal());
        output.putInt("KombuchaCombatMode", getCombatMode().ordinal());
        output.putInt("KombuchaAttackMode", getAttackMode().ordinal());
        output.putInt("KombuchaPatrolX", this.patrolCenter.getX());
        output.putInt("KombuchaPatrolY", this.patrolCenter.getY());
        output.putInt("KombuchaPatrolZ", this.patrolCenter.getZ());
        output.putInt("KombuchaFeedCooldown", this.feedCooldown);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hurt = super.doHurtTarget(level, target);
        if (hurt && target instanceof Mob mob && !mob.isAlive()) {
            this.addExperience(EXPERIENCE_PER_KILL);
        }
        return hurt;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        // the whole monster lives on inside a shroom — everything but the experience bar
        this.spawnAtLocation(level, this.captureLivingShroomData().toItemStack());
    }

    public LivingShroomData captureLivingShroomData() {
        Optional<UUID> owner = Optional.ofNullable(this.getOwnerReference()).map(EntityReference::getUUID);
        Optional<Component> name = Optional.ofNullable(this.getCustomName());
        return new LivingShroomData(owner, name, getLevel(), getAvailableUpgradePoints(),
                getHealthUpgrades(), getSpeedUpgrades(), getMeleeDamageUpgrades(),
                getRangedDamageUpgrades(), getMeleeSpeedUpgrades(), getRangedSpeedUpgrades(),
                getProjectileSpeedUpgrades(), this.feedCooldown,
                new FriendlyKombuchaStateData(this.isOrderedToSit(), getMovementMode().ordinal(),
                         getCombatMode().ordinal(), getAttackMode().ordinal()),
                 new FriendlyKombuchaPerkData(getIncreasedJumpPerkLevel(), getFallImmunityPerkLevel(),
                         getRegenerationPerkLevel()));
    }

    public void applyLivingShroomData(LivingShroomData data) {
        data.customName().ifPresent(this::setCustomName);
        data.ownerUuid().ifPresent(uuid -> {
            this.setOwnerReference(EntityReference.of(uuid));
            this.setTame(true, false);
        });
        this.entityData.set(MOVEMENT_MODE, data.sitting() ? MovementMode.STAY.ordinal()
                : data.movementMode());
        this.entityData.set(COMBAT_MODE, data.combatMode());
        this.entityData.set(ATTACK_MODE, data.attackMode());
        this.setOrderedToSit(getMovementMode() == MovementMode.STAY);
        this.patrolCenter = this.blockPosition();
        this.entityData.set(LEVEL, clampLevel(data.level()));
        this.entityData.set(UPGRADE_POINTS, data.upgradePoints());
        this.entityData.set(HEALTH_UPGRADES, clampUpgradeCount(data.healthUpgrades(), MAX_HEALTH_UPGRADES));
        this.entityData.set(SPEED_UPGRADES,
                clampUpgradeCount(data.speedUpgrades(), MOVEMENT_SPEED_BY_UPGRADE.length - 1));
        this.entityData.set(MELEE_DAMAGE_UPGRADES,
                clampUpgradeCount(data.meleeDamageUpgrades(), MAX_MELEE_DAMAGE_UPGRADES));
        this.entityData.set(RANGED_DAMAGE_UPGRADES,
                clampUpgradeCount(data.rangedDamageUpgrades(), MAX_RANGED_DAMAGE_UPGRADES));
        this.entityData.set(MELEE_SPEED_UPGRADES,
                clampUpgradeCount(data.meleeSpeedUpgrades(), MAX_MELEE_SPEED_UPGRADES));
        this.entityData.set(RANGED_SPEED_UPGRADES,
                clampUpgradeCount(data.rangedSpeedUpgrades(), MAX_RANGED_SPEED_UPGRADES));
        this.entityData.set(PROJECTILE_SPEED_UPGRADES,
                clampUpgradeCount(data.projectileSpeedUpgrades(), MAX_PROJECTILE_SPEED_UPGRADES));
        this.entityData.set(INCREASED_JUMP_PERK_LEVEL,
                Math.max(0, Math.min(FriendlyKombuchaPerk.INCREASED_JUMP.getMaxLevel(),
                        data.perkData().increasedJumpLevel())));
        this.entityData.set(FALL_IMMUNITY_PERK_LEVEL,
                Math.max(0, Math.min(FriendlyKombuchaPerk.FALL_IMMUNITY.getMaxLevel(),
                        data.perkData().fallImmunityLevel())));
        this.entityData.set(REGENERATION_PERK_LEVEL,
                Math.max(0, Math.min(FriendlyKombuchaPerk.REGENERATION.getMaxLevel(),
                        data.perkData().regenerationLevel())));
        this.feedCooldown = data.feedCooldown();
        // the experience bar is the only thing that does not survive death
        this.entityData.set(EXPERIENCE, 0);
        applyUpgradedAttributes();
        applyPerkAttributes();
        // come back to life at full health
        this.setHealth(this.getMaxHealth());
    }

    /** Spawns the kombucha back from a matured living shroom jar. */
    public static FriendlyKombuchaMonster reviveFromShroom(Level level, BlockPos pos, LivingShroomData data) {
        FriendlyKombuchaMonster kombucha = Kombucha.FRIENDLY_KOMBUCHA_MONSTER.get().create(level, EntitySpawnReason.MOB_SUMMONED);
        kombucha.setPos(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
        kombucha.setYRot(level.getRandom().nextFloat() * 360.0F);
        kombucha.applyLivingShroomData(data);
        return kombucha;
    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        LivingEntity owner = this.getOwner();
        return owner != null && this.distanceToSqr(owner) >= 24.0D * 24.0D;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this) {
            @Override
            public void clientTick() {
                double xMovement = FriendlyKombuchaMonster.this.getX() - FriendlyKombuchaMonster.this.xo;
                double zMovement = FriendlyKombuchaMonster.this.getZ() - FriendlyKombuchaMonster.this.zo;
                if (xMovement * xMovement + zMovement * zMovement > 2.5E-7D) {
                    FriendlyKombuchaMonster.this.yBodyRot = net.minecraft.util.Mth.rotateIfNecessary(
                            FriendlyKombuchaMonster.this.yBodyRot, FriendlyKombuchaMonster.this.getYRot(), 5.0F);
                } else {
                    FriendlyKombuchaMonster.this.yBodyRot = net.minecraft.util.Mth.rotateIfNecessary(
                            FriendlyKombuchaMonster.this.yBodyRot, FriendlyKombuchaMonster.this.yHeadRot, 5.0F);
                }
            }
        };
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.MOVEMENT_SPEED, BASE_SPEED)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 1.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    private static boolean isPerkMushroom(ItemStack stack) {
        return stack.is(Kombucha.UNCOMMON_COMBUCHA_SHROOM.get());
    }

    private static boolean isRegenerationMushroom(ItemStack stack) {
        return stack.is(Kombucha.ENDER_COMBUCHA_SHROOM.get());
    }

    @Override
    protected void playEatingSound() {
        this.playSound(SoundEvents.PLAYER_BURP, 0.8F, 0.9F + this.random.nextFloat() * 0.2F);
    }

    private void rollPerk(Player player) {
        FriendlyKombuchaPerk perk = MUSHROOM_PERKS[this.random.nextInt(MUSHROOM_PERKS.length)];
        rollPerk(player, perk);
    }

    private void rollPerk(Player player, FriendlyKombuchaPerk perk) {
        int currentLevel = getPerkLevel(perk);
        if (currentLevel < perk.getMaxLevel()) {
            setPerkLevel(perk, currentLevel + 1);
            applyPerkAttributes();
            player.sendOverlayMessage(Component.translatable("kombucha.perk.received", this.getName(),
                    Component.translatable(perk.getDisplayNameKey()), currentLevel + 1)
                    .withStyle(ChatFormatting.GREEN));
        } else if (getLevel() < MAX_LEVEL) {
            addExperience(getExperienceToNextLevel());
            player.sendOverlayMessage(Component.translatable("kombucha.perk.level_up", this.getName(), getLevel())
                    .withStyle(ChatFormatting.GREEN));
        }
    }

    private void setPerkLevel(FriendlyKombuchaPerk perk, int level) {
        int clampedLevel = Math.max(0, Math.min(perk.getMaxLevel(), level));
        switch (perk) {
            case INCREASED_JUMP -> this.entityData.set(INCREASED_JUMP_PERK_LEVEL, clampedLevel);
            case FALL_IMMUNITY -> this.entityData.set(FALL_IMMUNITY_PERK_LEVEL, clampedLevel);
            case REGENERATION -> this.entityData.set(REGENERATION_PERK_LEVEL, clampedLevel);
        }
    }

    private void applyPerkAttributes() {
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(getPerkStepHeight());
        this.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).setBaseValue(
                getFallImmunityPerkLevel() > 0 ? 0.0D : 1.0D);
    }

    private static int clampUpgradeCount(int upgrades, int maximum) {
        return Math.max(0, Math.min(upgrades, maximum));
    }

    private static int clampLevel(int level) {
        return Math.max(1, Math.min(level, MAX_LEVEL));
    }
}
