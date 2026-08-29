package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
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

/**
 * A jar full of lava. Only the nether tea mix works here — it turns the
 * jar into a kombucha jar brewing nether kombucha.
 */
public class LavaJarBlock extends Block {

    public LavaJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        // empty bucket picks the lava up
        if (stack.is(Items.BUCKET)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, Kombucha.EMPTY_KOMBUCHA_JAR.get().defaultBlockState(), 3);
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, new ItemStack(Items.LAVA_BUCKET));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // a nether mushroom starts the nether brew
        if (MushroomType.fromStack(stack) == MushroomType.NETHER) {
            if (!level.isClientSide()) {
                BlockState kombuchaState = Kombucha.KOMBUCHA_JAR.get().defaultBlockState()
                        .setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.UNSEALED_LAVA_INFESTED)
                        .setValue(KombuchaJarBlock.FILL, KombuchaJarBlock.Fill.FULL);
                level.setBlock(pos, kombuchaState, 3);
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    be.setFermentationTicks(KombuchaConfig.TICKS_TO_INFESTED.get());
                    be.setFillsLeft(3);
                    be.setMushroomType(MushroomType.NETHER);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // the nether mix starts the brew
        if (TeaType.isNetherMix(stack)) {
            if (!level.isClientSide()) {
                BlockState kombuchaState = Kombucha.KOMBUCHA_JAR.get().defaultBlockState()
                        .setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.UNSEALED);
                level.setBlock(pos, kombuchaState, 3);
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    be.setTeaType(TeaType.NETHER);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // anything else: hint
        if (!level.isClientSide()) {
            player.sendOverlayMessage(
                    Component.translatable("kombucha.hint.add_nether_mix").withStyle(ChatFormatting.WHITE));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.sendOverlayMessage(
                    Component.translatable("kombucha.hint.add_nether_mix").withStyle(ChatFormatting.WHITE));
        }
        return InteractionResult.SUCCESS;
    }
}
