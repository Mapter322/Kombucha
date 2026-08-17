package com.mapter.kombucha;

import com.mapter.kombucha.client.KombuchaTints;
import com.mapter.kombucha.client.model.CaveCombuchaMonsterModel;
import com.mapter.kombucha.client.model.NetherCombuchaMonsterModel;
import com.mapter.kombucha.client.renderer.entity.CaveCombuchaMonsterRenderer;
import com.mapter.kombucha.client.renderer.entity.NetherCombuchaMonsterRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = Kombucha.MODID, dist = Dist.CLIENT)
public class KombuchaClient {
    public KombuchaClient(IEventBus modEventBus) {
        modEventBus.addListener(KombuchaTints::registerBlockTints);
        modEventBus.addListener(KombuchaTints::registerItemTints);
        modEventBus.addListener(KombuchaClient::registerEntityRenderers);
        modEventBus.addListener(KombuchaClient::registerEntityLayerDefinitions);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Kombucha.CAVE_COMBUCHA_MONSTER.get(), CaveCombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.NETHER_COMBUCHA_MONSTER.get(), NetherCombuchaMonsterRenderer::new);
    }

    private static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CaveCombuchaMonsterModel.LAYER_LOCATION, CaveCombuchaMonsterModel::createBodyLayer);
        event.registerLayerDefinition(NetherCombuchaMonsterModel.LAYER_LOCATION, NetherCombuchaMonsterModel::createBodyLayer);
    }
}
