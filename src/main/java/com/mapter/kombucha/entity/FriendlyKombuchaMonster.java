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
import net.minecraft.world.entity.LivingEntity;
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

public class FriendlyKombuchaMonster extends TamableAnimal implements RangedAttackMob {
    private static final EntityDataAccessor<Integer> SHOOT_TIME =
            SynchedEntityData.defineId(FriendlyKombuchaMonster.class, EntityDataSerializers.INT);
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
        this.goalSelector.addGoal(1, new CombuchaRangedAttackGoal(this, 1.0, 45, 12.0F));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
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
            }
            return InteractionResult.SUCCESS;
        }
        if (!this.isTame()) {
            if (!this.level().isClientSide()) {
                player.sendOverlayMessage(Component.translatable("kombucha.hint.tame"));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
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
        SlimeCombuchaProjectile projectile = new SlimeCombuchaProjectile(serverLevel, this);
        Projectile.spawnProjectileUsingShoot(projectile, serverLevel, new ItemStack(Items.SLIME_BALL),
                xd, this.rangedTargetY + yo - projectile.getY(), zd,
                0.8F + this.rangedPower * 0.2667F, 4.0F);
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
