package com.mapter.kombucha.client.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class spoiled_combucha_monsterAnimation {
    private static Keyframe position(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.posVec(x, y, z), AnimationChannel.Interpolations.LINEAR);
    }

    private static Keyframe rotation(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.degreeVec(x, y, z), AnimationChannel.Interpolations.LINEAR);
    }

    private static AnimationChannel rotation(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION, keyframes);
    }

    private static AnimationChannel position(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.POSITION, keyframes);
    }

    public static final AnimationDefinition basic = AnimationDefinition.Builder.withLength(4.0F).looping()
            .addAnimation("head", position(
                    position(0.0F, 0.0F, 0.0F, 0.0F),
                    position(1.0F, 0.0F, -2.0F, 0.0F),
                    position(2.0F, 0.0F, 0.0F, 0.0F),
                    position(2.9167F, 0.0F, 1.0F, 0.0F),
                    position(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.75F, 2.5F, 0.0F, 0.0F),
                    rotation(1.625F, -2.5F, 0.0F, 0.0F),
                    rotation(2.625F, 5.0F, 0.0F, 0.0F),
                    rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, 2.5F, 0.0F, 0.0F),
                    rotation(1.25F, -5.0F, 0.0F, 0.0F),
                    rotation(2.2083F, 2.5F, 0.0F, 0.0F),
                    rotation(2.8333F, 0.0F, 0.0F, 0.0F),
                    rotation(3.5417F, -5.0F, 0.0F, 0.0F),
                    rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.6667F, 5.0F, 0.0F, 0.0F),
                    rotation(1.2917F, 2.5F, 0.0F, 0.0F),
                    rotation(1.875F, 5.0F, 0.0F, 0.0F),
                    rotation(2.7917F, 0.0F, 0.0F, 0.0F),
                    rotation(3.4583F, 2.5F, 0.0F, 0.0F),
                    rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle4", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.7083F, -5.0F, 0.0F, 0.0F),
                    rotation(2.0F, 0.0F, 0.0F, 0.0F),
                    rotation(2.6667F, 5.0F, 0.0F, 0.0F),
                    rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, 5.0F, 0.0F, 0.0F),
                    rotation(1.0F, -15.0F, 0.0F, 0.0F),
                    rotation(1.5F, -7.5F, 0.0F, 0.0F),
                    rotation(2.0F, 5.0F, 0.0F, 0.0F),
                    rotation(2.5F, -2.5F, 0.0F, 0.0F),
                    rotation(3.0F, 7.5F, 0.0F, 0.0F),
                    rotation(3.5F, 2.5F, 0.0F, 0.0F),
                    rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle2", rotation(
                    rotation(0.0F, 5.0F, 0.0F, 0.0F),
                    rotation(0.5F, 2.5F, 0.0F, 0.0F),
                    rotation(1.0F, 7.5F, 0.0F, 0.0F),
                    rotation(1.5F, -2.5F, 0.0F, 0.0F),
                    rotation(2.0F, 2.5F, 0.0F, 0.0F),
                    rotation(2.5F, -7.5F, 0.0F, 0.0F),
                    rotation(3.0F, 7.5F, 0.0F, 0.0F),
                    rotation(3.5F, 0.0F, 0.0F, 0.0F),
                    rotation(4.0F, 5.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle4", rotation(
                    rotation(0.0F, 10.0F, 0.0F, 0.0F),
                    rotation(0.5F, 5.0F, 0.0F, 0.0F),
                    rotation(1.0F, 10.0F, 0.0F, 0.0F),
                    rotation(1.5F, 0.0F, 0.0F, 0.0F),
                    rotation(2.0F, 7.5F, 0.0F, 0.0F),
                    rotation(2.5F, -5.0F, 0.0F, 0.0F),
                    rotation(3.0F, 0.0F, 0.0F, 0.0F),
                    rotation(3.5F, -7.5F, 0.0F, 0.0F),
                    rotation(4.0F, 10.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle3", rotation(
                    rotation(0.0F, 10.0F, 0.0F, 0.0F),
                    rotation(0.5F, 2.5F, 0.0F, 0.0F),
                    rotation(1.0F, 15.0F, 0.0F, 0.0F),
                    rotation(1.5F, -10.0F, 0.0F, 0.0F),
                    rotation(2.0F, -2.5F, 0.0F, 0.0F),
                    rotation(2.5F, -12.5F, 0.0F, 0.0F),
                    rotation(3.0F, -2.5F, 0.0F, 0.0F),
                    rotation(3.5F, -7.5F, 0.0F, 0.0F),
                    rotation(4.0F, 10.0F, 0.0F, 0.0F)))
            .addAnimation("bottom", position(
                    position(0.0F, 0.0F, 0.0F, 0.0F),
                    position(1.0F, 0.0F, -1.0F, 0.0F),
                    position(1.8333F, 0.0F, 0.0F, 0.0F),
                    position(3.0F, 0.0F, 0.5F, 0.0F),
                    position(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mid", position(
                    position(0.0F, 0.0F, 0.0F, 0.0F),
                    position(1.0F, 0.0F, -1.0F, 0.0F),
                    position(1.9167F, 0.0F, 0.08F, 0.0F),
                    position(3.0F, 0.0F, 1.08F, 0.0F),
                    position(4.0F, 0.0F, 0.08F, 0.0F)))
            .build();

    public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(1.125F).looping()
            .addAnimation("tentacle", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, -15.0F, 0.0F, 0.0F),
                    rotation(0.625F, 20.0F, 0.0F, 0.0F),
                    rotation(1.125F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, -15.0F, 0.0F, 0.0F),
                    rotation(0.625F, 22.5F, 0.0F, 0.0F),
                    rotation(1.125F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2917F, -2.5F, 0.0F, 0.0F),
                    rotation(0.8333F, -5.0F, 0.0F, 0.0F),
                    rotation(1.125F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle4", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.4167F, 5.0F, 0.0F, 0.0F),
                    rotation(0.8333F, -2.5F, 0.0F, 0.0F),
                    rotation(1.125F, 0.0F, 0.0F, 0.0F)))
            .build();

    public static final AnimationDefinition spit = AnimationDefinition.Builder.withLength(0.75F).looping()
            .addAnimation("mouth_tentacle", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, 0.0F, -22.5F, 0.0F),
                    rotation(0.5833F, 0.0F, 7.5F, 0.0F),
                    rotation(0.75F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle2", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, 0.0F, -17.5F, 0.0F),
                    rotation(0.5833F, 0.0F, 10.0F, 0.0F),
                    rotation(0.75F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle4", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, 0.0F, 22.5F, 0.0F),
                    rotation(0.5833F, 0.0F, -7.5F, 0.0F),
                    rotation(0.75F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, 0.0F, 17.5F, 0.0F),
                    rotation(0.5833F, 0.0F, -10.0F, 0.0F),
                    rotation(0.75F, 0.0F, 0.0F, 0.0F)))
            .build();

    public static final AnimationDefinition shoot = AnimationDefinition.Builder.withLength(0.4F).looping()
            .addAnimation("mouth_tentacle", rotation(
                    rotation(0.0F, -35.0F, 0.0F, 0.0F),
                    rotation(0.18F, -35.0F, 0.0F, 0.0F),
                    rotation(0.24F, 15.0F, 0.0F, 0.0F),
                    rotation(0.4F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle2", rotation(
                    rotation(0.0F, -30.0F, 0.0F, 0.0F),
                    rotation(0.18F, -30.0F, 0.0F, 0.0F),
                    rotation(0.24F, 15.0F, 0.0F, 0.0F),
                    rotation(0.4F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle3", rotation(
                    rotation(0.0F, -30.0F, 0.0F, 0.0F),
                    rotation(0.18F, -30.0F, 0.0F, 0.0F),
                    rotation(0.24F, 15.0F, 0.0F, 0.0F),
                    rotation(0.4F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mouth_tentacle4", rotation(
                    rotation(0.0F, -35.0F, 0.0F, 0.0F),
                    rotation(0.18F, -35.0F, 0.0F, 0.0F),
                    rotation(0.24F, 15.0F, 0.0F, 0.0F),
                    rotation(0.4F, 0.0F, 0.0F, 0.0F)))
            .build();

    public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("tentacle", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2083F, 0.0F, -15.0F, 0.0F),
                    rotation(0.75F, 0.0F, 15.0F, 0.0F),
                    rotation(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2083F, 0.0F, 15.0F, 0.0F),
                    rotation(0.75F, 0.0F, -15.0F, 0.0F),
                    rotation(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.25F, 0.0F, -20.0F, 0.0F),
                    rotation(0.75F, 0.0F, 10.0F, 0.0F),
                    rotation(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle4", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.25F, 0.0F, 20.0F, 0.0F),
                    rotation(0.75F, 0.0F, -12.5F, 0.0F),
                    rotation(1.0F, 0.0F, -2.5F, 0.0F)))
            .build();
}
