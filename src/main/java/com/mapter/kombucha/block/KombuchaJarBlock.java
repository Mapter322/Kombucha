package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.config.KombuchaConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                    BlockEntityType<T> type) {
        if (type == Kombucha.KOMBUCHA_JAR_BE.get()) {
            return (BlockEntityTicker<T>) (BlockEntityTicker<KombuchaJarBlockEntity>) KombuchaJarBlockEntity::tick;
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(JAR_TYPE);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        JarType jarType = state.getValue(JAR_TYPE);

        // Wool - seal the jar
        if (stack.is(ItemTags.WOOL) && jarType == JarType.UNSEALED) {
            if (!level.isClientSide()) {
                sealJar(level, pos, state, player, stack);
            }
            return InteractionResult.SUCCESS;
        }

        // Empty kombucha bottle on ready jar - fill with kombucha
        if (stack.is(Kombucha.EMPTY_KOMBUCHA_BOTTLE.get()) && jarType == JarType.INFESTED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    fillBottle(level, pos, state, player, stack, be);
                }
            }
            return InteractionResult.SUCCESS;
        }

        // Shift+RMB - remove lid
        if (player.isShiftKeyDown() && (jarType == JarType.SEALED || jarType == JarType.INFESTED)) {
            if (!level.isClientSide()) {
                unsealJar(level, pos, state, player);
            }
            return InteractionResult.SUCCESS;
        }

        // Normal RMB on sealed/infested - show progress
        if (jarType == JarType.SEALED || jarType == JarType.INFESTED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    showProgress(player, jarType, be.getFermentationTicks());
                }
            }
            return InteractionResult.SUCCESS;
        }

        // Non-wool item on UNSEALED - hint
        if (jarType == JarType.UNSEALED) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.hint.not_ready").withStyle(ChatFormatting.WHITE));
            }
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                Player player, BlockHitResult hitResult) {
        JarType jarType = state.getValue(JAR_TYPE);

        // Shift+RMB (empty hand) - remove lid
        if (player.isShiftKeyDown() && (jarType == JarType.SEALED || jarType == JarType.INFESTED)) {
            if (!level.isClientSide()) {
                unsealJar(level, pos, state, player);
            }
            return InteractionResult.SUCCESS;
        }

        // Empty hand on sealed/infested - show progress
        if (jarType == JarType.SEALED || jarType == JarType.INFESTED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    showProgress(player, jarType, be.getFermentationTicks());
                }
            }
            return InteractionResult.SUCCESS;
        }

        // Empty hand on UNSEALED - hint
        if (jarType == JarType.UNSEALED) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.hint.not_ready").withStyle(ChatFormatting.WHITE));
            }
            return InteractionResult.SUCCESS;
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    private static void sealJar(Level level, BlockPos pos, BlockState state, Player player, ItemStack stack) {
        KombuchaJarBlockEntity be = (KombuchaJarBlockEntity) level.getBlockEntity(pos);
        int ticks = be != null ? be.getFermentationTicks() : 0;
        int ticksPerStage = KombuchaConfig.TICKS_PER_STAGE.get();

        JarType targetType;
        int resetTicks;
        if (ticks >= ticksPerStage) {
            // Was at stage 2+ (infested), keep infested, restart from ticksPerStage
            targetType = JarType.INFESTED;
            resetTicks = ticksPerStage;
        } else {
            // Was at stage 1 (sealed), restart from 0
            targetType = JarType.SEALED;
            resetTicks = 0;
        }

        level.setBlock(pos, state.setValue(JAR_TYPE, targetType), 3);
        if (be != null) {
            be.setFermentationTicks(resetTicks);
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void unsealJar(Level level, BlockPos pos, BlockState state, Player player) {
        level.setBlock(pos, state.setValue(JAR_TYPE, JarType.UNSEALED), 3);
        if (!player.getAbilities().instabuild) {
            ItemStack wool = new ItemStack(Items.WHITE_WOOL);
            if (!player.getInventory().add(wool)) {
                player.drop(wool, false);
            }
        }
        level.playSound(null, pos, SoundEvents.WOOL_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void fillBottle(Level level, BlockPos pos, BlockState state, Player player,
                                    ItemStack stack, KombuchaJarBlockEntity be) {
        int ticksPerStage = KombuchaConfig.TICKS_PER_STAGE.get();

        // Only fill from a matured jar (stage 3)
        if (be.getFermentationTicks() < 2 * ticksPerStage) {
            return;
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        ItemStack drink = new ItemStack(getDrinkFor(be.getTeaType()));
        if (!player.getInventory().add(drink)) {
            player.drop(drink, false);
        }

        be.decrementFills();
        if (be.getFillsLeft() <= 0) {
            // Jar is empty - back to an empty jar
            level.setBlock(pos, Kombucha.EMPTY_KOMBUCHA_JAR.get().defaultBlockState(), 3);
        }
        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static Item getDrinkFor(TeaType teaType) {
        return switch (teaType) {
            case APPLE -> Kombucha.APPLE_KOMBUCHA_DRINK.get();
            case MELON -> Kombucha.MELON_KOMBUCHA_DRINK.get();
            case NETHER -> Kombucha.NETHER_KOMBUCHA_DRINK.get();
            case ENDER -> Kombucha.ENDER_KOMBUCHA_DRINK.get();
            case GOLDEN -> Kombucha.GOLDEN_KOMBUCHA_DRINK.get();
            default -> Kombucha.KOMBUCHA_DRINK.get();
        };
    }

    private static void showProgress(Player player, JarType jarType, int fermentationTicks) {
        int ticksPerStage = KombuchaConfig.TICKS_PER_STAGE.get();

        if (jarType == JarType.SEALED) {
            // Stage 1: tea without mushroom
            int remaining = Math.max(0, ticksPerStage - fermentationTicks);
            player.sendOverlayMessage(
                    Component.translatable("kombucha.progress.fermenting",
                            Component.literal(formatTime(remaining)).withStyle(ChatFormatting.WHITE))
                            .withStyle(ChatFormatting.YELLOW));
        } else {
            // INFESTED — stage 2 (mushroom growing) or stage 3 (matured)
            int remainingToFinal = Math.max(0, 2 * ticksPerStage - fermentationTicks);
            if (remainingToFinal > 0) {
                // Stage 2: mushroom appeared, still growing
                player.sendOverlayMessage(
                        Component.translatable("kombucha.progress.infested",
                                Component.literal(formatTime(remainingToFinal)).withStyle(ChatFormatting.WHITE))
                                .withStyle(ChatFormatting.YELLOW));
            } else {
                // Stage 3: matured
                player.sendOverlayMessage(
                        Component.translatable("kombucha.progress.ready")
                                .withStyle(ChatFormatting.GREEN));
            }
        }
    }

    private static String formatTime(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        if (seconds < 60) {
            return seconds + "s";
        }
        int minutes = seconds / 60;
        int secs = seconds % 60;
        if (secs == 0) {
            return minutes + "m";
        }
        return minutes + "m " + secs + "s";
    }
}
