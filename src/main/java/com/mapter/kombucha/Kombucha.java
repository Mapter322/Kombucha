package com.mapter.kombucha;

import com.mapter.kombucha.block.EmptyJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlockEntity;
import com.mapter.kombucha.block.LavaJarBlock;
import com.mapter.kombucha.block.TeaType;
import com.mapter.kombucha.block.WaterJarBlock;
import com.mapter.kombucha.component.ModDataComponents;
import com.mapter.kombucha.config.KombuchaConfig;
import com.mapter.kombucha.entity.SpoiledKombuchaMonster;
import com.mapter.kombucha.entity.CaveKombuchaMonster;
import com.mapter.kombucha.entity.FriendlyKombuchaMonster;
import com.mapter.kombucha.entity.EnderKombuchaMonster;
import com.mapter.kombucha.entity.EnderKombuchaProjectile;
import com.mapter.kombucha.entity.MagmaKombuchaProjectile;
import com.mapter.kombucha.entity.NetherKombuchaMonster;
import com.mapter.kombucha.entity.SlimeKombuchaProjectile;
import com.mapter.kombucha.effect.ModEffects;
import com.mapter.kombucha.item.KombuchaDrinkItem;
import com.mapter.kombucha.item.KombuchaJarItem;
import com.mapter.kombucha.item.LivingKombuchaShroomItem;
import com.mapter.kombucha.loot.DropJarContentsFunction;
import com.mapter.kombucha.item.EmptyKombuchaBottleItem;
import com.mapter.kombucha.network.FriendlyKombuchaUpgradePayload;
import com.mapter.kombucha.network.FriendlyKombuchaStatePayload;
import com.mapter.kombucha.sound.ModSounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.EnumMap;

@Mod(Kombucha.MODID)
public class Kombucha {
    public static final String MODID = "kombucha";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(MODID);
    public static final DeferredRegister<MapCodec<? extends LootItemFunction>> LOOT_FUNCTIONS =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, MODID);

    static {
        LOOT_FUNCTIONS.register("drop_jar_contents", () -> DropJarContentsFunction.CODEC);
    }

    public static final DeferredHolder<EntityType<?>, EntityType<SpoiledKombuchaMonster>> SPOILED_KOMBUCHA_MONSTER =
            ENTITY_TYPES.registerEntityType("spoiled_kombucha_monster", SpoiledKombuchaMonster::new, MobCategory.MONSTER,
                    b -> b.sized(1.0F, 1.0F).clientTrackingRange(8).notInPeaceful());

    public static final DeferredHolder<EntityType<?>, EntityType<CaveKombuchaMonster>> CAVE_KOMBUCHA_MONSTER =
            ENTITY_TYPES.registerEntityType("cave_kombucha_monster", CaveKombuchaMonster::new, MobCategory.MONSTER,
                    b -> b.sized(1.0F, 1.0F).clientTrackingRange(8).notInPeaceful());

    public static final DeferredHolder<EntityType<?>, EntityType<FriendlyKombuchaMonster>> FRIENDLY_KOMBUCHA_MONSTER =
            ENTITY_TYPES.registerEntityType("friendly_kombucha_monster", FriendlyKombuchaMonster::new, MobCategory.CREATURE,
                    b -> b.sized(1.0F, 1.0F).clientTrackingRange(8));

    public static final DeferredHolder<EntityType<?>, EntityType<NetherKombuchaMonster>> NETHER_KOMBUCHA_MONSTER =
            ENTITY_TYPES.registerEntityType("nether_kombucha_monster", NetherKombuchaMonster::new, MobCategory.MONSTER,
                    b -> b.sized(1.0F, 1.0F).clientTrackingRange(8).fireImmune().notInPeaceful());

    public static final DeferredHolder<EntityType<?>, EntityType<EnderKombuchaMonster>> ENDER_KOMBUCHA_MONSTER =
            ENTITY_TYPES.registerEntityType("ender_kombucha_monster", EnderKombuchaMonster::new, MobCategory.MONSTER,
                    b -> b.sized(1.0F, 1.0F).clientTrackingRange(8).fireImmune().notInPeaceful());

    public static final DeferredHolder<EntityType<?>, EntityType<SlimeKombuchaProjectile>> SLIME_KOMBUCHA_PROJECTILE =
            ENTITY_TYPES.registerEntityType("slime_kombucha_projectile", SlimeKombuchaProjectile::new, MobCategory.MISC,
                    b -> b.sized(0.35F, 0.35F).clientTrackingRange(4).updateInterval(10));

    public static final DeferredHolder<EntityType<?>, EntityType<MagmaKombuchaProjectile>> MAGMA_KOMBUCHA_PROJECTILE =
            ENTITY_TYPES.registerEntityType("magma_kombucha_projectile", MagmaKombuchaProjectile::new, MobCategory.MISC,
                    b -> b.sized(0.35F, 0.35F).clientTrackingRange(4).updateInterval(10));

    public static final DeferredHolder<EntityType<?>, EntityType<EnderKombuchaProjectile>> ENDER_KOMBUCHA_PROJECTILE =
            ENTITY_TYPES.registerEntityType("ender_kombucha_projectile", EnderKombuchaProjectile::new, MobCategory.MISC,
                    b -> b.sized(0.35F, 0.35F).clientTrackingRange(4).updateInterval(10));

    public static final DeferredItem<Item> SPOILED_KOMBUCHA_MONSTER_SPAWN_EGG = ITEMS.registerItem(
            "spoiled_kombucha_monster_spawn_egg", SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(SPOILED_KOMBUCHA_MONSTER.get()));

    public static final DeferredItem<Item> CAVE_KOMBUCHA_MONSTER_SPAWN_EGG = ITEMS.registerItem(
            "cave_kombucha_monster_spawn_egg", SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(CAVE_KOMBUCHA_MONSTER.get()));

    public static final DeferredItem<Item> FRIENDLY_KOMBUCHA_MONSTER_SPAWN_EGG = ITEMS.registerItem(
            "friendly_kombucha_monster_spawn_egg", SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(FRIENDLY_KOMBUCHA_MONSTER.get()));

    public static final DeferredItem<Item> NETHER_KOMBUCHA_MONSTER_SPAWN_EGG = ITEMS.registerItem(
            "nether_kombucha_monster_spawn_egg", SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(NETHER_KOMBUCHA_MONSTER.get()));

    public static final DeferredItem<Item> ENDER_KOMBUCHA_MONSTER_SPAWN_EGG = ITEMS.registerItem(
            "ender_kombucha_monster_spawn_egg", SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(ENDER_KOMBUCHA_MONSTER.get()));

    public static final DeferredItem<Item> GOLDEN_KOMBUCHA_SHROOM = ITEMS.register("golden_kombucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> KOMBUCHA_SHROOM = ITEMS.register("kombucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.COMMON)));

    public static final DeferredItem<Item> LIVING_KOMBUCHA_SHROOM = ITEMS.register("living_kombucha_shroom",
            id -> new LivingKombuchaShroomItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> NETHER_KOMBUCHA_SHROOM = ITEMS.register("nether_kombucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.RARE)));

    public static final DeferredItem<Item> ENDER_KOMBUCHA_SHROOM = ITEMS.register("ender_kombucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.RARE)));

    public static final DeferredBlock<EmptyJarBlock> EMPTY_KOMBUCHA_JAR = BLOCKS.register("empty_kombucha_jar",
            id -> new EmptyJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> EMPTY_KOMBUCHA_JAR_ITEM = ITEMS.register("empty_kombucha_jar",
            id -> new BlockItem(EMPTY_KOMBUCHA_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredBlock<KombuchaJarBlock> KOMBUCHA_JAR = BLOCKS.register("kombucha_jar",
            id -> new KombuchaJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(KombuchaJarBlock.LAVA) ? 15 : 0)));
    public static final DeferredItem<KombuchaJarItem> KOMBUCHA_JAR_ITEM = ITEMS.register("kombucha_jar",
            id -> new KombuchaJarItem(KOMBUCHA_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KombuchaJarBlockEntity>> KOMBUCHA_JAR_BE =
            BLOCK_ENTITIES.register("kombucha_jar",
                    () -> new BlockEntityType<>(KombuchaJarBlockEntity::new, KOMBUCHA_JAR.get()));

    public static final DeferredItem<Item> EMPTY_KOMBUCHA_BOTTLE = ITEMS.register("empty_kombucha_bottle",
            id -> new EmptyKombuchaBottleItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .stacksTo(16)));

    // one mix + one drink per tea type — add a value to the enum and it shows up here
    public static final EnumMap<TeaType, DeferredItem<Item>> TEA_MIXES = new EnumMap<>(TeaType.class);
    public static final EnumMap<TeaType, DeferredItem<KombuchaDrinkItem>> KOMBUCHA_DRINKS = new EnumMap<>(TeaType.class);

    static {
        for (TeaType type : TeaType.values()) {
            TEA_MIXES.put(type, ITEMS.register(type.getMixId(), id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id)))));
            KOMBUCHA_DRINKS.put(type, ITEMS.register(type.getDrinkId(), id -> {
                Item.Properties properties = new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM, id))
                        .rarity(Rarity.UNCOMMON)
                        .stacksTo(16)
                        .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                        .usingConvertsTo(EMPTY_KOMBUCHA_BOTTLE.get());
                FoodProperties food = foodProperties(type);
                if (food != null) {
                    properties.food(food, Consumables.DEFAULT_DRINK);
                }
                return new KombuchaDrinkItem(type, properties);
            }));
        }
    }

    private static FoodProperties foodProperties(TeaType type) {
        return switch (type) {
            case TEA -> new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationModifier(4.0F / (3 * 2))
                    .alwaysEdible()
                    .build();
            case APPLE, NETHER, ENDER, GOLDEN -> new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(5.0F / (4 * 2))
                    .alwaysEdible()
                    .build();
            case MELON -> new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationModifier(3.5F / (3 * 2))
                    .alwaysEdible()
                    .build();
            default -> null;
        };
    }

    public static final DeferredBlock<WaterJarBlock> WATER_JAR = BLOCKS.register("water_jar",
            id -> new WaterJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> WATER_JAR_ITEM = ITEMS.register("water_jar",
            id -> new BlockItem(WATER_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredBlock<LavaJarBlock> LAVA_JAR = BLOCKS.register("lava_jar",
            id -> new LavaJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .lightLevel(state -> 15)));
    public static final DeferredItem<BlockItem> LAVA_JAR_ITEM = ITEMS.register("lava_jar",
            id -> new BlockItem(LAVA_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> DRIED_FERN = ITEMS.register("dried_fern",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> TEA_LEAVES = ITEMS.register("tea_leaves",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KOMBUCHA_TAB = CREATIVE_MODE_TABS.register("kombucha_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.kombucha"))
                    .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS)
                    .icon(() -> KOMBUCHA_DRINKS.get(TeaType.TEA).get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        KOMBUCHA_DRINKS.forEach((type, drink) -> output.accept(drink.get()));
                        output.accept(EMPTY_KOMBUCHA_BOTTLE.get());
                        output.accept(EMPTY_KOMBUCHA_JAR_ITEM.get());
                        output.accept(DRIED_FERN.get());
                        output.accept(TEA_LEAVES.get());
                        TEA_MIXES.forEach((type, mix) -> output.accept(mix.get()));

                        ItemStack unsealed = new ItemStack(KOMBUCHA_JAR_ITEM.get());
                        unsealed.set(ModDataComponents.JAR_TYPE, KombuchaJarBlock.JarType.UNSEALED);
                        output.accept(unsealed);

                        ItemStack sealed = new ItemStack(KOMBUCHA_JAR_ITEM.get());
                        sealed.set(ModDataComponents.JAR_TYPE, KombuchaJarBlock.JarType.SEALED);
                        output.accept(sealed);

                        ItemStack infested = new ItemStack(KOMBUCHA_JAR_ITEM.get());
                        infested.set(ModDataComponents.JAR_TYPE, KombuchaJarBlock.JarType.INFESTED);
                        output.accept(infested);

                        output.accept(WATER_JAR_ITEM.get());
                        output.accept(LAVA_JAR_ITEM.get());

                        output.accept(SPOILED_KOMBUCHA_MONSTER_SPAWN_EGG.get());
                        output.accept(CAVE_KOMBUCHA_MONSTER_SPAWN_EGG.get());
                        output.accept(FRIENDLY_KOMBUCHA_MONSTER_SPAWN_EGG.get());
                        output.accept(NETHER_KOMBUCHA_MONSTER_SPAWN_EGG.get());
                        output.accept(ENDER_KOMBUCHA_MONSTER_SPAWN_EGG.get());
                        output.accept(KOMBUCHA_SHROOM.get());
                        output.accept(LIVING_KOMBUCHA_SHROOM.get());
                        output.accept(GOLDEN_KOMBUCHA_SHROOM.get());
                        output.accept(NETHER_KOMBUCHA_SHROOM.get());
                        output.accept(ENDER_KOMBUCHA_SHROOM.get());
                    }).build());

    public Kombucha(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        LOOT_FUNCTIONS.register(modEventBus);

        modEventBus.addListener(Kombucha::registerEntityAttributes);
        modEventBus.addListener(Kombucha::registerSpawnPlacements);
        modEventBus.addListener(Kombucha::registerPayloads);

        modContainer.registerConfig(ModConfig.Type.SERVER, KombuchaConfig.SPEC);
    }

    private static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(SPOILED_KOMBUCHA_MONSTER.get(), SpoiledKombuchaMonster.createAttributes().build());
        event.put(CAVE_KOMBUCHA_MONSTER.get(), CaveKombuchaMonster.createAttributes().build());
        event.put(FRIENDLY_KOMBUCHA_MONSTER.get(), FriendlyKombuchaMonster.createAttributes().build());
        event.put(NETHER_KOMBUCHA_MONSTER.get(), NetherKombuchaMonster.createAttributes().build());
        event.put(ENDER_KOMBUCHA_MONSTER.get(), EnderKombuchaMonster.createAttributes().build());
    }

    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(SPOILED_KOMBUCHA_MONSTER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(CAVE_KOMBUCHA_MONSTER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(NETHER_KOMBUCHA_MONSTER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(FriendlyKombuchaUpgradePayload.TYPE,
                FriendlyKombuchaUpgradePayload.STREAM_CODEC, (payload, context) -> context.enqueueWork(() -> {
                    if (!(context.player().level().getEntity(payload.entityId())
                            instanceof FriendlyKombuchaMonster kombucha)
                            || !kombucha.isTame()
                            || kombucha.isBaby()
                            || !kombucha.isOwnedBy(context.player())
                            || kombucha.distanceToSqr(context.player()) > 64.0D) {
                        return;
                    }
                     kombucha.upgradeStat(payload.stat());
                 }));
        event.registrar("1").playToServer(FriendlyKombuchaStatePayload.TYPE,
                FriendlyKombuchaStatePayload.STREAM_CODEC, (payload, context) -> context.enqueueWork(() -> {
                    if (!(context.player().level().getEntity(payload.entityId())
                            instanceof FriendlyKombuchaMonster kombucha)
                            || !kombucha.isTame()
                            || kombucha.isBaby()
                            || !kombucha.isOwnedBy(context.player())
                            || kombucha.distanceToSqr(context.player()) > 64.0D) {
                        return;
                    }
                    kombucha.setState(payload.category(), payload.state());
                }));
    }

}
