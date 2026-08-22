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

public class EnderCombuchaMonsterModel extends EntityModel<LivingEntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Kombucha.MODID, "ender_combucha_monster"), "main");

    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart mouth_tentacle;
    private final ModelPart mouth_tentacle2;
    private final ModelPart mouth_tentacle3;
    private final ModelPart mouth_tentacle4;
    private final ModelPart mid;
    private final ModelPart bottom;
    private final ModelPart tentacle3;
    private final ModelPart tentacle2;
    private final ModelPart tentacle4;
    private final ModelPart tentacle5;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation attackAnimation;
    private final KeyframeAnimation shootAnimation;
    private final KeyframeAnimation walkAnimation;

    public EnderCombuchaMonsterModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.eyes = this.head.getChild("eyes");
        this.mouth_tentacle = this.head.getChild("mouth_tentacle");
        this.mouth_tentacle2 = this.head.getChild("mouth_tentacle2");
        this.mouth_tentacle3 = this.head.getChild("mouth_tentacle3");
        this.mouth_tentacle4 = this.head.getChild("mouth_tentacle4");
        this.mid = root.getChild("mid");
        this.bottom = root.getChild("bottom");
        this.tentacle3 = root.getChild("tentacle3");
        this.tentacle2 = root.getChild("tentacle2");
        this.tentacle4 = root.getChild("tentacle4");
        this.tentacle5 = root.getChild("tentacle5");
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
                .texOffs(62, 38).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 14.0F, 0.0F));

        head.addOrReplaceChild("eyes", CubeListBuilder.create()
                .texOffs(48, 72).addBox(-5.0F, -8.0F, -4.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 77).addBox(2.0F, -8.0F, -4.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

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

        PartDefinition tentacle3 = partdefinition.addOrReplaceChild("tentacle3", CubeListBuilder.create(),
                PartPose.offsetAndRotation(8.0F, 22.0F, 6.0F, 3.0273F, -1.4377F, -2.8544F));
        addSegment(tentacle3, "cube_r9", 70, 0, -1.0F, 6.3745F, -14.1886F,
                0.4588F, -0.5F, -0.3338F, -0.8027F, -0.5148F, 0.1619F);
        addSegment(tentacle3, "cube_r10", 16, 72, -1.0F, 1.2322F, -11.2091F,
                0.4588F, -0.5F, -0.3338F, -0.2791F, -0.5148F, 0.1619F);
        addSegment(tentacle3, "cube_r11", 0, 72, -1.0F, -1.5F, -6.0F,
                0.4588F, -0.5F, -0.3338F, 0.2009F, -0.5148F, 0.1619F);

        PartDefinition tentacle2 = partdefinition.addOrReplaceChild("tentacle2", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-8.0F, 22.0F, 6.0F, 3.0273F, 1.4377F, 2.8544F));
        addSegment(tentacle2, "cube_r12", 0, 80, -2.0F, 0.7887F, -11.44F,
                -0.4588F, 0.0F, -0.3338F, -0.1125F, 0.5788F, 0.2075F);
        addSegment(tentacle2, "cube_r13", 32, 72, -2.0F, -2.0F, -6.0F,
                -0.4588F, 0.0F, -0.3338F, 0.3674F, 0.5788F, 0.2075F);
        addSegment(tentacle2, "cube_r14", 70, 8, -2.0F, 6.1058F, -14.6103F,
                -0.4588F, 0.0F, -0.3338F, -0.6361F, 0.5788F, 0.2075F);

        PartDefinition tentacle4 = partdefinition.addOrReplaceChild("tentacle4", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-7.0F, 22.0F, -7.0F, 0.0387F, -0.2213F, -0.0093F));
        addSegment(tentacle4, "cube_r15", 32, 80, -1.0F, 0.7887F, -11.44F,
                -1.4588F, 0.0F, -0.3338F, 0.4311F, 0.9016F, 0.7227F);
        addSegment(tentacle4, "cube_r16", 16, 80, -1.0F, -2.0F, -6.0F,
                -1.4588F, 0.0F, -0.3338F, 0.911F, 0.9016F, 0.7227F);
        addSegment(tentacle4, "cube_r17", 70, 16, -1.0F, 6.1058F, -14.6103F,
                -1.4588F, 0.0F, -0.3338F, -0.0925F, 0.9016F, 0.7227F);

        PartDefinition tentacle5 = partdefinition.addOrReplaceChild("tentacle5", CubeListBuilder.create(),
                PartPose.offsetAndRotation(8.0F, 22.0F, -7.0F, 0.0396F, -0.3019F, -0.011F));
        addSegment(tentacle5, "cube_r18", 64, 83, -2.0F, 0.327F, -10.5529F,
                1.4588F, 0.0F, -0.3338F, -0.0234F, -0.5194F, -0.2391F);
        addSegment(tentacle5, "cube_r19", 48, 83, -2.0F, -2.0F, -5.0F,
                1.4588F, 0.0F, -0.3338F, 0.4565F, -0.5194F, -0.2391F);
        addSegment(tentacle5, "cube_r20", 70, 24, -2.0F, 5.2624F, -14.073F,
                1.4588F, 0.0F, -0.3338F, -0.547F, -0.5194F, -0.2391F);

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

    private static void addSegment(PartDefinition parent, String name, int textureX, int textureY,
                                   float boxX, float boxY, float boxZ, float offsetX, float offsetY, float offsetZ,
                                   float rotationX, float rotationY, float rotationZ) {
        parent.addOrReplaceChild(name, CubeListBuilder.create().texOffs(textureX, textureY)
                        .addBox(boxX, boxY, boxZ, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(offsetX, offsetY, offsetZ, rotationX, rotationY, rotationZ));
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
            this.tentacle3.xRot += 0.45F;
            this.tentacle2.xRot += 0.45F;
            this.tentacle4.xRot += 0.45F;
            this.tentacle5.xRot += 0.45F;
        }
    }
}
