package com.mapter.kombucha.item;

import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.component.ModDataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
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
        return state.setValue(KombuchaJarBlock.JAR_TYPE, type);
    }
}
