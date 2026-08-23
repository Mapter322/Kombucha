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

public class BabyFriendlyKombuchaMonsterModel extends EntityModel<LivingEntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Kombucha.MODID, "baby_combucha_monster"), "main");

    private final ModelPart bottom;
    private final ModelPart mid;
    private final ModelPart top;
    private final ModelPart tentacle1;
    private final ModelPart tentacle2;
    private final ModelPart tentacle3;
    private final ModelPart tentacle4;
    private final ModelPart bb_main;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation walkAnimation;
    private final KeyframeAnimation attackAnimation;
    private final KeyframeAnimation shootAnimation;

    public BabyFriendlyKombuchaMonsterModel(ModelPart root) {
        super(root);
        this.bottom = root.getChild("bottom");
        this.mid = root.getChild("mid");
        this.top = root.getChild("top");
        this.tentacle1 = root.getChild("tentacle1");
        this.tentacle2 = root.getChild("tentacle2");
        this.tentacle3 = root.getChild("tentacle3");
        this.tentacle4 = root.getChild("tentacle4");
        this.bb_main = root.getChild("bb_main");
        this.idleAnimation = baby_combucha_monsterAnimation.idle.bake(root);
        this.walkAnimation = baby_combucha_monsterAnimation.walk.bake(root);
        this.attackAnimation = baby_combucha_monsterAnimation.attack.bake(root);
        this.shootAnimation = baby_combucha_monsterAnimation.shoot.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 10.0F,
                        new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 0.0F));

        partdefinition.addOrReplaceChild("mid", CubeListBuilder.create()
                .texOffs(0, 29).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 1.0F, 6.0F,
                        new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 1.0F, 8.0F,
                        new CubeDeformation(0.0F))
                .texOffs(24, 29).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F,
                        new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));

        partdefinition.addOrReplaceChild("top", CubeListBuilder.create()
                .texOffs(0, 20).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 8.0F,
                        new CubeDeformation(0.0F))
                .texOffs(32, 11).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 1.0F, 6.0F,
                        new CubeDeformation(0.0F))
                .texOffs(32, 18).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F,
                        new CubeDeformation(0.0F))
                .texOffs(32, 23).addBox(2.0F, -4.0F, -2.0F, 2.0F, 2.0F, 2.0F,
                        new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-4.0F, -4.0F, -2.0F, 2.0F, 2.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

        PartDefinition tentacle1 = partdefinition.addOrReplaceChild("tentacle1", CubeListBuilder.create()
                .texOffs(8, 36).addBox(0.0F, -1.3F, -1.6F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 23.3F, -5.15F,
                        0.0F, 0.2182F, 0.0F));
        tentacle1.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(14, 36).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.7F, -2.45F,
                        -0.4363F, 0.0F, 0.0F));
        tentacle1.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                .texOffs(20, 36).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, -4.0F,
                        -0.9599F, 0.0F, 0.0F));

        PartDefinition tentacle2 = partdefinition.addOrReplaceChild("tentacle2", CubeListBuilder.create()
                .texOffs(32, 36).addBox(0.0F, -1.3F, -1.6F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 23.3F, -5.4F,
                        0.0F, -0.2182F, 0.0F));
        tentacle2.addOrReplaceChild("cube_r3", CubeListBuilder.create()
                .texOffs(38, 36).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.7F, -2.45F,
                        -0.4363F, 0.0F, 0.0F));
        tentacle2.addOrReplaceChild("cube_r4", CubeListBuilder.create()
                .texOffs(26, 36).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, -4.0F,
                        -0.9599F, 0.0F, 0.0F));

        PartDefinition tentacle3 = partdefinition.addOrReplaceChild("tentacle3", CubeListBuilder.create()
                .texOffs(14, 39).addBox(-1.0F, 0.7F, 2.4F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4F, 21.3F, -0.9F,
                        0.0F, 1.5708F, 0.0F));
        tentacle3.addOrReplaceChild("cube_r5", CubeListBuilder.create()
                .texOffs(20, 39).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.3F, 1.55F,
                        -0.4363F, 0.0F, 0.0F));
        tentacle3.addOrReplaceChild("cube_r6", CubeListBuilder.create()
                .texOffs(8, 39).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        -0.9599F, 0.0F, 0.0F));

        PartDefinition tentacle4 = partdefinition.addOrReplaceChild("tentacle4", CubeListBuilder.create()
                .texOffs(38, 39).addBox(-1.0F, 0.7F, 2.4F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.4F, 21.3F, -0.05F,
                        0.0F, -1.5708F, 0.0F));
        tentacle4.addOrReplaceChild("cube_r7", CubeListBuilder.create()
                .texOffs(32, 39).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.3F, 1.55F,
                        -0.4363F, 0.0F, 0.0F));
        tentacle4.addOrReplaceChild("cube_r8", CubeListBuilder.create()
                .texOffs(26, 39).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
                        -0.9599F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
                .texOffs(1, 1).addBox(-0.5F, -6.0F, -5.0F, 1.0F, 1.0F, 1.0F,
                        new CubeDeformation(0.0F))
                .texOffs(1, 1).addBox(1.5F, -6.0F, -5.0F, 1.0F, 1.0F, 1.0F,
                        new CubeDeformation(0.0F))
                .texOffs(1, 1).addBox(-2.5F, -6.0F, -5.0F, 1.0F, 1.0F, 1.0F,
                        new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.idleAnimation.apply((long) (state.ageInTicks * 50.0F), 1.0F);
        this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.5F, 4.0F);
        this.bb_main.yRot = Mth.clamp(state.yRot, -35.0F, 35.0F) * Mth.DEG_TO_RAD;
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.attackTime > 0.0F) {
            this.attackAnimation.apply((long) (combuchaState.attackTime * 1125.0F), 1.0F);
        }
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.shootTime > 0) {
            this.shootAnimation.apply((long) ((8 - combuchaState.shootTime) * 50L), 1.0F);
        }
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.isJumping) {
            this.tentacle1.xRot += 0.35F;
            this.tentacle2.xRot += 0.35F;
            this.tentacle3.xRot += 0.35F;
            this.tentacle4.xRot += 0.35F;
        }
    }
}
