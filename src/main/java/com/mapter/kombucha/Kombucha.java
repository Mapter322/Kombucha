package com.mapter.kombucha;

import com.mapter.kombucha.item.KombuchaDrinkItem;
import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(Kombucha.MODID)
public class Kombucha {
    public static final String MODID = "kombucha";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<KombuchaDrinkItem> KOMBUCHA_DRINK = ITEMS.register("kombucha_drink",
            id -> new KombuchaDrinkItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))
                    .stacksTo(16)
                    .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    .usingConvertsTo(Items.GLASS_BOTTLE)));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KOMBUCHA_TAB = CREATIVE_MODE_TABS.register("kombucha_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.kombucha"))
                    .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS)
                    .icon(() -> KOMBUCHA_DRINK.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(KOMBUCHA_DRINK.get());
                    }).build());

    public Kombucha(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
