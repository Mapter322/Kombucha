package com.mapter.kombucha.item;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.block.TeaType;
import com.mapter.kombucha.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class KombuchaDrinkItem extends Item {
    private final TeaType teaType;

    public KombuchaDrinkItem(TeaType teaType, Properties properties) {
        super(properties);
        this.teaType = teaType;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(
                    ModEffects.COMBUCHA_FRIEND,
                    friendDuration(),
                    0));
            if (teaType == TeaType.GOLDEN) {
                player.addEffect(new MobEffectInstance(
                        ModEffects.COMBUCHA_IDOL,
                        5 * 60 * 20,
                        0));
                if (level instanceof ServerLevel serverLevel) {
                    summonGoldenCombucha(player, serverLevel);
                }
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (teaType == TeaType.GOLDEN) {
                player.heal(player.getMaxHealth());
                return super.finishUsingItem(stack, level, entity);
            }

            RandomSource random = level.getRandom();

            if (teaType == TeaType.NETHER || teaType == TeaType.ENDER) {
                player.heal(random.nextIntBetweenInclusive(3, 6) * 2.0F);
                if (teaType == TeaType.NETHER) {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.FIRE_RESISTANCE,
                            8 * 60 * 20,
                            0));
                }
                return super.finishUsingItem(stack, level, entity);
            }

            player.heal(random.nextIntBetweenInclusive(minHealingHearts(), maxHealingHearts()) * 2.0F);

            switch (teaType) {
                case TEA -> player.addEffect(new MobEffectInstance(
                        MobEffects.SPEED,
                        random.nextIntBetweenInclusive(1, 3) * 60 * 20,
                        0));
                case APPLE -> {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.SPEED,
                            randomDuration(random, 2, 5),
                            1));
                    applyAppleEffect(player, random);
                }
                case MELON -> {
                    removeNegativeEffects(player);
                    applyMelonEffect(player, random);
                }
                default -> throw new IllegalStateException("Unexpected implemented drink: " + teaType);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    private int minHealingHearts() {
        return teaType == TeaType.TEA ? 1 : 2;
    }

    private int maxHealingHearts() {
        return teaType == TeaType.TEA ? 3 : 4;
    }

    private int friendDuration() {
        return switch (teaType) {
            case APPLE, MELON -> 3 * 60 * 20;
            case NETHER, ENDER, GOLDEN -> 5 * 60 * 20;
            default -> 2 * 60 * 20;
        };
    }

    private static void summonGoldenCombucha(Player player, ServerLevel level) {
        BlockPos spawnPos = player.blockPosition().relative(player.getDirection(), 3);
        double x = spawnPos.getX() + 0.5D;
        double y = spawnPos.getY() + 0.8D;
        double z = spawnPos.getZ() + 0.5D;

        level.sendParticles(ParticleTypes.END_ROD, x, y, z, 24, 0.45D, 0.7D, 0.45D, 0.08D);
        level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 16, 0.35D, 0.5D, 0.35D, 0.1D);
        level.playSound(null, x, y, z,
                SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.HOSTILE, 0.8F, 1.2F);

        Kombucha.SPOILED_COMBUCHA_MONSTER.get().spawn(level, spawnPos, EntitySpawnReason.MOB_SUMMONED);

        level.sendParticles(ParticleTypes.POOF, x, y, z, 12, 0.45D, 0.5D, 0.45D, 0.08D);
        level.playSound(null, x, y, z,
                SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 0.9F, 1.0F);
    }

    private static void applyAppleEffect(Player player, RandomSource random) {
        int roll = random.nextInt(100);
        if (roll < 20) {
            player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, randomDuration(random, 2, 5), 1));
        } else if (roll < 60) {
            player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, randomDuration(random, 2, 5), 0));
        } else {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, randomDuration(random, 2, 3), 0));
        }
    }

    private static void applyMelonEffect(Player player, RandomSource random) {
        int roll = random.nextInt(3);
        switch (roll) {
            case 0 -> player.addEffect(new MobEffectInstance(
                    MobEffects.ABSORPTION,
                    randomDuration(random, 2, 5),
                    random.nextInt(2)));
            case 1 -> player.addEffect(new MobEffectInstance(
                    ModEffects.FALL_IMMUNITY,
                    randomDuration(random, 2, 5),
                    0));
            case 2 -> player.addEffect(new MobEffectInstance(
                    MobEffects.WATER_BREATHING,
                    randomDuration(random, 2, 3),
                    0));
            default -> throw new IllegalStateException("Unexpected melon effect roll: " + roll);
        }
    }

    private static void removeNegativeEffects(Player player) {
        player.getActiveEffects().stream()
                .filter(effect -> !effect.getEffect().value().isBeneficial())
                .map(MobEffectInstance::getEffect)
                .toList()
                .forEach(player::removeEffect);
    }

    private static int randomDuration(RandomSource random, int minMinutes, int maxMinutes) {
        return random.nextIntBetweenInclusive(minMinutes, maxMinutes) * 60 * 20;
    }
}
