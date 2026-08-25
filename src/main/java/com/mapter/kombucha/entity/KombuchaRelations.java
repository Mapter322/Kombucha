package com.mapter.kombucha.entity;

import com.mapter.kombucha.effect.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class KombuchaRelations {
    private KombuchaRelations() {
    }

    public static boolean isFriendOfKombuchas(LivingEntity entity) {
        return entity instanceof Player player
                && (player.hasEffect(ModEffects.KOMBUCHA_FRIEND)
                || player.hasEffect(ModEffects.KOMBUCHA_IDOL));
    }

    public static boolean isIdolOfKombuchas(LivingEntity entity) {
        return entity instanceof Player player && player.hasEffect(ModEffects.KOMBUCHA_IDOL);
    }
}
