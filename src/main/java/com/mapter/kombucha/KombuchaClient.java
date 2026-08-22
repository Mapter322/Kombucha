package com.mapter.kombucha;

import com.mapter.kombucha.client.KombuchaTints;
import com.mapter.kombucha.client.model.SpoiledCombuchaMonsterModel;
import com.mapter.kombucha.client.model.EnderCombuchaMonsterModel;
import com.mapter.kombucha.client.model.NetherCombuchaMonsterModel;
import com.mapter.kombucha.client.renderer.entity.SpoiledCombuchaMonsterRenderer;
import com.mapter.kombucha.client.renderer.entity.EnderCombuchaMonsterRenderer;
import com.mapter.kombucha.client.renderer.entity.NetherCombuchaMonsterRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@Mod(value = Kombucha.MODID, dist = Dist.CLIENT)
public class KombuchaClient {
    public KombuchaClient(IEventBus modEventBus) {
        modEventBus.addListener(KombuchaTints::registerBlockTints);
        modEventBus.addListener(KombuchaTints::registerItemTints);
        modEventBus.addListener(KombuchaClient::registerEntityRenderers);
        modEventBus.addListener(KombuchaClient::registerEntityLayerDefinitions);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Kombucha.SPOILED_COMBUCHA_MONSTER.get(), SpoiledCombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.NETHER_COMBUCHA_MONSTER.get(), NetherCombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.ENDER_COMBUCHA_MONSTER.get(), EnderCombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.SLIME_COMBUCHA_PROJECTILE.get(), context -> new ThrownItemRenderer<>(context, 0.7F, false));
        event.registerEntityRenderer(Kombucha.MAGMA_COMBUCHA_PROJECTILE.get(), context -> new ThrownItemRenderer<>(context, 0.7F, true));
        event.registerEntityRenderer(Kombucha.ENDER_COMBUCHA_PROJECTILE.get(), context -> new ThrownItemRenderer<>(context, 0.7F, false));
    }

    private static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpoiledCombuchaMonsterModel.LAYER_LOCATION, SpoiledCombuchaMonsterModel::createBodyLayer);
        event.registerLayerDefinition(NetherCombuchaMonsterModel.LAYER_LOCATION, NetherCombuchaMonsterModel::createBodyLayer);
        event.registerLayerDefinition(EnderCombuchaMonsterModel.LAYER_LOCATION, EnderCombuchaMonsterModel::createBodyLayer);
    }
}
