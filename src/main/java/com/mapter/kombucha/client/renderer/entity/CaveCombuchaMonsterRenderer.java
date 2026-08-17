package com.mapter.kombucha.client.renderer.entity;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.model.CaveCombuchaMonsterModel;
import com.mapter.kombucha.entity.CaveCombuchaMonster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CaveCombuchaMonsterRenderer extends MobRenderer<CaveCombuchaMonster, LivingEntityRenderState, CaveCombuchaMonsterModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/entity/cave_combucha_monster.png");

    public CaveCombuchaMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new CaveCombuchaMonsterModel(context.bakeLayer(CaveCombuchaMonsterModel.LAYER_LOCATION)), 0.4F);
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
