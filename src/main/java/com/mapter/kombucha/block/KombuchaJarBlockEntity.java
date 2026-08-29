package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.component.LivingShroomData;
import com.mapter.kombucha.config.KombuchaConfig;
import com.mapter.kombucha.entity.CaveKombuchaMonster;
import com.mapter.kombucha.entity.EnderKombuchaMonster;
import com.mapter.kombucha.entity.NetherKombuchaMonster;
import com.mapter.kombucha.entity.SpoiledKombuchaMonster;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.entity.monster.Monster;
import org.jspecify.annotations.Nullable;

public class KombuchaJarBlockEntity extends BlockEntity {

    private TeaType teaType = TeaType.TEA;
    private MushroomType mushroomType = MushroomType.REGULAR;
    private int fermentationTicks = 0;
    private int fillsLeft = 3;
    private @Nullable LivingShroomData livingShroomData;

    public KombuchaJarBlockEntity(BlockPos pos, BlockState state) {
        super(Kombucha.KOMBUCHA_JAR_BE.get(), pos, state);
    }

    public TeaType getTeaType() {
        return teaType;
    }

    public MushroomType getMushroomType() {
        return mushroomType;
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

    public void setMushroomType(MushroomType mushroomType) {
        this.mushroomType = mushroomType;
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

        if (jarType != KombuchaJarBlock.JarType.SEALED
                && jarType != KombuchaJarBlock.JarType.INFESTED
                && jarType != KombuchaJarBlock.JarType.SPOILED) {
            // unsealed — paused, but a ready jar keeps the same particles as a closed one
            if (!level.isClientSide()
                    && jarType == KombuchaJarBlock.JarType.UNSEALED_INFESTED
                    && FermentationStage.of(be.fermentationTicks,
                    KombuchaConfig.TICKS_TO_INFESTED.get(),
                    KombuchaConfig.TICKS_TO_FERMENTED.get(),
                    KombuchaConfig.TICKS_TO_SPOILED.get(),
                    KombuchaConfig.TICKS_TO_MONSTER.get())
                    == FermentationStage.THREE
                    && level.getGameTime() % 10 == 0) {
                sendReadyParticles(level, pos);
            }
            return;
        }

        if (!level.isClientSide()) {
            int ticksToInfested = KombuchaConfig.TICKS_TO_INFESTED.get();
            if (jarType == KombuchaJarBlock.JarType.SEALED
                    && be.fermentationTicks < ticksToInfested
                    && !isUnderground(level, pos)) {
                return;
            }

            be.fermentationTicks++;
            be.setChanged();

            int ticksToFermented = KombuchaConfig.TICKS_TO_FERMENTED.get();
            int ticksToSpoiled = KombuchaConfig.TICKS_TO_SPOILED.get();
            int ticksToMonster = KombuchaConfig.TICKS_TO_MONSTER.get();
            FermentationStage stage = FermentationStage.of(be.fermentationTicks, ticksToInfested,
                    ticksToFermented, ticksToSpoiled, ticksToMonster);

            boolean freshAir = level.canSeeSky(pos.above());
            if (jarType == KombuchaJarBlock.JarType.SPOILED && stage == FermentationStage.THREE) {
                stage = FermentationStage.SPOILED;
            } else if (jarType == KombuchaJarBlock.JarType.INFESTED
                    && stage == FermentationStage.THREE && !freshAir) {
                // A mushroom that is not moved outside spoils instead of maturing.
                stage = FermentationStage.SPOILED;
            }

            // the mushroom appears: a sealed jar turns infested in a cave
            if (stage != FermentationStage.ONE && jarType == KombuchaJarBlock.JarType.SEALED) {
                level.setBlock(pos, state.setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.INFESTED), 3);
            }

            // the mushroom spoils: an infested jar enters the final stage
            if (stage == FermentationStage.SPOILED && jarType == KombuchaJarBlock.JarType.INFESTED) {
                level.setBlock(pos, state.setValue(KombuchaJarBlock.JAR_TYPE, KombuchaJarBlock.JarType.SPOILED), 3);

                if (be.livingShroomData != null) {
                    // the shroom survives the failed brew and drops back out with everything it carries
                    Block.popResource(level, pos, be.livingShroomData.toItemStack());
                    be.setLivingShroomData(null);
                    be.setFillsLeft(0);
                    level.setBlock(pos, Kombucha.EMPTY_KOMBUCHA_JAR.get().defaultBlockState(), 3);
                    return;
                }
            }

            // the spoiled kombucha breaks the jar as it emerges
            if (stage == FermentationStage.MONSTER
                    && (jarType == KombuchaJarBlock.JarType.INFESTED
                    || jarType == KombuchaJarBlock.JarType.SPOILED)) {
                Monster monster = switch (be.mushroomType) {
                    case REGULAR -> new SpoiledKombuchaMonster(
                            Kombucha.SPOILED_KOMBUCHA_MONSTER.get(), level);
                    case UNCOMMON -> new CaveKombuchaMonster(
                            Kombucha.CAVE_KOMBUCHA_MONSTER.get(), level);
                    case NETHER -> new NetherKombuchaMonster(
                            Kombucha.NETHER_KOMBUCHA_MONSTER.get(), level);
                    case ENDER -> new EnderKombuchaMonster(
                            Kombucha.ENDER_KOMBUCHA_MONSTER.get(), level);
                };
                monster.setPos(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
                monster.setYRot(level.getRandom().nextFloat() * 360.0F);
                level.addFreshEntity(monster);
                level.destroyBlock(pos, true);
                level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                            pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D,
                            40, 0.3D, 0.3D, 0.3D, 0.1D);
                }
                return;
            }

            // bubbles while it's growing, witch particles once it's ready
            boolean growing = stage == FermentationStage.ONE || stage == FermentationStage.TWO;
            boolean matured = stage == FermentationStage.THREE;

            if (growing && level.getGameTime() % 15 == 0) {
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

    public static boolean isUnderground(Level level, BlockPos pos) {
        return pos.getY() < 40
                && level.getMaxLocalRawBrightness(pos) == 0
                && !level.canSeeSky(pos.above())
                && !level.getFluidState(pos).is(Fluids.WATER);
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
        input.read("mushroom_type", MushroomType.CODEC).ifPresent(type -> this.mushroomType = type);
        input.read("fermentation_ticks", Codec.INT).ifPresent(ticks -> this.fermentationTicks = ticks);
        input.read("fills_left", Codec.INT).ifPresent(fills -> this.fillsLeft = fills);
        input.read("living_shroom", LivingShroomData.CODEC).ifPresent(data -> this.livingShroomData = data);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("tea_type", TeaType.CODEC, teaType);
        output.store("mushroom_type", MushroomType.CODEC, mushroomType);
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
        tag.putString("mushroom_type", mushroomType.getSerializedName());
        tag.putInt("fermentation_ticks", fermentationTicks);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
