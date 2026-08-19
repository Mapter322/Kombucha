package com.mapter.kombucha.entity;

import com.mapter.kombucha.Kombucha;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

public class CaveCombuchaMonster extends Monster implements RangedAttackMob {
    private static final EntityDataAccessor<Integer> SHOOT_TIME =
            SynchedEntityData.defineId(CaveCombuchaMonster.class, EntityDataSerializers.INT);
    private double rangedTargetX;
    private double rangedTargetY;
    private double rangedTargetZ;
    private float rangedPower;

    public CaveCombuchaMonster(EntityType<? extends CaveCombuchaMonster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CombuchaRangedAttackGoal(this, 1.0, 45, 12.0F));
        this.goalSelector.addGoal(2, new CombuchaFollowPlayerGoal(this, 1.0, 3.0, 16.0));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new CombuchaHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CombuchaTargetGoal(this));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.SLIME_SQUISH, 0.4F, 0.9F + this.random.nextFloat() * 0.2F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(SHOOT_TIME, 0);
    }

    @Override
    public void tick() {
        super.tick();
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
        ItemStack itemStack = new ItemStack(Items.SLIME_BALL);
        SlimeCombuchaProjectile projectile = new SlimeCombuchaProjectile(serverLevel, this);
        Projectile.spawnProjectileUsingShoot(projectile, serverLevel, itemStack,
                xd, this.rangedTargetY + yo - projectile.getY(), zd, 0.8F + this.rangedPower * 0.2667F, 4.0F);
        this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, 0.8F + this.random.nextFloat() * 0.2F);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(level, Kombucha.UNCOMMON_COMBUCHA_SHROOM.get());
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }
}
