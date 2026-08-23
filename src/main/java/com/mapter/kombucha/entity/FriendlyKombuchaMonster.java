package com.mapter.kombucha.entity;

import com.mapter.kombucha.Kombucha;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
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

public class FriendlyKombuchaMonster extends TamableAnimal implements RangedAttackMob {
    public static final int FEED_COOLDOWN_TICKS = 6000;
    public static final int EXPERIENCE_PER_KILL = 5;
    public static final int EXPERIENCE_PER_FEED = 2;
    private static final int BASE_LEVEL_EXPERIENCE = 10;
    private static final double BASE_HEALTH = 20.0D;
    private static final double BASE_SPEED = 0.25D;
    private static final double BASE_MELEE_DAMAGE = 6.0D;
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
    private int feedCooldown;
    private double rangedTargetX;
    private double rangedTargetY;
    private double rangedTargetZ;
    private float rangedPower;

    public FriendlyKombuchaMonster(EntityType<? extends FriendlyKombuchaMonster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CombuchaRangedAttackGoal(this, 1.0,
                this::getRangedAttackIntervalTicks, 12.0F));
        this.goalSelector.addGoal(2, new CombuchaMeleeAttackGoal(this, 1.0, true,
                this::getMeleeAttackIntervalTicks));
        this.goalSelector.addGoal(3, new FriendlyKombuchaFollowOwnerGoal(this, 1.0, 3.0F, 2.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new FriendlyKombuchaLookAtOwnerGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
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
                this.feedCooldown = FEED_COOLDOWN_TICKS;
                this.addExperience(EXPERIENCE_PER_FEED);
                this.level().broadcastEntityEvent(this, (byte) 7);
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
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.feedCooldown > 0) {
            this.feedCooldown--;
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
        return Math.max(MIN_PROJECTILE_SPEED, getRangedProjectileSpeed(power) - getProjectileSpeedUpgrades() * 0.05F);
    }

    public boolean canUpgradeProjectileSpeed() {
        float nextMin = getRangedProjectileSpeed(RANGED_PROJECTILE_MIN_POWER)
                - (getProjectileSpeedUpgrades() + 1) * 0.05F;
        return nextMin > MIN_PROJECTILE_SPEED;
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
        return Math.max(1, (int) Math.ceil(BASE_LEVEL_EXPERIENCE * Math.pow(1.1D, getLevel() - 1)));
    }

    public int getAvailableUpgradePoints() {
        return this.entityData.get(UPGRADE_POINTS);
    }

    public int getHealthUpgrades() {
        return this.entityData.get(HEALTH_UPGRADES);
    }

    public int getSpeedUpgrades() {
        return this.entityData.get(SPEED_UPGRADES);
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

    public float getRangedDamage() {
        return SlimeCombuchaProjectile.DAMAGE + getRangedDamageUpgrades() * 0.5F;
    }

    public int getMeleeAttackIntervalTicks() {
        return Math.max(1, MELEE_ATTACK_INTERVAL_TICKS - getMeleeSpeedUpgrades() * 2);
    }

    public int getRangedAttackIntervalTicks() {
        return Math.max(1, RANGED_ATTACK_INTERVAL_TICKS - getRangedSpeedUpgrades() * 2);
    }

    public void addExperience(int amount) {
        if (this.level().isClientSide() || amount <= 0) {
            return;
        }
        int experience = getExperience() + amount;
        int level = getLevel();
        int points = getAvailableUpgradePoints();
        while (experience >= getExperienceToNextLevel(level)) {
            experience -= getExperienceToNextLevel(level);
            level++;
            points++;
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
        if (stat == STAT_PROJECTILE_SPEED && !canUpgradeProjectileSpeed()) {
            return false;
        }
        this.entityData.set(accessor, this.entityData.get(accessor) + 1);
        this.entityData.set(UPGRADE_POINTS, getAvailableUpgradePoints() - 1);
        applyUpgradedAttributes();
        return true;
    }

    private void applyUpgradedAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BASE_HEALTH + getHealthUpgrades() * 2.0D);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED + getSpeedUpgrades() * 0.05D);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(BASE_MELEE_DAMAGE + getMeleeDamageUpgrades());
        this.setHealth(Math.min(this.getHealth(), this.getMaxHealth()));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(EXPERIENCE, input.getIntOr("KombuchaExperience", 0));
        this.entityData.set(LEVEL, Math.max(1, input.getIntOr("KombuchaLevel", 1)));
        this.entityData.set(UPGRADE_POINTS, input.getIntOr("KombuchaUpgradePoints", 0));
        this.entityData.set(HEALTH_UPGRADES, input.getIntOr("KombuchaHealthUpgrades", 0));
        this.entityData.set(SPEED_UPGRADES, input.getIntOr("KombuchaSpeedUpgrades", 0));
        this.entityData.set(MELEE_DAMAGE_UPGRADES, input.getIntOr("KombuchaMeleeDamageUpgrades", 0));
        this.entityData.set(RANGED_DAMAGE_UPGRADES, input.getIntOr("KombuchaRangedDamageUpgrades", 0));
        this.entityData.set(MELEE_SPEED_UPGRADES, input.getIntOr("KombuchaMeleeSpeedUpgrades", 0));
        this.entityData.set(RANGED_SPEED_UPGRADES, input.getIntOr("KombuchaRangedSpeedUpgrades", 0));
        this.entityData.set(PROJECTILE_SPEED_UPGRADES, input.getIntOr("KombuchaProjectileSpeedUpgrades", 0));
        this.feedCooldown = input.getIntOr("KombuchaFeedCooldown", 0);
        applyUpgradedAttributes();
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
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }
}
