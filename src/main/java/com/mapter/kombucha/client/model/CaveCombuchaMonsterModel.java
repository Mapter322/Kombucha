package com.mapter.kombucha.client.model;

import com.mapter.kombucha.Kombucha;
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

public class CaveCombuchaMonsterModel extends EntityModel<LivingEntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Kombucha.MODID, "cave_combucha_monster"), "main");

    private final ModelPart head;
    private final ModelPart mouth_tentacle;
    private final ModelPart mouth_tentacle2;
    private final ModelPart mouth_tentacle3;
    private final ModelPart mouth_tentacle4;
    private final ModelPart mid;
    private final ModelPart bottom;
    private final ModelPart tentacle;
    private final ModelPart tentacle2;
    private final ModelPart tentacle3;
    private final ModelPart tentacle4;

    public CaveCombuchaMonsterModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.mouth_tentacle = this.head.getChild("mouth_tentacle");
        this.mouth_tentacle2 = this.head.getChild("mouth_tentacle2");
        this.mouth_tentacle3 = this.head.getChild("mouth_tentacle3");
        this.mouth_tentacle4 = this.head.getChild("mouth_tentacle4");
        this.mid = root.getChild("mid");
        this.bottom = root.getChild("bottom");
        this.tentacle = root.getChild("tentacle");
        this.tentacle2 = root.getChild("tentacle2");
        this.tentacle3 = root.getChild("tentacle3");
        this.tentacle4 = root.getChild("tentacle4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 36).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 17.0F, new CubeDeformation(0.0F))
        .texOffs(12, 72).addBox(-8.0F, -4.0F, -8.0F, 16.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
        .texOffs(74, 55).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
        .texOffs(64, 97).addBox(-5.0F, -8.0F, -4.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(76, 97).addBox(2.0F, -8.0F, -4.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition cloth_r1 = head.addOrReplaceChild("cloth_r1", CubeListBuilder.create().texOffs(74, 70).addBox(-6.0F, -1.0F, 2.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.75F, 0.0F, -0.4913F, 0.7092F, -0.0769F));

        PartDefinition glass_shard_r1 = head.addOrReplaceChild("glass_shard_r1", CubeListBuilder.create().texOffs(28, 97).addBox(1.0F, -1.0F, 1.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, 1.0F, 8.0F, 0.7006F, 0.5847F, 0.186F));

        PartDefinition mouth_tentacle = head.addOrReplaceChild("mouth_tentacle", CubeListBuilder.create().texOffs(92, 89).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -1.0F, -10.0F, 0.0F, 0.1745F, 0.0F));

        PartDefinition cube_r1 = mouth_tentacle.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(92, 92).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition mouth_tentacle2 = head.addOrReplaceChild("mouth_tentacle2", CubeListBuilder.create().texOffs(88, 97).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.0F, -10.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition cube_r2 = mouth_tentacle2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(94, 95).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition mouth_tentacle3 = head.addOrReplaceChild("mouth_tentacle3", CubeListBuilder.create().texOffs(94, 101).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -1.0F, -10.0F, 0.0F, -0.0873F, 0.0F));

        PartDefinition cube_r3 = mouth_tentacle3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 102).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition mouth_tentacle4 = head.addOrReplaceChild("mouth_tentacle4", CubeListBuilder.create().texOffs(94, 98).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.0F, -9.75F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r4 = mouth_tentacle4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(88, 100).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition mid = partdefinition.addOrReplaceChild("mid", CubeListBuilder.create().texOffs(12, 55).addBox(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition cube_r5 = mid.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(15, 57).addBox(-7.0F, -2.0F, -7.0F, 13.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r6 = mid.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(15, 57).addBox(-7.0F, -2.0F, -7.0F, 13.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition glass_shard_r2 = mid.addOrReplaceChild("glass_shard_r2", CubeListBuilder.create().texOffs(82, 49).addBox(1.0F, -1.0F, 1.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, -6.0F, 1.0564F, 0.2507F, 0.4137F));

        PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(12, 17).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));

        PartDefinition glass_shard_r3 = bottom.addOrReplaceChild("glass_shard_r3", CubeListBuilder.create().texOffs(46, 97).addBox(1.0F, -1.0F, 1.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, 7.0F, 2.2327F, -1.2684F, -1.2591F));

        PartDefinition tentacle = partdefinition.addOrReplaceChild("tentacle", CubeListBuilder.create().texOffs(74, 81).addBox(-13.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 23.0F, -11.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition cube_r7 = tentacle.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(82, 25).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -3.75F, -11.0F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r8 = tentacle.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(82, 17).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -0.5F, -6.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition tentacle2 = partdefinition.addOrReplaceChild("tentacle2", CubeListBuilder.create().texOffs(82, 33).addBox(-13.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, 23.0F, -6.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition cube_r9 = tentacle2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(12, 89).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -3.75F, -11.0F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r10 = tentacle2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(82, 41).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -0.5F, -6.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition tentacle3 = partdefinition.addOrReplaceChild("tentacle3", CubeListBuilder.create().texOffs(28, 89).addBox(-0.5412F, -2.0F, -6.3338F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 23.0F, -1.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition cube_r11 = tentacle3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(60, 89).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4588F, -3.75F, -12.3338F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r12 = tentacle3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(44, 89).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4588F, -0.5F, -7.3338F, -0.48F, 0.0F, 0.0F));

        PartDefinition tentacle4 = partdefinition.addOrReplaceChild("tentacle4", CubeListBuilder.create().texOffs(76, 89).addBox(-0.7654F, -2.0F, -6.8206F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 23.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition cube_r13 = tentacle4.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(12, 97).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2346F, -3.75F, -12.8206F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r14 = tentacle4.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(90, 81).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2346F, -0.5F, -7.8206F, -0.48F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
