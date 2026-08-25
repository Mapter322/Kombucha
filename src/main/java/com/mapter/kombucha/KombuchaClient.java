package com.mapter.kombucha;

import com.mapter.kombucha.client.KombuchaTints;
import com.mapter.kombucha.client.model.BabyFriendlyKombuchaMonsterModel;
import com.mapter.kombucha.client.model.SpoiledKombuchaMonsterModel;
import com.mapter.kombucha.client.model.FriendlyKombuchaMonsterModel;
import com.mapter.kombucha.client.model.EnderKombuchaMonsterModel;
import com.mapter.kombucha.client.model.NetherKombuchaMonsterModel;
import com.mapter.kombucha.client.model.CaveKombuchaMonsterModel;
import com.mapter.kombucha.client.renderer.entity.SpoiledKombuchaMonsterRenderer;
import com.mapter.kombucha.client.renderer.entity.FriendlyKombuchaMonsterRenderer;
import com.mapter.kombucha.client.renderer.entity.EnderKombuchaMonsterRenderer;
import com.mapter.kombucha.client.renderer.entity.NetherKombuchaMonsterRenderer;
import com.mapter.kombucha.client.renderer.entity.CaveKombuchaMonsterRenderer;
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
        event.registerEntityRenderer(Kombucha.SPOILED_KOMBUCHA_MONSTER.get(), SpoiledKombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.FRIENDLY_KOMBUCHA_MONSTER.get(), FriendlyKombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.NETHER_KOMBUCHA_MONSTER.get(), NetherKombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.CAVE_KOMBUCHA_MONSTER.get(), CaveKombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.ENDER_KOMBUCHA_MONSTER.get(), EnderKombuchaMonsterRenderer::new);
        event.registerEntityRenderer(Kombucha.SLIME_KOMBUCHA_PROJECTILE.get(), context -> new ThrownItemRenderer<>(context, 0.7F, false));
        event.registerEntityRenderer(Kombucha.MAGMA_KOMBUCHA_PROJECTILE.get(), context -> new ThrownItemRenderer<>(context, 0.7F, true));
        event.registerEntityRenderer(Kombucha.ENDER_KOMBUCHA_PROJECTILE.get(), context -> new ThrownItemRenderer<>(context, 0.7F, false));
    }

    private static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpoiledKombuchaMonsterModel.LAYER_LOCATION, SpoiledKombuchaMonsterModel::createBodyLayer);
        event.registerLayerDefinition(FriendlyKombuchaMonsterModel.LAYER_LOCATION, FriendlyKombuchaMonsterModel::createBodyLayer);
        event.registerLayerDefinition(BabyFriendlyKombuchaMonsterModel.LAYER_LOCATION, BabyFriendlyKombuchaMonsterModel::createBodyLayer);
        event.registerLayerDefinition(NetherKombuchaMonsterModel.LAYER_LOCATION, NetherKombuchaMonsterModel::createBodyLayer);
        event.registerLayerDefinition(CaveKombuchaMonsterModel.LAYER_LOCATION, CaveKombuchaMonsterModel::createBodyLayer);
        event.registerLayerDefinition(EnderKombuchaMonsterModel.LAYER_LOCATION, EnderKombuchaMonsterModel::createBodyLayer);
    }
}
