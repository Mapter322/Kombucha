package com.mapter.kombucha.client.renderer.entity;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.model.NetherCombuchaMonsterModel;
import com.mapter.kombucha.entity.NetherCombuchaMonster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherCombuchaMonsterRenderer extends MobRenderer<NetherCombuchaMonster, LivingEntityRenderState, NetherCombuchaMonsterModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/entity/nether_combucha_monster.png");

    public NetherCombuchaMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new NetherCombuchaMonsterModel(context.bakeLayer(NetherCombuchaMonsterModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
