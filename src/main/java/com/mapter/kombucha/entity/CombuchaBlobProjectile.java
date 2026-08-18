package com.mapter.kombucha.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class CombuchaBlobProjectile extends ThrowableItemProjectile {
    protected CombuchaBlobProjectile(EntityType<? extends CombuchaBlobProjectile> type, Level level) {
        super(type, level);
    }

    protected CombuchaBlobProjectile(EntityType<? extends CombuchaBlobProjectile> type, LivingEntity owner, Level level, Item item) {
        super(type, owner, level, new ItemStack(item));
    }

    protected abstract float getDamage();

    protected boolean setsTargetOnFire() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity target = hitResult.getEntity();
        if (!(target instanceof LivingEntity livingTarget) || target == this.getOwner()) {
            return;
        }

        Entity owner = this.getOwner();
        LivingEntity livingOwner = owner instanceof LivingEntity ? (LivingEntity) owner : null;
        if (livingTarget.hurtOrSimulate(this.damageSources().mobProjectile(this, livingOwner), this.getDamage())
                && this.setsTargetOnFire()) {
            livingTarget.setRemainingFireTicks(60);
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(this.getItem()));
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }
}
