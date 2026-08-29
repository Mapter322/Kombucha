package com.mapter.kombucha;

import com.mapter.kombucha.effect.ModEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Kombucha.MODID)
public final class KombuchaEvents {
    private KombuchaEvents() {
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof Player player)
                || !player.hasEffect(ModEffects.VAMPIRISM)
                || event.getHealthDamage() <= 0.0F) {
            return;
        }

        int amplifier = player.getEffect(ModEffects.VAMPIRISM).getAmplifier();
        player.heal(event.getHealthDamage() * 0.1F * (amplifier + 1));
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }

        boolean flightEffect = player.hasEffect(ModEffects.FLIGHT);
        boolean vanillaFlight = player.isCreative() || player.isSpectator();
        boolean shouldAllowFlight = flightEffect || vanillaFlight;

        if (player.getAbilities().mayfly != shouldAllowFlight) {
            player.getAbilities().mayfly = shouldAllowFlight;
            player.onUpdateAbilities();
        }

        if (!shouldAllowFlight && player.getAbilities().flying) {
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
    }
}
