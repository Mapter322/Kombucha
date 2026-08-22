package com.mapter.kombucha.client.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public final class ender_combucha_monsterAnimation {
    private ender_combucha_monsterAnimation() {
    }

    private static Keyframe position(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.posVec(x, y, z), AnimationChannel.Interpolations.LINEAR);
    }

    private static Keyframe rotation(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.degreeVec(x, y, z), AnimationChannel.Interpolations.LINEAR);
    }

    private static AnimationChannel position(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.POSITION, keyframes);
    }

    private static AnimationChannel rotation(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION, keyframes);
    }

    public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(2.0F).looping()
            .addAnimation("head", position(
                    position(0.0F, 0.0F, 0.0F, 0.0F),
                    position(1.0F, 0.0F, -1.0F, 0.0F),
                    position(2.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mid", position(
                    position(0.0F, 0.0F, 0.0F, 0.0F),
                    position(1.0F, 0.0F, -0.75F, 0.0F),
                    position(2.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("bottom", position(
                    position(0.0F, 0.0F, 0.0F, 0.0F),
                    position(1.0F, 0.0F, -0.5F, 0.0F),
                    position(2.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, -2.0F, -1.0F),
                    rotation(0.75F, 0.75F, 2.0F, 1.0F),
                    rotation(1.75F, -0.75F, 2.0F, 1.0F),
                    rotation(2.0F, 0.0F, -2.0F, -1.0F)))
            .addAnimation("tentacle5", rotation(
                    rotation(0.0F, 0.0F, 4.0F, -2.0F),
                    rotation(0.75F, -1.5F, -4.0F, 2.0F),
                    rotation(1.75F, 1.5F, -4.0F, 2.0F),
                    rotation(2.0F, 0.0F, 4.0F, -2.0F)))
            .addAnimation("tentacle2", rotation(
                    rotation(0.0F, 0.0F, 2.0F, -1.0F),
                    rotation(0.75F, -0.75F, -2.0F, 1.0F),
                    rotation(1.75F, 0.75F, -2.0F, 1.0F),
                    rotation(2.0F, 0.0F, 2.0F, -1.0F)))
            .addAnimation("tentacle4", rotation(
                    rotation(0.0F, 0.0F, -4.0F, -2.0F),
                    rotation(0.75F, 1.5F, 4.0F, 2.0F),
                    rotation(1.75F, -1.5F, 4.0F, 2.0F),
                    rotation(2.0F, 0.0F, -4.0F, -2.0F)))
            .build();

    public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.1667F).looping()
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.25F, 0.0F, 17.5F, 0.0F),
                    rotation(0.4167F, 0.0F, 32.5F, 0.0F),
                    rotation(0.5417F, 0.0F, 42.5F, 0.0F),
                    rotation(0.75F, 0.0F, 17.5F, 0.0F),
                    rotation(1.0F, 0.0F, -5.0F, 0.0F),
                    rotation(1.1667F, 0.0F, 12.5F, 0.0F)))
            .addAnimation("tentacle5", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.25F, 0.0F, -15.0F, 0.0F),
                    rotation(0.4167F, 0.0F, -25.0F, 0.0F),
                    rotation(0.5417F, 0.0F, -27.5F, 0.0F),
                    rotation(0.75F, 0.0F, -2.5F, 0.0F),
                    rotation(1.0F, 0.0F, 15.0F, 0.0F),
                    rotation(1.1667F, 0.0F, 2.5F, 0.0F)))
            .addAnimation("tentacle2", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.25F, 0.0F, 20.0F, 0.0F),
                    rotation(0.4167F, 0.0F, 37.5F, 0.0F),
                    rotation(0.5417F, 0.0F, 47.5F, 0.0F),
                    rotation(0.75F, 0.0F, 22.5F, 0.0F),
                    rotation(1.0F, 0.0F, -15.0F, 0.0F),
                    rotation(1.1667F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle4", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.25F, 0.0F, -17.5F, 0.0F),
                    rotation(0.4167F, 0.0F, -37.5F, 0.0F),
                    rotation(0.5417F, 0.0F, -45.0F, 0.0F),
                    rotation(0.75F, 0.0F, -20.0F, 0.0F),
                    rotation(1.0F, 0.0F, -12.5F, 0.0F),
                    rotation(1.1667F, 0.0F, 2.5F, 0.0F)))
            .build();

    public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(0.8F).looping()
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2F, 0.0F, -35.0F, 0.0F),
                    rotation(0.55F, 0.0F, 35.0F, 0.0F),
                    rotation(0.8F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle5", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2F, 0.0F, 35.0F, 0.0F),
                    rotation(0.55F, 0.0F, -35.0F, 0.0F),
                    rotation(0.8F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2F, 0.0F, 35.0F, 0.0F),
                    rotation(0.55F, 0.0F, -35.0F, 0.0F),
                    rotation(0.8F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle4", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2F, 0.0F, -35.0F, 0.0F),
                    rotation(0.55F, 0.0F, 35.0F, 0.0F),
                    rotation(0.8F, 0.0F, 0.0F, 0.0F)))
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
}
