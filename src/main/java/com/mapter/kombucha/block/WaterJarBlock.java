package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import net.minecraft.core.BlockPos;
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
        // Empty bucket → pick up water
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

        // Tea mix → kombucha jar
        TeaType teaType = getTeaType(stack);
        if (teaType != null) {
            if (!level.isClientSide()) {
                BlockState kombuchaState = Kombucha.KOMBUCHA_JAR.get().defaultBlockState()
                        .setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.UNSEALED);
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

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private TeaType getTeaType(ItemStack stack) {
        if (stack.is(Kombucha.TEA_MIX.get())) return TeaType.TEA;
        if (stack.is(Kombucha.APPLE_TEA_MIX.get())) return TeaType.APPLE;
        if (stack.is(Kombucha.MELON_TEA_MIX.get())) return TeaType.MELON;
        if (stack.is(Kombucha.NETHER_TEA_MIX.get())) return TeaType.NETHER;
        if (stack.is(Kombucha.ENDER_TEA_MIX.get())) return TeaType.ENDER;
        if (stack.is(Kombucha.GOLDEN_TEA_MIX.get())) return TeaType.GOLDEN;
        return null;
    }
}
