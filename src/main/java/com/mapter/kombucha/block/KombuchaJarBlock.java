package com.mapter.kombucha.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class KombuchaJarBlock extends BaseEntityBlock {

    public enum JarType implements StringRepresentable {
        UNSEALED("unsealed"),
        SEALED("sealed"),
        INFESTED("infested");

        public static final Codec<JarType> CODEC = StringRepresentable.fromEnum(JarType::values);

        private final String name;

        JarType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public static final EnumProperty<JarType> JAR_TYPE = EnumProperty.create("jar_type", JarType.class);
    public static final MapCodec<KombuchaJarBlock> CODEC = simpleCodec(KombuchaJarBlock::new);

    public KombuchaJarBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(JAR_TYPE, JarType.UNSEALED));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KombuchaJarBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(JAR_TYPE);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        // Wool → seal the jar
        if (stack.is(ItemTags.WOOL) && state.getValue(JAR_TYPE) == JarType.UNSEALED) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(JAR_TYPE, JarType.SEALED), 3);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
