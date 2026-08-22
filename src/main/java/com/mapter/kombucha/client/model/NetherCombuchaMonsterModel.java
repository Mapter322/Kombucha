package com.mapter.kombucha.client.model;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.renderer.entity.state.CombuchaMonsterRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class NetherCombuchaMonsterModel extends EntityModel<LivingEntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Kombucha.MODID, "nether_combucha_monster"), "main");

    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart mouth_tentacle4;
    private final ModelPart mouth_tentacle3;
    private final ModelPart mouth_tentacle2;
    private final ModelPart mouth_tentacle;
    private final ModelPart mid;
    private final ModelPart bottom;
    private final ModelPart tentacle;
    private final ModelPart tentacle2;
    private final ModelPart tentacle3;
    private final ModelPart tentacle4;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation attackAnimation;
    private final KeyframeAnimation shootAnimation;
    private final KeyframeAnimation walkAnimation;

    public NetherCombuchaMonsterModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.eyes = this.head.getChild("eyes");
        this.mouth_tentacle4 = this.head.getChild("mouth_tentacle4");
        this.mouth_tentacle3 = this.head.getChild("mouth_tentacle3");
        this.mouth_tentacle2 = this.head.getChild("mouth_tentacle2");
        this.mouth_tentacle = this.head.getChild("mouth_tentacle");
        this.mid = root.getChild("mid");
        this.bottom = root.getChild("bottom");
        this.tentacle = root.getChild("tentacle");
        this.tentacle2 = root.getChild("tentacle2");
        this.tentacle3 = root.getChild("tentacle3");
        this.tentacle4 = root.getChild("tentacle4");
        this.idleAnimation = nether_combucha_monsterAnimation.idle.bake(root);
        this.attackAnimation = nether_combucha_monsterAnimation.attack.bake(root);
        this.shootAnimation = nether_combucha_monsterAnimation.shoot.bake(root);
        this.walkAnimation = nether_combucha_monsterAnimation.walk.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-7.0F, -2.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
        .texOffs(0, 46).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
        .texOffs(56, 0).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
        .texOffs(70, 75).addBox(3.0F, -7.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(56, 28).addBox(2.0F, -8.0F, 1.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(66, 75).addBox(-5.0F, -8.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(72, 18).addBox(-6.0F, -10.0F, 3.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

        head.addOrReplaceChild("eyes", CubeListBuilder.create()
                .texOffs(68, 28).addBox(-4.0F, -8.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 23).addBox(2.0F, -8.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
        PartDefinition mouth_tentacle4 = head.addOrReplaceChild("mouth_tentacle4", CubeListBuilder.create().texOffs(54, 75).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.0F, -7.75F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r1 = mouth_tentacle4.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(60, 75).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition mouth_tentacle3 = head.addOrReplaceChild("mouth_tentacle3", CubeListBuilder.create().texOffs(72, 72).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -1.0F, -8.0F, 0.0F, -0.0873F, 0.0F));

        PartDefinition cube_r2 = mouth_tentacle3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 75).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition mouth_tentacle2 = head.addOrReplaceChild("mouth_tentacle2", CubeListBuilder.create().texOffs(60, 72).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.0F, -8.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition cube_r3 = mouth_tentacle2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(66, 72).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition mouth_tentacle = head.addOrReplaceChild("mouth_tentacle", CubeListBuilder.create().texOffs(48, 72).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -1.0F, -8.0F, 0.0F, 0.1745F, 0.0F));

        PartDefinition cube_r4 = mouth_tentacle.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(54, 72).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition mid = partdefinition.addOrReplaceChild("mid", CubeListBuilder.create().texOffs(74, 75).addBox(-8.0F, -4.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(0, 76).addBox(-7.0F, 0.0F, -7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(4, 76).addBox(5.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(12, 76).addBox(0.0F, 1.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(48, 44).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
        .texOffs(0, 32).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
        .texOffs(48, 32).addBox(-5.0F, 2.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
        .texOffs(72, 12).addBox(-2.0F, -1.0F, -7.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

        PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(8, 76).addBox(7.0F, -2.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(0, 0).addBox(-7.0F, -2.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.0F));

        PartDefinition tentacle = partdefinition.addOrReplaceChild("tentacle", CubeListBuilder.create().texOffs(48, 56).addBox(-0.3736F, -2.0F, -5.7579F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.25F, 22.0F, -5.25F, 0.3927F, 0.6981F, 0.0F));

        PartDefinition cube_r5 = tentacle.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 60).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6264F, -3.75F, -11.7579F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r6 = tentacle.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(56, 12).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6264F, -0.5F, -6.7579F, -0.48F, 0.0F, 0.0F));

        PartDefinition tentacle2 = partdefinition.addOrReplaceChild("tentacle2", CubeListBuilder.create().texOffs(32, 68).addBox(0.0747F, -2.0F, -6.4328F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 22.0F, -5.75F, 0.3927F, -0.6981F, 0.0F));

        PartDefinition cube_r7 = tentacle2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(16, 60).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0747F, -3.75F, -12.4328F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r8 = tentacle2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(16, 68).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0747F, -0.5F, -7.4328F, -0.48F, 0.0F, 0.0F));

        PartDefinition tentacle3 = partdefinition.addOrReplaceChild("tentacle3", CubeListBuilder.create().texOffs(32, 60).addBox(-0.5412F, -2.0F, -6.3338F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 22.0F, 3.0F, 0.0F, -1.5708F, 0.3927F));

        PartDefinition cube_r9 = tentacle3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(56, 20).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4588F, -3.75F, -12.3338F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r10 = tentacle3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(48, 64).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4588F, -0.5F, -7.3338F, -0.48F, 0.0F, 0.0F));

        PartDefinition tentacle4 = partdefinition.addOrReplaceChild("tentacle4", CubeListBuilder.create().texOffs(64, 56).addBox(-0.7654F, -2.0F, -6.8206F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 21.75F, 4.0F, 0.0F, 1.5708F, -0.3927F));

        PartDefinition cube_r11 = tentacle4.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 68).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2346F, -3.75F, -12.8206F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r12 = tentacle4.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(64, 64).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2346F, -0.5F, -7.8206F, -0.48F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.idleAnimation.apply((long) (state.ageInTicks * 50.0F), 1.0F);
        this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.5F, 4.0F);
        this.eyes.yRot = Mth.clamp(state.yRot, -35.0F, 35.0F) * Mth.DEG_TO_RAD;
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.attackTime > 0.0F) {
            this.attackAnimation.apply((long) (combuchaState.attackTime * 800.0F), 1.5F);
        }
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.shootTime > 0) {
            long elapsed = (long) ((8 - combuchaState.shootTime) * 50L);
            this.shootAnimation.apply(elapsed, 1.0F);
        }
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.isJumping) {
            this.tentacle.xRot += 0.45F;
            this.tentacle2.xRot += 0.45F;
            this.tentacle3.xRot += 0.45F;
            this.tentacle4.xRot += 0.45F;
        }
    }
}
