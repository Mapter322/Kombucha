package com.mapter.kombucha.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.model.FriendlyKombuchaMonsterModel;
import com.mapter.kombucha.client.renderer.entity.state.CombuchaMonsterRenderState;
import com.mapter.kombucha.entity.FriendlyKombuchaMonster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FriendlyKombuchaMonsterRenderer extends MobRenderer<FriendlyKombuchaMonster, CombuchaMonsterRenderState, FriendlyKombuchaMonsterModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/entity/friendly_kombucha_monster.png");

    public FriendlyKombuchaMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new FriendlyKombuchaMonsterModel(context.bakeLayer(FriendlyKombuchaMonsterModel.LAYER_LOCATION)), 0.4F);
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
    public void extractRenderState(FriendlyKombuchaMonster entity, CombuchaMonsterRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isJumping = !entity.onGround();
        state.attackTime = entity.getAttackAnim(partialTicks);
        state.shootTime = entity.getShootTime();
    }

    @Override
    protected void scale(CombuchaMonsterRenderState state, PoseStack poseStack) {
        if (state.isBaby) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public void submit(CombuchaMonsterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                       CameraRenderState camera) {
        this.getModel().setupAnim(state);
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
