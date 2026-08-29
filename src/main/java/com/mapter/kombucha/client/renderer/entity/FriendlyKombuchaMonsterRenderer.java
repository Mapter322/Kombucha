package com.mapter.kombucha.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.model.BabyFriendlyKombuchaMonsterModel;
import com.mapter.kombucha.client.model.FriendlyKombuchaMonsterModel;
import com.mapter.kombucha.client.renderer.entity.state.KombuchaMonsterRenderState;
import com.mapter.kombucha.entity.FriendlyKombuchaMonster;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
public class FriendlyKombuchaMonsterRenderer extends MobRenderer<FriendlyKombuchaMonster, KombuchaMonsterRenderState, EntityModel<LivingEntityRenderState>> {
    private static final Identifier ADULT_TEXTURE =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/entity/friendly_kombucha_monster.png");
    private static final Identifier BABY_TEXTURE =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/entity/baby_kombucha_monster.png");
    private final EntityModel<LivingEntityRenderState> adultModel;
    private final EntityModel<LivingEntityRenderState> babyModel;

    public FriendlyKombuchaMonsterRenderer(EntityRendererProvider.Context context) {
        this(context,
                new FriendlyKombuchaMonsterModel(context.bakeLayer(FriendlyKombuchaMonsterModel.LAYER_LOCATION)),
                new BabyFriendlyKombuchaMonsterModel(context.bakeLayer(BabyFriendlyKombuchaMonsterModel.LAYER_LOCATION)));
    }

    private FriendlyKombuchaMonsterRenderer(EntityRendererProvider.Context context,
                                             EntityModel<LivingEntityRenderState> adultModel,
                                             EntityModel<LivingEntityRenderState> babyModel) {
        super(context, adultModel, 0.4F);
        this.adultModel = adultModel;
        this.babyModel = babyModel;
    }

    @Override
    public KombuchaMonsterRenderState createRenderState() {
        return new KombuchaMonsterRenderState();
    }

    @Override
    public Identifier getTextureLocation(KombuchaMonsterRenderState state) {
        return state.isBaby ? BABY_TEXTURE : ADULT_TEXTURE;
    }

    @Override
    public void extractRenderState(FriendlyKombuchaMonster entity, KombuchaMonsterRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isJumping = !entity.onGround();
        state.attackTime = entity.getAttackAnim(partialTicks);
        state.shootTime = entity.getShootTime();
    }

    @Override
    public void submit(KombuchaMonsterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                       CameraRenderState camera) {
        this.model = state.isBaby ? this.babyModel : this.adultModel;
        this.getModel().setupAnim(state);
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
