package com.mapter.kombucha.loot;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlockEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

//drops the jar's mushroom separately while the jar itself is always empty
public class DropJarContentsFunction extends LootItemConditionalFunction {
    public static final MapCodec<DropJarContentsFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, DropJarContentsFunction::new));

    private DropJarContentsFunction(List<LootItemCondition> predicates) {
        super(predicates);
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        BlockState state = context.getOptionalParameter(LootContextParams.BLOCK_STATE);
        if (state != null && hasMushroom(state)
                && context.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof KombuchaJarBlockEntity be) {
            ItemStack mushroom = be.hasLivingShroom()
                    ? be.getLivingShroomData().toItemStack()
                    : new ItemStack(Kombucha.KOMBUCHA_SHROOM.get());
            Block.popResource(context.getLevel(), be.getBlockPos(), mushroom);
        }
        return stack;
    }

    private static boolean hasMushroom(BlockState state) {
        if (!state.hasProperty(KombuchaJarBlock.JAR_TYPE)) {
            return false;
        }
        KombuchaJarBlock.JarType jarType = state.getValue(KombuchaJarBlock.JAR_TYPE);
        return jarType == KombuchaJarBlock.JarType.INFESTED
                || jarType == KombuchaJarBlock.JarType.UNSEALED_INFESTED
                || jarType == KombuchaJarBlock.JarType.UNSEALED_WATER_INFESTED
                || jarType == KombuchaJarBlock.JarType.UNSEALED_LAVA_INFESTED;
    }
}
