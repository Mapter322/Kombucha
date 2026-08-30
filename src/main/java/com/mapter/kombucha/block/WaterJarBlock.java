package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.component.LivingShroomData;
import com.mapter.kombucha.component.ModDataComponents;
import com.mapter.kombucha.config.KombuchaConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WaterJarBlock extends Block {

    public WaterJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        // empty bucket picks the water up
        if (stack.is(Items.BUCKET)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, Kombucha.EMPTY_KOMBUCHA_JAR.get().defaultBlockState(), 3);
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // a water-compatible starter mushroom puts the jar at stage 2, waiting for tea mix
        MushroomType mushroomType = MushroomType.fromStack(stack);
        if (mushroomType != null && mushroomType != MushroomType.NETHER) {
            if (!level.isClientSide()) {
                BlockState kombuchaState = Kombucha.KOMBUCHA_JAR.get().defaultBlockState()
                        .setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.UNSEALED_WATER_INFESTED)
                        .setValue(KombuchaJarBlock.FILL, KombuchaJarBlock.Fill.FULL)
                        .setValue(KombuchaJarBlock.LAVA, false);
                level.setBlock(pos, kombuchaState, 3);
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    be.setFermentationTicks(KombuchaConfig.TICKS_TO_INFESTED.get());
                    be.setFillsLeft(3);
                    be.setMushroomType(mushroomType);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // a living shroom carries a dead friendly kombucha — the jar can grow it back
        if (stack.is(Kombucha.LIVING_KOMBUCHA_SHROOM.get())) {
            if (!level.isClientSide()) {
                BlockState kombuchaState = Kombucha.KOMBUCHA_JAR.get().defaultBlockState()
                        .setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.UNSEALED_WATER_INFESTED)
                        .setValue(KombuchaJarBlock.FILL, KombuchaJarBlock.Fill.FULL)
                        .setValue(KombuchaJarBlock.LAVA, false);
                level.setBlock(pos, kombuchaState, 3);
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    be.setFermentationTicks(KombuchaConfig.TICKS_TO_INFESTED.get());
                    be.setFillsLeft(3);
                    be.setLivingShroomData(stack.getOrDefault(ModDataComponents.LIVING_SHROOM_DATA, LivingShroomData.DEFAULT));
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // tea mix starts the brew
        TeaType teaType = TeaType.fromStack(stack);
        if (teaType != null) {
            // the nether mix needs a lava jar instead
            if (teaType == TeaType.NETHER) {
                if (!level.isClientSide()) {
                    player.sendOverlayMessage(
                            Component.translatable("kombucha.hint.nether_needs_lava").withStyle(ChatFormatting.WHITE));
                }
                return InteractionResult.SUCCESS;
            }
            if (!level.isClientSide()) {
                BlockState kombuchaState = Kombucha.KOMBUCHA_JAR.get().defaultBlockState()
                        .setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.UNSEALED)
                        .setValue(KombuchaJarBlock.LAVA, false);
                level.setBlock(pos, kombuchaState, 3);
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    be.setTeaType(teaType);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // anything else: hint
        if (!level.isClientSide()) {
            player.sendOverlayMessage(
                    Component.translatable("kombucha.hint.add_tea_or_shroom").withStyle(ChatFormatting.WHITE));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.sendOverlayMessage(
                    Component.translatable("kombucha.hint.add_tea_or_shroom").withStyle(ChatFormatting.WHITE));
        }
        return InteractionResult.SUCCESS;
    }
}
