package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.component.LivingShroomData;
import com.mapter.kombucha.config.KombuchaConfig;
import com.mapter.kombucha.entity.SpoiledCombuchaMonster;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class KombuchaJarBlockEntity extends BlockEntity {

    private TeaType teaType = TeaType.TEA;
    private int fermentationTicks = 0;
    private int fillsLeft = 3;
    private @Nullable LivingShroomData livingShroomData;

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

    public int getFillsLeft() {
        return fillsLeft;
    }

    public void decrementFills() {
        this.fillsLeft--;
        setChanged();
    }

    public void setFillsLeft(int fillsLeft) {
        this.fillsLeft = fillsLeft;
        setChanged();
    }

    public void setTeaType(TeaType teaType) {
        this.teaType = teaType;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public @Nullable LivingShroomData getLivingShroomData() {
        return livingShroomData;
    }

    public boolean hasLivingShroom() {
        return livingShroomData != null;
    }

    public void setLivingShroomData(@Nullable LivingShroomData data) {
        this.livingShroomData = data;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void onDataPacket(Connection connection, ValueInput input) {
        super.onDataPacket(connection, input);
        if (level != null && level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, KombuchaJarBlockEntity be) {
        KombuchaJarBlock.JarType jarType = state.getValue(KombuchaJarBlock.JAR_TYPE);

        if (jarType != KombuchaJarBlock.JarType.SEALED && jarType != KombuchaJarBlock.JarType.INFESTED) {
            // unsealed — paused, but a ready jar keeps the same particles as a closed one
            if (!level.isClientSide()
                    && jarType == KombuchaJarBlock.JarType.UNSEALED_INFESTED
                    && FermentationStage.of(be.fermentationTicks,
                    KombuchaConfig.TICKS_TO_INFESTED.get(),
                    KombuchaConfig.TICKS_TO_FERMENTED.get(),
                    KombuchaConfig.TICKS_TO_SPOILED.get())
                    == FermentationStage.THREE
                    && level.getGameTime() % 10 == 0) {
                sendReadyParticles(level, pos);
            }
            return;
        }

        if (!level.isClientSide()) {
            be.fermentationTicks++;
            be.setChanged();

            int ticksToInfested = KombuchaConfig.TICKS_TO_INFESTED.get();
            int ticksToFermented = KombuchaConfig.TICKS_TO_FERMENTED.get();
            int ticksToSpoiled = KombuchaConfig.TICKS_TO_SPOILED.get();
            FermentationStage stage = FermentationStage.of(be.fermentationTicks, ticksToInfested,
                    ticksToFermented, ticksToSpoiled);

            // the mushroom appears: a sealed jar turns infested
            if (stage != FermentationStage.ONE && jarType == KombuchaJarBlock.JarType.SEALED) {
                level.setBlock(pos, state.setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.INFESTED), 3);
            }

            // the mushroom dies: an infested jar spoils
            if (stage == FermentationStage.SPOILED && jarType == KombuchaJarBlock.JarType.INFESTED) {
                level.setBlock(pos, state.setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.SPOILED), 3);

                if (be.livingShroomData != null) {
                    // the shroom survives the failed brew and drops back out with everything it carries
                    Block.popResource(level, pos, be.livingShroomData.toItemStack());
                    be.setLivingShroomData(null);
                } else if (be.teaType != TeaType.NETHER
                        && !level.canSeeSky(pos)
                        && level.getMaxLocalRawBrightness(pos) <= 7
                        && level.getRandom().nextFloat() < 0.10F) {
                    SpoiledCombuchaMonster monster = new SpoiledCombuchaMonster(Kombucha.SPOILED_COMBUCHA_MONSTER.get(), level);
                    monster.setPos(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
                    monster.setYRot(level.getRandom().nextFloat() * 360.0F);
                    level.addFreshEntity(monster);
                }
            }

            // bubbles while it's growing, witch particles once it's ready
            boolean matured = stage == FermentationStage.THREE;

            if (!matured && level.getGameTime() % 15 == 0) {
                double x = pos.getX() + 0.3 + level.getRandom().nextDouble() * 0.4;
                double y = pos.getY() + 0.9;
                double z = pos.getZ() + 0.3 + level.getRandom().nextDouble() * 0.4;
                ((ServerLevel) level).sendParticles(ParticleTypes.BUBBLE, x, y, z, 1, 0, 0.05, 0, 0.1);
            }

            if (matured && level.getGameTime() % 10 == 0) {
                sendReadyParticles(level, pos);
            }
        }
    }

    private static void sendReadyParticles(Level level, BlockPos pos) {
        double x = pos.getX() + 0.3 + level.getRandom().nextDouble() * 0.4;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.3 + level.getRandom().nextDouble() * 0.4;
        ((ServerLevel) level).sendParticles(ParticleTypes.WITCH, x, y, z, 1, 0, 0.02, 0, 0.02);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("tea_type", TeaType.CODEC).ifPresent(type -> this.teaType = type);
        input.read("fermentation_ticks", Codec.INT).ifPresent(ticks -> this.fermentationTicks = ticks);
        input.read("fills_left", Codec.INT).ifPresent(fills -> this.fillsLeft = fills);
        input.read("living_shroom", LivingShroomData.CODEC).ifPresent(data -> this.livingShroomData = data);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("tea_type", TeaType.CODEC, teaType);
        output.store("fermentation_ticks", Codec.INT, fermentationTicks);
        output.store("fills_left", Codec.INT, fillsLeft);
        if (this.livingShroomData != null) {
            output.store("living_shroom", LivingShroomData.CODEC, this.livingShroomData);
        }
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
