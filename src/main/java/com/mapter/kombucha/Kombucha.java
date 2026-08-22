package com.mapter.kombucha;

import com.mapter.kombucha.block.EmptyJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlockEntity;
import com.mapter.kombucha.block.LavaJarBlock;
import com.mapter.kombucha.block.TeaType;
import com.mapter.kombucha.block.WaterJarBlock;
import com.mapter.kombucha.component.ModDataComponents;
import com.mapter.kombucha.config.KombuchaConfig;
import com.mapter.kombucha.entity.SpoiledCombuchaMonster;
import com.mapter.kombucha.entity.EnderCombuchaMonster;
import com.mapter.kombucha.entity.EnderCombuchaProjectile;
import com.mapter.kombucha.entity.MagmaCombuchaProjectile;
import com.mapter.kombucha.entity.NetherCombuchaMonster;
import com.mapter.kombucha.entity.SlimeCombuchaProjectile;
import com.mapter.kombucha.effect.ModEffects;
import com.mapter.kombucha.item.KombuchaDrinkItem;
import com.mapter.kombucha.item.KombuchaJarItem;
import com.mapter.kombucha.loot.CopyJarDataFunction;
import com.mapter.kombucha.item.EmptyCombuchaBottleItem;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
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
        LOOT_FUNCTIONS.register("copy_jar_data", () -> CopyJarDataFunction.CODEC);
    }

    public static final DeferredHolder<EntityType<?>, EntityType<SpoiledCombuchaMonster>> SPOILED_COMBUCHA_MONSTER =
            ENTITY_TYPES.registerEntityType("spoiled_combucha_monster", SpoiledCombuchaMonster::new, MobCategory.MONSTER,
                    b -> b.sized(1.0F, 1.0F).clientTrackingRange(8).notInPeaceful());

    public static final DeferredHolder<EntityType<?>, EntityType<NetherCombuchaMonster>> NETHER_COMBUCHA_MONSTER =
            ENTITY_TYPES.registerEntityType("nether_combucha_monster", NetherCombuchaMonster::new, MobCategory.MONSTER,
                    b -> b.sized(1.0F, 1.0F).clientTrackingRange(8).fireImmune().notInPeaceful());

    public static final DeferredHolder<EntityType<?>, EntityType<EnderCombuchaMonster>> ENDER_COMBUCHA_MONSTER =
            ENTITY_TYPES.registerEntityType("ender_combucha_monster", EnderCombuchaMonster::new, MobCategory.MONSTER,
                    b -> b.sized(1.0F, 1.0F).clientTrackingRange(8).fireImmune().notInPeaceful());

    public static final DeferredHolder<EntityType<?>, EntityType<SlimeCombuchaProjectile>> SLIME_COMBUCHA_PROJECTILE =
            ENTITY_TYPES.registerEntityType("slime_combucha_projectile", SlimeCombuchaProjectile::new, MobCategory.MISC,
                    b -> b.sized(0.35F, 0.35F).clientTrackingRange(4).updateInterval(10));

    public static final DeferredHolder<EntityType<?>, EntityType<MagmaCombuchaProjectile>> MAGMA_COMBUCHA_PROJECTILE =
            ENTITY_TYPES.registerEntityType("magma_combucha_projectile", MagmaCombuchaProjectile::new, MobCategory.MISC,
                    b -> b.sized(0.35F, 0.35F).clientTrackingRange(4).updateInterval(10));

    public static final DeferredHolder<EntityType<?>, EntityType<EnderCombuchaProjectile>> ENDER_COMBUCHA_PROJECTILE =
            ENTITY_TYPES.registerEntityType("ender_combucha_projectile", EnderCombuchaProjectile::new, MobCategory.MISC,
                    b -> b.sized(0.35F, 0.35F).clientTrackingRange(4).updateInterval(10));

    public static final DeferredItem<Item> SPOILED_COMBUCHA_MONSTER_SPAWN_EGG = ITEMS.registerItem(
            "spoiled_combucha_monster_spawn_egg", SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(SPOILED_COMBUCHA_MONSTER.get()));

    public static final DeferredItem<Item> NETHER_COMBUCHA_MONSTER_SPAWN_EGG = ITEMS.registerItem(
            "nether_combucha_monster_spawn_egg", SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(NETHER_COMBUCHA_MONSTER.get()));

    public static final DeferredItem<Item> ENDER_COMBUCHA_MONSTER_SPAWN_EGG = ITEMS.registerItem(
            "ender_combucha_monster_spawn_egg", SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(ENDER_COMBUCHA_MONSTER.get()));

    public static final DeferredItem<Item> UNCOMMON_COMBUCHA_SHROOM = ITEMS.register("uncommon_combucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> COMBUCHA_SHROOM = ITEMS.register("combucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.COMMON)));

    public static final DeferredItem<Item> LIVING_COMBUCHA_SHROOM = ITEMS.register("living_combucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> NETHER_COMBUCHA_SHROOM = ITEMS.register("nether_combucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.RARE)));

    public static final DeferredItem<Item> ENDER_COMBUCHA_SHROOM = ITEMS.register("ender_combucha_shroom",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .rarity(Rarity.RARE)));

    public static final DeferredBlock<EmptyJarBlock> EMPTY_KOMBUCHA_JAR = BLOCKS.register("empty_combucha_jar",
            id -> new EmptyJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> EMPTY_KOMBUCHA_JAR_ITEM = ITEMS.register("empty_combucha_jar",
            id -> new BlockItem(EMPTY_KOMBUCHA_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredBlock<KombuchaJarBlock> KOMBUCHA_JAR = BLOCKS.register("kombucha_jar",
            id -> new KombuchaJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .noOcclusion()));
    public static final DeferredItem<KombuchaJarItem> KOMBUCHA_JAR_ITEM = ITEMS.register("kombucha_jar",
            id -> new KombuchaJarItem(KOMBUCHA_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KombuchaJarBlockEntity>> KOMBUCHA_JAR_BE =
            BLOCK_ENTITIES.register("kombucha_jar",
                    () -> new BlockEntityType<>(KombuchaJarBlockEntity::new, KOMBUCHA_JAR.get()));

    public static final DeferredItem<Item> EMPTY_KOMBUCHA_BOTTLE = ITEMS.register("empty_combucha_bottle",
            id -> new EmptyCombuchaBottleItem(new Item.Properties()
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
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> WATER_JAR_ITEM = ITEMS.register("water_jar",
            id -> new BlockItem(WATER_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredBlock<LavaJarBlock> LAVA_JAR = BLOCKS.register("lava_jar",
            id -> new LavaJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
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

                        output.accept(SPOILED_COMBUCHA_MONSTER_SPAWN_EGG.get());
                        output.accept(NETHER_COMBUCHA_MONSTER_SPAWN_EGG.get());
                        output.accept(ENDER_COMBUCHA_MONSTER_SPAWN_EGG.get());
                        output.accept(COMBUCHA_SHROOM.get());
                        output.accept(LIVING_COMBUCHA_SHROOM.get());
                        output.accept(UNCOMMON_COMBUCHA_SHROOM.get());
                        output.accept(NETHER_COMBUCHA_SHROOM.get());
                        output.accept(ENDER_COMBUCHA_SHROOM.get());
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

        modContainer.registerConfig(ModConfig.Type.SERVER, KombuchaConfig.SPEC);
    }

    private static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(SPOILED_COMBUCHA_MONSTER.get(), SpoiledCombuchaMonster.createAttributes().build());
        event.put(NETHER_COMBUCHA_MONSTER.get(), NetherCombuchaMonster.createAttributes().build());
        event.put(ENDER_COMBUCHA_MONSTER.get(), EnderCombuchaMonster.createAttributes().build());
    }

    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(SPOILED_COMBUCHA_MONSTER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(NETHER_COMBUCHA_MONSTER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

}
