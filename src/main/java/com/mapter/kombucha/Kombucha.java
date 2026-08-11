package com.mapter.kombucha;

import com.mapter.kombucha.block.EmptyJarBlock;
import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.block.WaterJarBlock;
import com.mapter.kombucha.component.ModDataComponents;
import com.mapter.kombucha.item.KombuchaDrinkItem;
import com.mapter.kombucha.item.KombuchaJarItem;
import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(Kombucha.MODID)
public class Kombucha {
    public static final String MODID = "kombucha";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Empty jar
    public static final DeferredBlock<EmptyJarBlock> EMPTY_KOMBUCHA_JAR = BLOCKS.register("empty_combucha_jar",
            id -> new EmptyJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> EMPTY_KOMBUCHA_JAR_ITEM = ITEMS.register("empty_combucha_jar",
            id -> new BlockItem(EMPTY_KOMBUCHA_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    // Kombucha jar (unsealed / sealed / infested)
    public static final DeferredBlock<KombuchaJarBlock> KOMBUCHA_JAR = BLOCKS.register("kombucha_jar",
            id -> new KombuchaJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .noOcclusion()));
    public static final DeferredItem<KombuchaJarItem> KOMBUCHA_JAR_ITEM = ITEMS.register("kombucha_jar",
            id -> new KombuchaJarItem(KOMBUCHA_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    // Kombucha drink
    public static final DeferredItem<KombuchaDrinkItem> KOMBUCHA_DRINK = ITEMS.register("kombucha_drink",
            id -> new KombuchaDrinkItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .stacksTo(16)
                    .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    .usingConvertsTo(Items.GLASS_BOTTLE)));

    // Water jar
    public static final DeferredBlock<WaterJarBlock> WATER_JAR = BLOCKS.register("water_jar",
            id -> new WaterJarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, id))
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> WATER_JAR_ITEM = ITEMS.register("water_jar",
            id -> new BlockItem(WATER_JAR.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    // Tea leaves
    public static final DeferredItem<Item> TEA_LEAVES = ITEMS.register("tea_leaves",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    // Tea mixes
    public static final DeferredItem<Item> TEA_MIX = ITEMS.register("tea_mix",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> APPLE_TEA_MIX = ITEMS.register("apple_tea_mix",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> MELON_TEA_MIX = ITEMS.register("melon_tea_mix",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> NETHER_TEA_MIX = ITEMS.register("nether_tea_mix",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> ENDER_TEA_MIX = ITEMS.register("ender_tea_mix",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> GOLDEN_TEA_MIX = ITEMS.register("golden_tea_mix",
            id -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    // Creative tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KOMBUCHA_TAB = CREATIVE_MODE_TABS.register("kombucha_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.kombucha"))
                    .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS)
                    .icon(() -> KOMBUCHA_DRINK.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(KOMBUCHA_DRINK.get());
                        output.accept(EMPTY_KOMBUCHA_JAR_ITEM.get());
                        output.accept(TEA_LEAVES.get());

                        output.accept(TEA_MIX.get());
                        output.accept(APPLE_TEA_MIX.get());
                        output.accept(MELON_TEA_MIX.get());
                        output.accept(NETHER_TEA_MIX.get());
                        output.accept(ENDER_TEA_MIX.get());
                        output.accept(GOLDEN_TEA_MIX.get());

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
                    }).build());

    public Kombucha(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
    }
}
