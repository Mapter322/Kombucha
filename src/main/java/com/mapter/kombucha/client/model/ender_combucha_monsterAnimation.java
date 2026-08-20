package com.mapter.kombucha.client.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public final class ender_combucha_monsterAnimation {
    private ender_combucha_monsterAnimation() {
    }

    private static Keyframe rotation(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.degreeVec(x, y, z), AnimationChannel.Interpolations.LINEAR);
    }

    private static AnimationChannel rotation(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION, keyframes);
    }

    public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(2.0F).looping()
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("mid", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.75F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("bottom", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, 0.0F, -5.0F, 0.0F),
                    rotation(1.5F, 0.0F, 5.0F, 0.0F),
                    rotation(2.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle5", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.75F, 0.0F, 5.0F, 0.0F),
                    rotation(1.75F, 0.0F, -2.5F, 0.0F),
                    rotation(2.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.5F, 0.0F, 5.0F, 0.0F),
                    rotation(1.25F, 0.0F, -2.5F, 0.0F),
                    rotation(2.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle4", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.75F, 0.0F, 7.5F, 0.0F),
                    rotation(1.5F, 0.0F, -5.0F, 0.0F),
                    rotation(2.0F, 0.0F, 0.0F, 0.0F)))
            .build();

    public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2083F, 0.0F, -15.0F, 0.0F),
                    rotation(0.75F, 0.0F, 15.0F, 0.0F),
                    rotation(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle5", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2083F, 0.0F, 15.0F, 0.0F),
                    rotation(0.75F, 0.0F, -15.0F, 0.0F),
                    rotation(1.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotation(
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

    public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(0.8F).looping()
            .addAnimation("tentacle3", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2F, 0.0F, -35.0F, 0.0F),
                    rotation(0.55F, 0.0F, 35.0F, 0.0F),
                    rotation(0.8F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2F, 0.0F, 35.0F, 0.0F),
                    rotation(0.55F, 0.0F, -35.0F, 0.0F),
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
