package com.mapter.kombucha.client.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public final class cave_kombucha_monsterAnimation {
    private cave_kombucha_monsterAnimation() {
    }

    private static Keyframe rotation(float time, float x, float y, float z) {
        return new Keyframe(time, KeyframeAnimations.degreeVec(x, y, z), AnimationChannel.Interpolations.LINEAR);
    }

    private static AnimationChannel rotation(Keyframe... keyframes) {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION, keyframes);
    }

    public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.0F).looping()
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
            .addAnimation("tentacle5", rotation(
                    rotation(0.0F, 0.0F, 0.0F, 0.0F),
                    rotation(0.2083F, 0.0F, -15.0F, 0.0F),
                    rotation(0.75F, 0.0F, 15.0F, 0.0F),
                    rotation(1.0F, 0.0F, 0.0F, 0.0F)))
            .build();
}
