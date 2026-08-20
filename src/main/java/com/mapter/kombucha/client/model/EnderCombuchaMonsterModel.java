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

public class EnderCombuchaMonsterModel extends EntityModel<LivingEntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Kombucha.MODID, "ender_combucha_monster"), "main");

    private final ModelPart head;
    private final ModelPart mouth_tentacle;
    private final ModelPart mouth_tentacle2;
    private final ModelPart mouth_tentacle3;
    private final ModelPart mouth_tentacle4;
    private final ModelPart mid;
    private final ModelPart bottom;
    private final ModelPart tentacle3;
    private final ModelPart tentacle5;
    private final ModelPart tentacle2;
    private final ModelPart tentacle4;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation attackAnimation;
    private final KeyframeAnimation shootAnimation;
    private final KeyframeAnimation walkAnimation;

    public EnderCombuchaMonsterModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.mouth_tentacle = this.head.getChild("mouth_tentacle");
        this.mouth_tentacle2 = this.head.getChild("mouth_tentacle2");
        this.mouth_tentacle3 = this.head.getChild("mouth_tentacle3");
        this.mouth_tentacle4 = this.head.getChild("mouth_tentacle4");
        this.mid = root.getChild("mid");
        this.bottom = root.getChild("bottom");
        this.tentacle3 = root.getChild("tentacle3");
        this.tentacle5 = this.tentacle3.getChild("tentacle5");
        this.tentacle2 = root.getChild("tentacle2");
        this.tentacle4 = this.tentacle2.getChild("tentacle4");
        this.idleAnimation = ender_combucha_monsterAnimation.idle.bake(root);
        this.attackAnimation = ender_combucha_monsterAnimation.attack.bake(root);
        this.shootAnimation = ender_combucha_monsterAnimation.shoot.bake(root);
        this.walkAnimation = ender_combucha_monsterAnimation.walk.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-8.0F, -4.0F, -8.0F, 16.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(62, 38).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(48, 72).addBox(-5.0F, -8.0F, -4.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 77).addBox(2.0F, -8.0F, -4.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 14.0F, 0.0F));

        head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(80, 83)
                .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-9.0F, -1.0F, 0.0F, 0.2245F, -0.147F, -1.0409F));
        head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(70, 32)
                .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.0F, -5.0F, 5.0F, -0.2849F, 1.0228F, 0.2849F));

        addMouthTentacle(head, "mouth_tentacle", 86, 0, -2.5F, 0.1745F);
        addMouthTentacle(head, "mouth_tentacle2", 86, 6, -0.5F, 0.0873F);
        addMouthTentacle(head, "mouth_tentacle3", 86, 12, 1.5F, -0.0873F);
        addMouthTentacle(head, "mouth_tentacle4", 86, 18, 3.5F, -0.1745F);

        PartDefinition mid = partdefinition.addOrReplaceChild("mid", CubeListBuilder.create()
                .texOffs(0, 55).addBox(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 18.0F, 0.0F));
        mid.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(62, 68)
                .addBox(-7.0F, -2.0F, -7.0F, 13.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        mid.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(62, 53)
                .addBox(-7.0F, -2.0F, -7.0F, 13.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19)
                .addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 22.0F, 0.0F));

        PartDefinition tentacle3 = partdefinition.addOrReplaceChild("tentacle3", CubeListBuilder.create()
                .texOffs(0, 72).addBox(-0.5412F, -2.0F, -6.3338F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(8.0F, 22.0F, 6.0F, 3.0273F, -1.4377F, -2.8544F));
        addTentacleSegment(tentacle3, "cube_r9", 70, 0, 0.4588F, -3.75F, -12.3338F, -1.0036F);
        addTentacleSegment(tentacle3, "cube_r10", 16, 72, 0.4588F, -0.5F, -7.3338F, -0.48F);

        PartDefinition tentacle5 = tentacle3.addOrReplaceChild("tentacle5", CubeListBuilder.create()
                .texOffs(48, 83).addBox(-0.5412F, -2.0F, -6.3338F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-14.0F, 0.0F, 3.0F, 0.2888F, 1.4377F, 0.2872F));
        addTentacleSegment(tentacle5, "cube_r11", 64, 83, 0.4588F, -0.5F, -7.3338F, -0.48F);
        addTentacleSegment(tentacle5, "cube_r12", 70, 24, 0.4588F, -3.75F, -12.3338F, -1.0036F);

        PartDefinition tentacle2 = partdefinition.addOrReplaceChild("tentacle2", CubeListBuilder.create()
                .texOffs(32, 72).addBox(-1.4588F, -2.0F, -6.3338F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-8.0F, 22.0F, 6.0F, 3.0273F, 1.4377F, 2.8544F));
        addTentacleSegment(tentacle2, "cube_r13", 0, 80, -0.4588F, -0.5F, -7.3338F, -0.48F);
        addTentacleSegment(tentacle2, "cube_r14", 70, 8, -0.4588F, -3.75F, -12.3338F, -1.0036F);

        PartDefinition tentacle4 = tentacle2.addOrReplaceChild("tentacle4", CubeListBuilder.create()
                .texOffs(16, 80).addBox(-1.4588F, -2.0F, -6.3338F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(14.0F, 0.0F, 3.0F, 0.2888F, -1.4377F, -0.2872F));
        addTentacleSegment(tentacle4, "cube_r15", 32, 80, -0.4588F, -0.5F, -7.3338F, -0.48F);
        addTentacleSegment(tentacle4, "cube_r16", 70, 16, -0.4588F, -3.75F, -12.3338F, -1.0036F);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    private static void addMouthTentacle(PartDefinition head, String name, int textureX, int textureY,
                                         float x, float yRotation) {
        PartDefinition tentacle = head.addOrReplaceChild(name, CubeListBuilder.create()
                .texOffs(textureX, textureY).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)), PartPose.offsetAndRotation(x, -1.0F, -10.0F, 0.0F, yRotation, 0.0F));
        tentacle.addOrReplaceChild("segment", CubeListBuilder.create().texOffs(textureX, textureY + 3)
                .addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));
    }

    private static void addTentacleSegment(PartDefinition parent, String name, int textureX, int textureY,
                                           float x, float y, float z, float xRotation) {
        parent.addOrReplaceChild(name, CubeListBuilder.create().texOffs(textureX, textureY)
                .addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(x, y, z, xRotation, 0.0F, 0.0F));
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.idleAnimation.apply((long) (state.ageInTicks * 50.0F), 1.0F);
        this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.5F, 4.0F);
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.attackTime > 0.0F) {
            this.attackAnimation.apply((long) (combuchaState.attackTime * 800.0F), 1.5F);
        }
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.shootTime > 0) {
            long elapsed = (long) ((8 - combuchaState.shootTime) * 50L);
            this.shootAnimation.apply(elapsed, 1.0F);
        }
        if (state instanceof CombuchaMonsterRenderState combuchaState && combuchaState.isJumping) {
            this.tentacle3.xRot += 0.45F;
            this.tentacle5.xRot += 0.45F;
            this.tentacle2.xRot += 0.45F;
            this.tentacle4.xRot += 0.45F;
        }
    }
}
