package com.mapter.kombucha.entity;

import com.mapter.kombucha.effect.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class CombuchaRelations {
    private CombuchaRelations() {
    }

    public static boolean isFriendOfCombuchas(LivingEntity entity) {
        return entity instanceof Player player
                && (player.hasEffect(ModEffects.COMBUCHA_FRIEND)
                || player.hasEffect(ModEffects.COMBUCHA_IDOL));
    }

    public static boolean isIdolOfCombuchas(LivingEntity entity) {
        return entity instanceof Player player && player.hasEffect(ModEffects.COMBUCHA_IDOL);
    }
}
