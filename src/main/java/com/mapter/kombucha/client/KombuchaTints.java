package com.mapter.kombucha.client;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlockEntity;
import com.mapter.kombucha.block.TeaType;
import com.mapter.kombucha.component.ModDataComponents;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;
import java.util.Set;

/**
 * Liquid colour for the jar. The liquid texture is white with a tintindex;
 * in-world the colour comes from the block entity's tea type, in the item
 * from the {@code kombucha:tea_type} component (defaults to TEA).
 */
public class KombuchaTints {

    // water blue (sampled from the old water swatch)
    public static final int WATER_COLOR = 0xFF3F76E4;

    // ----- Item tint sources (referenced from item model JSONs) -----

    private static final class TeaTypeItemTint implements ItemTintSource {
        private static final MapCodec<TeaTypeItemTint> CODEC = MapCodec.unit(new TeaTypeItemTint());

        @Override
        public int calculate(ItemStack stack, ClientLevel level, LivingEntity entity) {
            return stack.getOrDefault(ModDataComponents.TEA_TYPE, TeaType.TEA).getColor();
        }

        @Override
        public MapCodec<? extends ItemTintSource> type() {
            return CODEC;
        }
    }

    private static final class WaterItemTint implements ItemTintSource {
        private static final MapCodec<WaterItemTint> CODEC = MapCodec.unit(new WaterItemTint());

        @Override
        public int calculate(ItemStack stack, ClientLevel level, LivingEntity entity) {
            return WATER_COLOR;
        }

        @Override
        public MapCodec<? extends ItemTintSource> type() {
            return CODEC;
        }
    }

    // ----- Block tint sources (registered per block; list index = tintindex) -----

    // kombucha jar: water before the tea mix, otherwise the tea type from the block entity
    private static final class JarLiquidTint implements BlockTintSource {
        @Override
        public int color(BlockState state) {
            return TeaType.TEA.getColor();
        }

        @Override
        public int colorInWorld(BlockState state, BlockAndTintGetter getter, BlockPos pos) {
            if (state.getValue(KombuchaJarBlock.JAR_TYPE) == KombuchaJarBlock.JarType.UNSEALED_WATER_INFESTED) {
                return WATER_COLOR;
            }
            if (getter.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                return be.getTeaType().getColor();
            }
            return TeaType.TEA.getColor();
        }

        @Override
        public Set<Property<?>> relevantProperties() {
            return Set.of(KombuchaJarBlock.JAR_TYPE);
        }
    }

    // plain water jar: always water blue
    private static final class WaterJarTint implements BlockTintSource {
        @Override
        public int color(BlockState state) {
            return WATER_COLOR;
        }

        @Override
        public int colorInWorld(BlockState state, BlockAndTintGetter getter, BlockPos pos) {
            return WATER_COLOR;
        }

        @Override
        public Set<Property<?>> relevantProperties() {
            return Set.of();
        }
    }

    public static void registerBlockTints(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(new JarLiquidTint()), Kombucha.KOMBUCHA_JAR.get());
        event.register(List.of(new WaterJarTint()), Kombucha.WATER_JAR.get());
    }

    public static void registerItemTints(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Identifier.fromNamespaceAndPath(Kombucha.MODID, "tea_type"), TeaTypeItemTint.CODEC);
        event.register(Identifier.fromNamespaceAndPath(Kombucha.MODID, "water"), WaterItemTint.CODEC);
    }
}
