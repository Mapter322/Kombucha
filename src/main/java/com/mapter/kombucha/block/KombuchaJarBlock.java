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
        INFESTED("infested"),
        UNSEALED_INFESTED("unsealed_infested"),
        UNSEALED_WATER_INFESTED("unsealed_water_infested"),
        SPOILED("spoiled");

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

    public enum Fill implements StringRepresentable {
        EMPTY("empty"),
        ONE_THIRD("one_third"),
        TWO_THIRDS("two_thirds"),
        FULL("full");

        public static final Codec<Fill> CODEC = StringRepresentable.fromEnum(Fill::values);

        private final String name;

        Fill(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public static final EnumProperty<JarType> JAR_TYPE = EnumProperty.create("jar_type", JarType.class);
    public static final EnumProperty<Fill> FILL = EnumProperty.create("fill", Fill.class);
    public static final MapCodec<KombuchaJarBlock> CODEC = simpleCodec(KombuchaJarBlock::new);

    public KombuchaJarBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(JAR_TYPE, JarType.UNSEALED)
                .setValue(FILL, Fill.FULL));
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
        builder.add(JAR_TYPE, FILL);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        JarType jarType = state.getValue(JAR_TYPE);

        // Wool - seal the jar (with or without the mushroom inside)
        if (stack.is(ItemTags.WOOL) && (jarType == JarType.UNSEALED || jarType == JarType.UNSEALED_INFESTED)) {
            if (!level.isClientSide()) {
                sealJar(level, pos, state, player, stack);
            }
            return InteractionResult.SUCCESS;
        }

        // Empty kombucha bottle on an open matured jar - fill with kombucha
        if (stack.is(Kombucha.EMPTY_KOMBUCHA_BOTTLE.get()) && jarType == JarType.UNSEALED_INFESTED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    if (be.getFillsLeft() <= 0) {
                        player.sendOverlayMessage(
                                Component.translatable("kombucha.hint.add_water").withStyle(ChatFormatting.WHITE));
                    } else if (be.getFermentationTicks() >= 2 * KombuchaConfig.TICKS_PER_STAGE.get()) {
                        fillBottle(level, pos, state, player, stack, be);
                    } else {
                        player.sendOverlayMessage(
                                Component.translatable("kombucha.hint.not_ready").withStyle(ChatFormatting.WHITE));
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }

        // Water bucket on an open empty jar with mushroom - fill with water
        if (stack.is(Items.WATER_BUCKET) && jarType == JarType.UNSEALED_INFESTED
                && state.getValue(FILL) == Fill.EMPTY) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(JAR_TYPE, JarType.UNSEALED_WATER_INFESTED)
                        .setValue(FILL, Fill.FULL), 3);
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // Tea mix on a water-filled jar with mushroom - new batch starts at stage 2
        TeaType teaType = TeaType.fromStack(stack);
        if (teaType != null && jarType == JarType.UNSEALED_WATER_INFESTED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    be.setTeaType(teaType);
                    // The SCOBY is already mature - fermentation starts from stage 2
                    be.setFermentationTicks(KombuchaConfig.TICKS_PER_STAGE.get());
                    // New batch - the jar is full again
                    be.setFillsLeft(3);
                }
                level.setBlock(pos, state.setValue(JAR_TYPE, JarType.UNSEALED_INFESTED), 3);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
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

        // Normal RMB on sealed/infested/unsealed infested/spoiled - show progress
        if (jarType == JarType.SEALED || jarType == JarType.INFESTED
                || jarType == JarType.UNSEALED_INFESTED || jarType == JarType.SPOILED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    showProgress(player, jarType, be.getFermentationTicks());
                }
            }
            return InteractionResult.SUCCESS;
        }

        // Open jar with water and mushroom - waiting for a tea mix
        if (jarType == JarType.UNSEALED_WATER_INFESTED) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.hint.add_tea_mix").withStyle(ChatFormatting.WHITE));
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

        // Empty hand on sealed/infested/unsealed infested/spoiled - show progress
        if (jarType == JarType.SEALED || jarType == JarType.INFESTED
                || jarType == JarType.UNSEALED_INFESTED || jarType == JarType.SPOILED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    showProgress(player, jarType, be.getFermentationTicks());
                }
            }
            return InteractionResult.SUCCESS;
        }

        // Empty hand on an open jar with water and mushroom - waiting for a tea mix
        if (jarType == JarType.UNSEALED_WATER_INFESTED) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.hint.add_tea_mix").withStyle(ChatFormatting.WHITE));
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
        boolean hasMushroom = state.getValue(JAR_TYPE) == JarType.UNSEALED_INFESTED;
        if (!hasMushroom && level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
            hasMushroom = be.getFermentationTicks() >= KombuchaConfig.TICKS_PER_STAGE.get();
        }

        // Closing the lid never rolls the brew back — the stage is preserved
        JarType targetType = hasMushroom ? JarType.INFESTED : JarType.SEALED;
        level.setBlock(pos, state.setValue(JAR_TYPE, targetType), 3);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void unsealJar(Level level, BlockPos pos, BlockState state, Player player) {
        // The mushroom stays visible when the lid comes off an infested jar
        JarType target = state.getValue(JAR_TYPE) == JarType.INFESTED
                ? JarType.UNSEALED_INFESTED
                : JarType.UNSEALED;
        level.setBlock(pos, state.setValue(JAR_TYPE, target), 3);
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
        if (be.getFermentationTicks() < 2 * ticksPerStage || be.getFillsLeft() <= 0) {
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
        BlockState newState = switch (be.getFillsLeft()) {
            case 0 -> state.setValue(FILL, Fill.EMPTY);
            case 1 -> state.setValue(FILL, Fill.ONE_THIRD);
            case 2 -> state.setValue(FILL, Fill.TWO_THIRDS);
            default -> state;
        };
        // The mushroom (SCOBY) stays in the jar even when all the drink is gone
        level.setBlock(pos, newState, 3);
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

        if (jarType == JarType.SPOILED) {
            player.sendOverlayMessage(
                    Component.translatable("kombucha.progress.spoiled").withStyle(ChatFormatting.DARK_RED));
            return;
        }

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
