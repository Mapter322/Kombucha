package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.config.KombuchaConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class KombuchaJarBlockEntity extends BlockEntity {

    private TeaType teaType = TeaType.TEA;
    private int fermentationTicks = 0;

    public KombuchaJarBlockEntity(BlockPos pos, BlockState state) {
        super(Kombucha.KOMBUCHA_JAR_BE.get(), pos, state);
    }

    public TeaType getTeaType() {
        return teaType;
    }

    public int getFermentationTicks() {
        return fermentationTicks;
    }

    public void setFermentationTicks(int ticks) {
        this.fermentationTicks = ticks;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void setTeaType(TeaType teaType) {
        this.teaType = teaType;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, KombuchaJarBlockEntity be) {
        KombuchaJarBlock.JarType jarType = state.getValue(KombuchaJarBlock.JAR_TYPE);

        if (jarType != KombuchaJarBlock.JarType.SEALED && jarType != KombuchaJarBlock.JarType.INFESTED) {
            // Paused — unsealed jar, don't count ticks but preserve progress
            return;
        }

        if (!level.isClientSide()) {
            // Server side: count ticks and handle stage transitions
            be.fermentationTicks++;
            be.setChanged();

            int ticksPerStage = KombuchaConfig.TICKS_PER_STAGE.get();

            // Stage 1 - Stage 2: SEALED becomes INFESTED after ticksPerStage ticks
            if (be.fermentationTicks >= ticksPerStage && jarType == KombuchaJarBlock.JarType.SEALED) {
                level.setBlock(pos, state.setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.INFESTED), 3);
            }

            // Bubbles for stages 1 and 2
            boolean stage3 = jarType == KombuchaJarBlock.JarType.INFESTED
                    && be.fermentationTicks >= 2 * ticksPerStage;

            if (!stage3 && level.getGameTime() % 15 == 0) {
                // Stages 1 & 2: vanilla bubbles
                double x = pos.getX() + 0.3 + level.getRandom().nextDouble() * 0.4;
                double y = pos.getY() + 0.9;
                double z = pos.getZ() + 0.3 + level.getRandom().nextDouble() * 0.4;
                ((ServerLevel) level).sendParticles(ParticleTypes.BUBBLE, x, y, z, 1, 0, 0.05, 0, 0.1);
            }

            // Stage 3: witch particles
            if (stage3 && level.getGameTime() % 10 == 0) {
                double x = pos.getX() + 0.3 + level.getRandom().nextDouble() * 0.4;
                double y = pos.getY() + 1.0;
                double z = pos.getZ() + 0.3 + level.getRandom().nextDouble() * 0.4;
                ((ServerLevel) level).sendParticles(ParticleTypes.WITCH, x, y, z, 1, 0, 0.02, 0, 0.02);
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("tea_type", TeaType.CODEC).ifPresent(type -> this.teaType = type);
        input.read("fermentation_ticks", Codec.INT).ifPresent(ticks -> this.fermentationTicks = ticks);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("tea_type", TeaType.CODEC, teaType);
        output.store("fermentation_ticks", Codec.INT, fermentationTicks);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putString("tea_type", teaType.getSerializedName());
        tag.putInt("fermentation_ticks", fermentationTicks);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
