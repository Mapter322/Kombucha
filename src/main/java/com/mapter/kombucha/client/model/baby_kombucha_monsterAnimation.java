package com.mapter.kombucha.client.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public final class baby_kombucha_monsterAnimation {
    private baby_kombucha_monsterAnimation() {
    }

    private static Keyframe rotation(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.degreeVec(x, y, z), AnimationChannel.Interpolations.LINEAR);
    }

    private static Keyframe position(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.posVec(x, y, z), AnimationChannel.Interpolations.LINEAR);
    }

    private static AnimationChannel rotationChannel(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION, keyframes);
    }

    private static AnimationChannel positionChannel(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.POSITION, keyframes);
    }

    public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(4.0F).looping()
            .addAnimation("top", positionChannel(
                    position(0.0F, 0.0F, 0.0F, 0.0F),
                    position(1.0F, 0.0F, -0.8F, 0.0F),
                    position(2.0F, 0.0F, 0.0F, 0.0F),
                    position(3.0F, 0.0F, 0.5F, 0.0F),
                    position(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("mid", positionChannel(
                    position(0.0F, 0.0F, 0.0F, 0.0F),
                    position(1.0F, 0.0F, -0.5F, 0.0F),
                    position(2.0F, 0.0F, 0.0F, 0.0F),
                    position(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle1", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(1.0F, 8.0F, 0.0F, 0.0F),
                    rotation(2.0F, -8.0F, 0.0F, 0.0F), rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(1.0F, -8.0F, 0.0F, 0.0F),
                    rotation(2.0F, 8.0F, 0.0F, 0.0F), rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle3", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(1.0F, 6.0F, 0.0F, 0.0F),
                    rotation(2.0F, -6.0F, 0.0F, 0.0F), rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle4", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(1.0F, -6.0F, 0.0F, 0.0F),
                    rotation(2.0F, 6.0F, 0.0F, 0.0F), rotation(4.0F, 0.0F, 0.0F, 0.0F)))
            .build();

    public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("tentacle1", rotationChannel(
                    rotation(0.0F, 0.0F, -12.0F, 0.0F), rotation(0.5F, 0.0F, 12.0F, 0.0F),
                    rotation(1.0F, 0.0F, -12.0F, 0.0F)))
            .addAnimation("tentacle2", rotationChannel(
                    rotation(0.0F, 0.0F, 12.0F, 0.0F), rotation(0.5F, 0.0F, -12.0F, 0.0F),
                    rotation(1.0F, 0.0F, 12.0F, 0.0F)))
            .addAnimation("tentacle3", rotationChannel(
                    rotation(0.0F, 0.0F, 10.0F, 0.0F), rotation(0.5F, 0.0F, -10.0F, 0.0F),
                    rotation(1.0F, 0.0F, 10.0F, 0.0F)))
            .addAnimation("tentacle4", rotationChannel(
                    rotation(0.0F, 0.0F, -10.0F, 0.0F), rotation(0.5F, 0.0F, 10.0F, 0.0F),
                    rotation(1.0F, 0.0F, -10.0F, 0.0F)))
            .build();

    public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(1.125F).looping()
            .addAnimation("tentacle1", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(0.5F, -15.0F, 0.0F, 0.0F),
                    rotation(0.625F, 20.0F, 0.0F, 0.0F), rotation(1.125F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle2", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(0.5F, -15.0F, 0.0F, 0.0F),
                    rotation(0.625F, 20.0F, 0.0F, 0.0F), rotation(1.125F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle3", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(0.5F, -10.0F, 0.0F, 0.0F),
                    rotation(0.625F, 15.0F, 0.0F, 0.0F), rotation(1.125F, 0.0F, 0.0F, 0.0F)))
            .addAnimation("tentacle4", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(0.5F, -10.0F, 0.0F, 0.0F),
                    rotation(0.625F, 15.0F, 0.0F, 0.0F), rotation(1.125F, 0.0F, 0.0F, 0.0F)))
            .build();

    public static final AnimationDefinition shoot = AnimationDefinition.Builder.withLength(0.4F).looping()
            .addAnimation("top", rotationChannel(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F), rotation(0.18F, -12.0F, 0.0F, 0.0F),
                    rotation(0.24F, 8.0F, 0.0F, 0.0F), rotation(0.4F, 0.0F, 0.0F, 0.0F)))
            .build();
}
