package com.mapter.kombucha.block;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.component.LivingShroomData;
import com.mapter.kombucha.component.ModDataComponents;
import com.mapter.kombucha.config.KombuchaConfig;
import com.mapter.kombucha.entity.FriendlyKombuchaMonster;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.LevelReader;
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
        UNSEALED_LAVA_INFESTED("unsealed_lava_infested"),
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
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, includeData);
        stack.set(ModDataComponents.JAR_TYPE, state.getValue(JAR_TYPE));
        if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
            stack.set(ModDataComponents.TEA_TYPE, be.getTeaType());
            if (be.getLivingShroomData() != null) {
                stack.set(ModDataComponents.LIVING_SHROOM_DATA, be.getLivingShroomData());
            }
        }
        return stack;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        JarType jarType = state.getValue(JAR_TYPE);

        // seal with wool (mushroom or not)
        if (stack.is(ItemTags.WOOL) && (jarType == JarType.UNSEALED || jarType == JarType.UNSEALED_INFESTED)) {
            if (!level.isClientSide()) {
                sealJar(level, pos, state, player, stack);
            }
            return InteractionResult.SUCCESS;
        }

        // bottle from a matured jar
        if (stack.is(Kombucha.EMPTY_KOMBUCHA_BOTTLE.get()) && jarType == JarType.UNSEALED_INFESTED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    if (be.hasLivingShroom()) {
                        // a living shroom jar does not produce drinks — shift+RMB revives the monster
                        player.sendOverlayMessage(
                                Component.translatable("kombucha.progress.living_ready").withStyle(ChatFormatting.GREEN));
                    } else if (be.getFillsLeft() <= 0) {
                        // the nether brew is refilled with lava, everything else with water
                        player.sendOverlayMessage(
                                Component.translatable(be.getTeaType() == TeaType.NETHER
                                                ? "kombucha.hint.add_lava"
                                                : "kombucha.hint.add_water")
                                        .withStyle(ChatFormatting.WHITE));
                    } else if (FermentationStage.of(be.getFermentationTicks(),
                            KombuchaConfig.TICKS_TO_INFESTED.get(),
                            KombuchaConfig.TICKS_TO_FERMENTED.get(),
                            KombuchaConfig.TICKS_TO_SPOILED.get(),
                            KombuchaConfig.TICKS_TO_MONSTER.get()) == FermentationStage.THREE) {
                        fillBottle(level, pos, state, player, stack, be);
                    } else {
                        player.sendOverlayMessage(
                                Component.translatable("kombucha.hint.not_ready").withStyle(ChatFormatting.WHITE));
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }

        // add water to a drained jar
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

        // add lava to a drained nether jar
        if (stack.is(Items.LAVA_BUCKET) && jarType == JarType.UNSEALED_INFESTED
                && state.getValue(FILL) == Fill.EMPTY) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(JAR_TYPE, JarType.UNSEALED_LAVA_INFESTED)
                        .setValue(FILL, Fill.FULL), 3);
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // add tea mix — a new batch starts at stage 2
        TeaType teaType = TeaType.fromStack(stack);
        if (teaType != null) {
            boolean waterJar = jarType == JarType.UNSEALED_WATER_INFESTED;
            boolean lavaJar = jarType == JarType.UNSEALED_LAVA_INFESTED;
            boolean nether = teaType == TeaType.NETHER;

            if (waterJar && !nether || lavaJar && nether) {
                if (!level.isClientSide()) {
                    if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                        be.setTeaType(teaType);
                        // the SCOBY is already mature, so we start from stage 2
                        be.setFermentationTicks(KombuchaConfig.TICKS_TO_INFESTED.get());
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

            // the nether mix needs a lava jar
            if (nether && waterJar) {
                if (!level.isClientSide()) {
                    player.sendOverlayMessage(
                            Component.translatable("kombucha.hint.nether_needs_lava").withStyle(ChatFormatting.WHITE));
                }
                return InteractionResult.SUCCESS;
            }

            // only the nether mix brews in lava
            if (lavaJar && !nether) {
                if (!level.isClientSide()) {
                    player.sendOverlayMessage(
                            Component.translatable("kombucha.hint.add_nether_mix").withStyle(ChatFormatting.WHITE));
                }
                return InteractionResult.SUCCESS;
            }
        }

        // shift+RMB: revive the kombucha in a matured living-shroom jar, otherwise take the lid off
        if (player.isShiftKeyDown()) {
            if (tryReviveLivingShroom(level, pos, state, player)) {
                return InteractionResult.SUCCESS;
            }
            if (tryTakeMushroom(level, pos, state, player)) {
                return InteractionResult.SUCCESS;
            }
            if (jarType == JarType.SEALED || jarType == JarType.INFESTED) {
                if (!level.isClientSide()) {
                    unsealJar(level, pos, state, player);
                }
                return InteractionResult.SUCCESS;
            }
        }

        // RMB: show how the brew is doing
        if (jarType == JarType.SEALED || jarType == JarType.INFESTED
                || jarType == JarType.UNSEALED_INFESTED || jarType == JarType.SPOILED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    showProgress(level, player, jarType, be);
                }
            }
            return InteractionResult.SUCCESS;
        }

        // still waiting for a tea mix
        if (jarType == JarType.UNSEALED_WATER_INFESTED || jarType == JarType.UNSEALED_LAVA_INFESTED) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(
                        Component.translatable(jarType == JarType.UNSEALED_LAVA_INFESTED
                                        ? "kombucha.hint.add_nether_mix"
                                        : "kombucha.hint.add_tea_mix")
                                .withStyle(ChatFormatting.WHITE));
            }
            return InteractionResult.SUCCESS;
        }

        // anything else on an unsealed jar: hint
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

        // shift+RMB: revive the kombucha in a matured living-shroom jar, otherwise take the lid off
        if (player.isShiftKeyDown()) {
            if (tryReviveLivingShroom(level, pos, state, player)) {
                return InteractionResult.SUCCESS;
            }
            if (tryTakeMushroom(level, pos, state, player)) {
                return InteractionResult.SUCCESS;
            }
            if (jarType == JarType.SEALED || jarType == JarType.INFESTED) {
                if (!level.isClientSide()) {
                    unsealJar(level, pos, state, player);
                }
                return InteractionResult.SUCCESS;
            }
        }

        // empty hand: show how the brew is doing
        if (jarType == JarType.SEALED || jarType == JarType.INFESTED
                || jarType == JarType.UNSEALED_INFESTED || jarType == JarType.SPOILED) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) {
                    showProgress(level, player, jarType, be);
                }
            }
            return InteractionResult.SUCCESS;
        }

        // still waiting for a tea mix
        if (jarType == JarType.UNSEALED_WATER_INFESTED || jarType == JarType.UNSEALED_LAVA_INFESTED) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(
                        Component.translatable(jarType == JarType.UNSEALED_LAVA_INFESTED
                                        ? "kombucha.hint.add_nether_mix"
                                        : "kombucha.hint.add_tea_mix")
                                .withStyle(ChatFormatting.WHITE));
            }
            return InteractionResult.SUCCESS;
        }

        // anything else: hint
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
            hasMushroom = FermentationStage.of(be.getFermentationTicks(),
                    KombuchaConfig.TICKS_TO_INFESTED.get(),
                    KombuchaConfig.TICKS_TO_FERMENTED.get(),
                    KombuchaConfig.TICKS_TO_SPOILED.get(),
                    KombuchaConfig.TICKS_TO_MONSTER.get()) != FermentationStage.ONE;
        }

        // closing the lid keeps the stage
        JarType targetType = hasMushroom ? JarType.INFESTED : JarType.SEALED;
        level.setBlock(pos, state.setValue(JAR_TYPE, targetType), 3);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void unsealJar(Level level, BlockPos pos, BlockState state, Player player) {
        // the mushroom stays visible once the lid is off
        JarType target = state.getValue(JAR_TYPE) == JarType.INFESTED
                ? JarType.UNSEALED_INFESTED
                : JarType.UNSEALED;
        level.setBlock(pos, state.setValue(JAR_TYPE, target), 3);
        if (!player.getAbilities().instabuild) {
            ItemStack wool = new ItemStack(Items.WOOL.white());
            if (!player.getInventory().add(wool)) {
                player.drop(wool, false);
            }
        }
        level.playSound(null, pos, SoundEvents.WOOL_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void fillBottle(Level level, BlockPos pos, BlockState state, Player player,
                                    ItemStack stack, KombuchaJarBlockEntity be) {
        int ticksToInfested = KombuchaConfig.TICKS_TO_INFESTED.get();
        int ticksToFermented = KombuchaConfig.TICKS_TO_FERMENTED.get();
        int ticksToSpoiled = KombuchaConfig.TICKS_TO_SPOILED.get();

        // only when matured (stage 3)
        if (FermentationStage.of(be.getFermentationTicks(), ticksToInfested,
                ticksToFermented, ticksToSpoiled, KombuchaConfig.TICKS_TO_MONSTER.get()) != FermentationStage.THREE
                || be.getFillsLeft() <= 0) {
            return;
        }
        if (!level.canSeeSky(pos.above())) {
            player.sendOverlayMessage(
                    Component.translatable("kombucha.hint.fresh_air").withStyle(ChatFormatting.WHITE));
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
        // the SCOBY stays even when the jar is empty
        level.setBlock(pos, newState, 3);
        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static Item getDrinkFor(TeaType teaType) {
        return Kombucha.KOMBUCHA_DRINKS.get(teaType).get();
    }

    /** Shift+RMB on an open jar: take the mushroom during stages 2 and 3. */
    private static boolean tryTakeMushroom(Level level, BlockPos pos, BlockState state, Player player) {
        JarType jarType = state.getValue(JAR_TYPE);
        if (jarType != JarType.UNSEALED_INFESTED
                && jarType != JarType.UNSEALED_WATER_INFESTED
                && jarType != JarType.UNSEALED_LAVA_INFESTED) {
            return false;
        }
        if (!(level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be)) {
            return false;
        }

        FermentationStage stage = FermentationStage.of(be.getFermentationTicks(),
                KombuchaConfig.TICKS_TO_INFESTED.get(),
                KombuchaConfig.TICKS_TO_FERMENTED.get(),
                KombuchaConfig.TICKS_TO_SPOILED.get(),
                KombuchaConfig.TICKS_TO_MONSTER.get());
        if (stage != FermentationStage.TWO && stage != FermentationStage.THREE) {
            return false;
        }

        if (!level.isClientSide()) {
            ItemStack mushroom = new ItemStack(Kombucha.KOMBUCHA_SHROOM.get());
            if (!player.getInventory().add(mushroom)) {
                player.drop(mushroom, false);
            }

            if (jarType == JarType.UNSEALED_WATER_INFESTED) {
                level.setBlock(pos, Kombucha.WATER_JAR.get().defaultBlockState(), 3);
            } else if (jarType == JarType.UNSEALED_LAVA_INFESTED) {
                level.setBlock(pos, Kombucha.LAVA_JAR.get().defaultBlockState(), 3);
            } else {
                be.setFermentationTicks(0);
                level.setBlock(pos, state.setValue(JAR_TYPE, JarType.UNSEALED), 3);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return true;
    }

    /** Shift+RMB on a living-shroom jar: revive the monster once the shroom has matured. */
    private static boolean tryReviveLivingShroom(Level level, BlockPos pos, BlockState state, Player player) {
        JarType jarType = state.getValue(JAR_TYPE);
        if (jarType != JarType.INFESTED && jarType != JarType.UNSEALED_INFESTED) {
            return false;
        }
        if (!(level.getBlockEntity(pos) instanceof KombuchaJarBlockEntity be) || !be.hasLivingShroom()) {
            return false;
        }
        if (!level.isClientSide()) {
            FermentationStage stage = FermentationStage.of(be.getFermentationTicks(),
                    KombuchaConfig.TICKS_TO_INFESTED.get(),
                    KombuchaConfig.TICKS_TO_FERMENTED.get(),
                    KombuchaConfig.TICKS_TO_SPOILED.get(),
                    KombuchaConfig.TICKS_TO_MONSTER.get());
            if (stage == FermentationStage.THREE) {
                reviveLivingShroom(level, pos, state, be);
            } else {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.hint.living_not_ready").withStyle(ChatFormatting.WHITE));
            }
        }
        return true;
    }

    private static void reviveLivingShroom(Level level, BlockPos pos, BlockState state, KombuchaJarBlockEntity be) {
        LivingShroomData data = be.getLivingShroomData();
        if (data == null) {
            return;
        }
        level.addFreshEntity(FriendlyKombuchaMonster.reviveFromShroom(level, pos, data));
        // the living shroom is gone — the jar is empty
        be.setLivingShroomData(null);
        be.setFermentationTicks(0);
        be.setFillsLeft(0);
        level.setBlock(pos, Kombucha.EMPTY_KOMBUCHA_JAR.get().defaultBlockState(), 3);
        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D,
                    40, 0.3D, 0.3D, 0.3D, 0.1D);
        }
    }

    private static void showProgress(Level level, Player player, JarType jarType, KombuchaJarBlockEntity be) {
        int fermentationTicks = be.getFermentationTicks();
        int ticksToInfested = KombuchaConfig.TICKS_TO_INFESTED.get();
        int ticksToFermented = KombuchaConfig.TICKS_TO_FERMENTED.get();
        int ticksToSpoiled = KombuchaConfig.TICKS_TO_SPOILED.get();
        FermentationStage stage = FermentationStage.of(fermentationTicks, ticksToInfested,
                ticksToFermented, ticksToSpoiled, KombuchaConfig.TICKS_TO_MONSTER.get());

        if (jarType == JarType.SPOILED) {
            if (stage == FermentationStage.SPOILED) {
                long remaining = Math.max(0L, (long) ticksToInfested + ticksToFermented + ticksToSpoiled
                        + KombuchaConfig.TICKS_TO_MONSTER.get() - fermentationTicks);
                player.sendOverlayMessage(
                        Component.translatable("kombucha.progress.turning_monster",
                                        Component.literal(formatTime(remaining)).withStyle(ChatFormatting.WHITE))
                                .withStyle(ChatFormatting.DARK_RED));
            } else {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.progress.spoiled").withStyle(ChatFormatting.DARK_RED));
            }
            return;
        }

        if (jarType == JarType.UNSEALED_INFESTED) {
            if (stage == FermentationStage.THREE) {
                if (be.hasLivingShroom()) {
                    player.sendOverlayMessage(
                            Component.translatable("kombucha.progress.living_ready").withStyle(ChatFormatting.GREEN));
                } else {
                    player.sendOverlayMessage(
                            Component.translatable("kombucha.progress.ready_unsealed").withStyle(ChatFormatting.GREEN));
                }
            } else {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.hint.cover_jar").withStyle(ChatFormatting.WHITE));
            }
            return;
        }

        if (jarType == JarType.INFESTED && stage == FermentationStage.TWO
                && !level.canSeeSky(be.getBlockPos().above())) {
            player.sendOverlayMessage(
                    Component.translatable("kombucha.hint.fresh_air").withStyle(ChatFormatting.WHITE));
            return;
        }

        if (jarType == JarType.SEALED) {
            if (fermentationTicks < ticksToInfested
                    && !KombuchaJarBlockEntity.isUnderground(level, be.getBlockPos())) {
                player.sendOverlayMessage(
                    Component.translatable("kombucha.hint.underground").withStyle(ChatFormatting.WHITE));
                return;
            }
            // stage 1: plain tea
            int remaining = Math.max(0, ticksToInfested - fermentationTicks);
            player.sendOverlayMessage(
                    Component.translatable("kombucha.progress.fermenting",
                            Component.literal(formatTime(remaining)).withStyle(ChatFormatting.WHITE))
                            .withStyle(ChatFormatting.YELLOW));
        } else if (stage == FermentationStage.TWO) {
            // stage 2: mushroom growing
            int remaining = Math.max(0, ticksToInfested + ticksToFermented - fermentationTicks);
            player.sendOverlayMessage(
                    Component.translatable("kombucha.progress.infested",
                            Component.literal(formatTime(remaining)).withStyle(ChatFormatting.WHITE))
                            .withStyle(ChatFormatting.YELLOW));
        } else {
            // stage 3: ready
            if (be.hasLivingShroom()) {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.progress.living_ready").withStyle(ChatFormatting.GREEN));
            } else {
                player.sendOverlayMessage(
                        Component.translatable("kombucha.progress.ready")
                                .withStyle(ChatFormatting.GREEN));
            }
        }
    }

    private static String formatTime(long ticks) {
        long seconds = Math.max(1, ticks / 20);
        if (seconds < 60) {
            return seconds + "s";
        }
        long minutes = seconds / 60;
        long secs = seconds % 60;
        if (secs == 0) {
            return minutes + "m";
        }
        return minutes + "m " + secs + "s";
    }
}
