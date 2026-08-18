package com.mapter.kombucha.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.model.CaveCombuchaMonsterModel;
import com.mapter.kombucha.client.renderer.entity.state.CombuchaMonsterRenderState;
import com.mapter.kombucha.entity.CaveCombuchaMonster;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CaveCombuchaMonsterRenderer extends MobRenderer<CaveCombuchaMonster, CombuchaMonsterRenderState, CaveCombuchaMonsterModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/entity/cave_combucha_monster.png");

    public CaveCombuchaMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new CaveCombuchaMonsterModel(context.bakeLayer(CaveCombuchaMonsterModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public CombuchaMonsterRenderState createRenderState() {
        return new CombuchaMonsterRenderState();
    }

    @Override
    public Identifier getTextureLocation(CombuchaMonsterRenderState state) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(CaveCombuchaMonster entity, CombuchaMonsterRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isJumping = !entity.onGround();
        state.attackTime = entity.getAttackAnim(partialTicks);
        state.shootTime = entity.getShootTime();
    }

    @Override
    public void submit(CombuchaMonsterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                       CameraRenderState camera) {
        this.getModel().setupAnim(state);
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
