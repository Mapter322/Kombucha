package com.mapter.kombucha.loot;

import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlockEntity;
import com.mapter.kombucha.component.ModDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * Copies the jar's identity (jar_type from the block state, tea_type from the
 * block entity) onto the dropped item so a picked-up jar keeps its tea.
 */
public class CopyJarDataFunction extends LootItemConditionalFunction {
    public static final MapCodec<CopyJarDataFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, CopyJarDataFunction::new));

    private CopyJarDataFunction(List<LootItemCondition> predicates) {
        super(predicates);
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        BlockState state = context.getOptionalParameter(LootContextParams.BLOCK_STATE);
        if (state != null) {
            stack.set(ModDataComponents.JAR_TYPE, state.getValue(KombuchaJarBlock.JAR_TYPE));
        }
        if (context.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof KombuchaJarBlockEntity be) {
            stack.set(ModDataComponents.TEA_TYPE, be.getTeaType());
            if (be.getLivingShroomData() != null) {
                stack.set(ModDataComponents.LIVING_SHROOM_DATA, be.getLivingShroomData());
            }
        }
        return stack;
    }
}
