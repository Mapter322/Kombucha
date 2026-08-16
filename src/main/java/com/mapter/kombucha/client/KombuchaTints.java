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

    // identity tint — the lava texture is already coloured, it must not be tinted
    public static final int WHITE_COLOR = 0xFFFFFFFF;

    // fully transparent tint — hides an element (alpha is multiplied like RGB)
    public static final int INVISIBLE_COLOR = 0x00FFFFFF;

    // ----- Item tint sources (referenced from item model JSONs) -----

    private static final class TeaTypeItemTint implements ItemTintSource {
        private static final MapCodec<TeaTypeItemTint> CODEC = MapCodec.unit(new TeaTypeItemTint());

        @Override
        public int calculate(ItemStack stack, ClientLevel level, LivingEntity entity) {
            TeaType type = stack.getOrDefault(ModDataComponents.TEA_TYPE, TeaType.TEA);
            // nether kombucha brews in lava — the tea element is hidden, lava shows instead
            return type == TeaType.NETHER ? INVISIBLE_COLOR : type.getColor();
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

    private static final class LavaItemTint implements ItemTintSource {
        private static final MapCodec<LavaItemTint> CODEC = MapCodec.unit(new LavaItemTint());

        @Override
        public int calculate(ItemStack stack, ClientLevel level, LivingEntity entity) {
            return WHITE_COLOR;
        }

        @Override
        public MapCodec<? extends ItemTintSource> type() {
            return CODEC;
        }
    }

    // tint index 1 in the jar models: the lava element over the tea liquid.
    // visible only for nether kombucha, hidden for every other tea.
    private static final class LavaElementItemTint implements ItemTintSource {
        private static final MapCodec<LavaElementItemTint> CODEC = MapCodec.unit(new LavaElementItemTint());

        @Override
        public int calculate(ItemStack stack, ClientLevel level, LivingEntity entity) {
            TeaType type = stack.getOrDefault(ModDataComponents.TEA_TYPE, TeaType.TEA);
            return type == TeaType.NETHER ? WHITE_COLOR : INVISIBLE_COLOR;
        }

        @Override
        public MapCodec<? extends ItemTintSource> type() {
            return CODEC;
        }
    }

    // ----- Block tint sources (registered per block; list index = tintindex) -----

    // kombucha jar, tint index 0: the tea liquid.
    // water before the tea mix, tea colour from the block entity, lava for the lava states.
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
            // the lava texture is already coloured — leave it alone
            if (state.getValue(KombuchaJarBlock.JAR_TYPE) == KombuchaJarBlock.JarType.UNSEALED_LAVA_INFESTED) {
                return WHITE_COLOR;
            }
            if (getter.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                // nether kombucha brews in lava — hide the tea element, lava shows instead
                if (be.getTeaType() == TeaType.NETHER) {
                    return INVISIBLE_COLOR;
                }
                return be.getTeaType().getColor();
            }
            return TeaType.TEA.getColor();
        }

        @Override
        public Set<Property<?>> relevantProperties() {
            return Set.of(KombuchaJarBlock.JAR_TYPE);
        }
    }

    // kombucha jar, tint index 1: the lava element layered over the tea liquid.
    // visible only for nether kombucha, hidden for every other tea.
    private static final class JarLavaElementTint implements BlockTintSource {
        @Override
        public int color(BlockState state) {
            return INVISIBLE_COLOR;
        }

        @Override
        public int colorInWorld(BlockState state, BlockAndTintGetter getter, BlockPos pos) {
            if (getter.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                return be.getTeaType() == TeaType.NETHER ? WHITE_COLOR : INVISIBLE_COLOR;
            }
            return INVISIBLE_COLOR;
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

    // lava jar: the texture is already coloured, the tint is a no-op
    private static final class LavaJarTint implements BlockTintSource {
        @Override
        public int color(BlockState state) {
            return WHITE_COLOR;
        }

        @Override
        public int colorInWorld(BlockState state, BlockAndTintGetter getter, BlockPos pos) {
            return WHITE_COLOR;
        }

        @Override
        public Set<Property<?>> relevantProperties() {
            return Set.of();
        }
    }

    public static void registerBlockTints(RegisterColorHandlersEvent.BlockTintSources event) {
        // list index = tintindex: 0 is the tea liquid, 1 is the lava element
        event.register(List.of(new JarLiquidTint(), new JarLavaElementTint()), Kombucha.KOMBUCHA_JAR.get());
        event.register(List.of(new WaterJarTint()), Kombucha.WATER_JAR.get());
        event.register(List.of(new LavaJarTint()), Kombucha.LAVA_JAR.get());
    }

    public static void registerItemTints(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Identifier.fromNamespaceAndPath(Kombucha.MODID, "tea_type"), TeaTypeItemTint.CODEC);
        event.register(Identifier.fromNamespaceAndPath(Kombucha.MODID, "water"), WaterItemTint.CODEC);
        event.register(Identifier.fromNamespaceAndPath(Kombucha.MODID, "lava"), LavaItemTint.CODEC);
        event.register(Identifier.fromNamespaceAndPath(Kombucha.MODID, "lava_element"), LavaElementItemTint.CODEC);
    }
}
