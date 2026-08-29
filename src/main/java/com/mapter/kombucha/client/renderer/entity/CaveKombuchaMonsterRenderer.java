package com.mapter.kombucha.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.model.CaveKombuchaMonsterModel;
import com.mapter.kombucha.client.renderer.entity.state.KombuchaMonsterRenderState;
import com.mapter.kombucha.entity.CaveKombuchaMonster;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
public class CaveKombuchaMonsterRenderer extends MobRenderer<CaveKombuchaMonster, KombuchaMonsterRenderState, CaveKombuchaMonsterModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/entity/cave_kombucha_monster.png");

    public CaveKombuchaMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new CaveKombuchaMonsterModel(context.bakeLayer(CaveKombuchaMonsterModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public KombuchaMonsterRenderState createRenderState() {
        return new KombuchaMonsterRenderState();
    }

    @Override
    public Identifier getTextureLocation(KombuchaMonsterRenderState state) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(CaveKombuchaMonster entity, KombuchaMonsterRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isJumping = !entity.onGround();
        state.attackTime = entity.getAttackAnim(partialTicks);
        state.shootTime = entity.getShootTime();
    }

    @Override
    public void submit(KombuchaMonsterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                       CameraRenderState camera) {
        this.getModel().setupAnim(state);
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
