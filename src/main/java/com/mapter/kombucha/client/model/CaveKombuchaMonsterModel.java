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

public class CaveKombuchaMonsterModel extends EntityModel<LivingEntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Kombucha.MODID, "cave_kombucha_monster"), "main");

    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart mouthTentacles;
    private final ModelPart mouthTentacleGroup;
    private final ModelPart mouthTentacleTip;
    private final ModelPart bottom;
    private final ModelPart mid;
    private final ModelPart tentacle4;
    private final ModelPart tentacle2;
    private final ModelPart tentacle5;
    private final ModelPart tentacle3;
    private final ModelPart bbMain;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation spitAnimation;
    private final KeyframeAnimation walkAnimation;

    public CaveKombuchaMonsterModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.eyes = this.head.getChild("eyes");
        this.mouthTentacles = this.head.getChild("mouth_tentacles");
        this.mouthTentacleGroup = this.mouthTentacles.getChild("cube_r5");
        this.mouthTentacleTip = this.mouthTentacles.getChild("cube_r6");
        this.bottom = root.getChild("bottom");
        this.mid = root.getChild("mid");
        this.tentacle4 = root.getChild("tentacle4");
        this.tentacle2 = root.getChild("tentacle2");
        this.tentacle5 = root.getChild("tentacle5");
        this.tentacle3 = root.getChild("tentacle3");
        this.bbMain = root.getChild("bb_main");
        this.idleAnimation = cave_kombucha_monsterAnimation.idle.bake(root);
        this.spitAnimation = cave_kombucha_monsterAnimation.spit.bake(root);
        this.walkAnimation = cave_kombucha_monsterAnimation.walk.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-4.5F, -1.0F, 1.0F, 14.0F, 2.0F, 14.0F,
                        new CubeDeformation(0.0F))
                .texOffs(0, 46).addBox(-3.5F, -3.0F, 2.0F, 12.0F, 2.0F, 12.0F,
                        new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-2.5F, -5.0F, 3.0F, 10.0F, 2.0F, 10.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offset(-2.5F, 12.0F, -8.0F));

        head.addOrReplaceChild("eyes", CubeListBuilder.create()
                        .texOffs(64, 28).addBox(-1.5F, -6.0F, 4.0F, 2.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(56, 28).addBox(4.5F, -6.0F, 4.0F, 2.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.ZERO);

        head.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                        .texOffs(72, 23).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.5F, -5.0F, 13.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition mouthTentacles = head.addOrReplaceChild("mouth_tentacles", CubeListBuilder.create()
                .texOffs(72, 72).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F))
                .texOffs(48, 75).addBox(1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F))
                .texOffs(54, 75).addBox(3.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F))
                .texOffs(60, 75).addBox(5.0F, -1.0F, -0.75F, 1.0F, 1.0F, 2.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        mouthTentacles.addOrReplaceChild("cube_r5", CubeListBuilder.create()
                        .texOffs(60, 72).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(54, 72).addBox(1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(66, 72).addBox(-3.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));
        mouthTentacles.addOrReplaceChild("cube_r6", CubeListBuilder.create()
                        .texOffs(48, 72).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.0F, -0.5F, -1.5F, -0.5236F, 0.0F, 0.0F));

        root.addOrReplaceChild("bottom", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-7.0F, -2.0F, -7.0F, 14.0F, 2.0F, 14.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        root.addOrReplaceChild("mid", CubeListBuilder.create()
                        .texOffs(48, 44).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 32).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 2.0F, 12.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(48, 32).addBox(-5.0F, 2.0F, -5.0F, 10.0F, 2.0F, 10.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 15.0F, 0.0F));

        addTentacle(root, "tentacle4", 32, 68, 48, 64, 32, 60,
                6.0F, 21.0F, -6.0F, 0.5236F, -0.48F, -0.3927F);
        addTentacle(root, "tentacle2", 16, 68, 16, 60, 0, 60,
                -6.75F, 20.75F, -5.5F, 0.5236F, 0.48F, 0.3927F);
        addTentacle(root, "tentacle5", 64, 56, 0, 68, 64, 64,
                -6.0F, 21.0F, 3.0F, 0.0F, 1.5708F, -0.3927F);
        addTentacle(root, "tentacle3", 56, 12, 48, 56, 56, 20,
                6.0F, 21.0F, 3.0F, 0.0F, -1.5708F, 0.3927F);

        PartDefinition bbMain = root.addOrReplaceChild("bb_main", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        bbMain.addOrReplaceChild("cube_r12", CubeListBuilder.create()
                        .texOffs(72, 29).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, -4.0F, 7.0F, -1.5708F, 0.0F, 0.0F));
        bbMain.addOrReplaceChild("cube_r13", CubeListBuilder.create()
                        .texOffs(72, 26).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, -7.0F, -6.0F, 1.5708F, 0.0F, -3.1416F));
        bbMain.addOrReplaceChild("cube_r14", CubeListBuilder.create()
                        .texOffs(72, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -9.0F, 4.0F, 0.0F, -1.5708F, -1.5708F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    private static void addTentacle(PartDefinition root, String name, int firstU, int firstV,
                                    int secondU, int secondV, int thirdU, int thirdV,
                                    float x, float y, float z, float xRot, float yRot, float zRot) {
        PartDefinition tentacle = root.addOrReplaceChild(name, CubeListBuilder.create()
                        .texOffs(firstU, firstV).addBox(-0.5412F, -2.0F, -6.3338F, 2.0F, 2.0F, 6.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(x, y, z, xRot, yRot, zRot));
        tentacle.addOrReplaceChild("segment_a", CubeListBuilder.create()
                        .texOffs(secondU, secondV).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.4588F, -3.75F, -12.3338F, -1.0036F, 0.0F, 0.0F));
        tentacle.addOrReplaceChild("segment_b", CubeListBuilder.create()
                        .texOffs(thirdU, thirdV).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.4588F, -0.5F, -7.3338F, -0.48F, 0.0F, 0.0F));
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);

        float idle = state.ageInTicks * 0.157F;
        this.head.y += Mth.sin(idle) - 1.0F;
        this.mid.y += Mth.sin(idle) * 0.5F - 0.5F;
        this.bottom.y += Mth.sin(idle) * 0.25F - 0.25F;

        this.idleAnimation.apply((long) (state.ageInTicks * 50.0F), 1.0F);
        this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.5F, 4.0F);
        this.eyes.yRot = Mth.clamp(state.yRot, -20.0F, 20.0F) * Mth.DEG_TO_RAD;

        float faceWiggle = Mth.sin(state.ageInTicks * 0.35F) * 0.12F;
        this.mouthTentacles.yRot += faceWiggle;
        this.mouthTentacleGroup.yRot -= faceWiggle * 1.4F;
        this.mouthTentacleTip.yRot += faceWiggle * 1.7F;

        if (state instanceof CombuchaMonsterRenderState combuchaState) {
            if (combuchaState.attackTime > 0.0F) {
                float attack = Mth.sin(combuchaState.attackTime * Mth.PI);
                this.tentacle2.xRot += attack * 0.35F;
                this.tentacle3.xRot -= attack * 0.35F;
                this.tentacle4.xRot += attack * 0.35F;
                this.tentacle5.xRot -= attack * 0.35F;
            }
            if (combuchaState.shootTime > 0) {
                long elapsed = (long) ((8 - combuchaState.shootTime) * 50L);
                this.spitAnimation.apply(elapsed, 1.0F);

                float spit = Mth.sin(((8.0F - combuchaState.shootTime) / 8.0F) * Mth.PI);
                this.mouthTentacles.yRot += spit * 0.18F;
                this.mouthTentacleGroup.yRot -= spit * 0.3F;
                this.mouthTentacleTip.yRot += spit * 0.36F;
            }
            if (combuchaState.isJumping) {
                this.tentacle2.xRot += 0.45F;
                this.tentacle3.xRot += 0.45F;
                this.tentacle4.xRot += 0.45F;
                this.tentacle5.xRot += 0.45F;
            }
        }
    }

}
