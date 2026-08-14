package com.mapter.kombucha;

import com.mapter.kombucha.client.KombuchaTints;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Kombucha.MODID, dist = Dist.CLIENT)
public class KombuchaClient {
    public KombuchaClient(IEventBus modEventBus) {
        modEventBus.addListener(KombuchaTints::registerBlockTints);
        modEventBus.addListener(KombuchaTints::registerItemTints);
    }
}
