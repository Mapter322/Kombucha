package com.mapter.kombucha.item;

import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlockEntity;
import com.mapter.kombucha.block.MushroomType;
import com.mapter.kombucha.block.TeaType;
import com.mapter.kombucha.component.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class KombuchaJarItem extends BlockItem {

    public KombuchaJarItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        KombuchaJarBlock.JarType type = context.getItemInHand()
                .getOrDefault(ModDataComponents.JAR_TYPE, KombuchaJarBlock.JarType.UNSEALED);
        TeaType teaType = context.getItemInHand().getOrDefault(ModDataComponents.TEA_TYPE, TeaType.TEA);
        return state.setValue(KombuchaJarBlock.JAR_TYPE, type)
                .setValue(KombuchaJarBlock.LAVA, teaType == TeaType.NETHER);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack stack, BlockState placedState) {
        boolean updated = super.updateCustomBlockEntityTag(pos, level, player, stack, placedState);
        if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
            be.setTeaType(stack.getOrDefault(ModDataComponents.TEA_TYPE, TeaType.TEA));
            be.setMushroomType(stack.getOrDefault(ModDataComponents.MUSHROOM_TYPE, MushroomType.REGULAR));
            be.setLivingShroomData(stack.get(ModDataComponents.LIVING_SHROOM_DATA));
            return true;
        }
        return updated;
    }
}
