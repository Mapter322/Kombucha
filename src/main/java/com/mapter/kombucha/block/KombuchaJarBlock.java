package com.mapter.kombucha.block;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class KombuchaJarBlock extends Block {

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

    public KombuchaJarBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(JAR_TYPE, JarType.UNSEALED));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(JAR_TYPE);
    }
}
