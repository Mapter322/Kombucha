package com.mapter.kombucha.client;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.screen.FriendlyKombuchaScreen;
import com.mapter.kombucha.entity.FriendlyKombuchaMonster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Kombucha.MODID, value = Dist.CLIENT)
public final class KombuchaClientEvents {
    private KombuchaClientEvents() {
    }

    @SubscribeEvent
    public static void onFriendlyKombuchaInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getEntity() instanceof LocalPlayer)
                || !(event.getTarget() instanceof FriendlyKombuchaMonster kombucha)
                || !kombucha.isTame()
                || event.getItemStack().is(Items.SUGAR)) {
            return;
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
        Minecraft.getInstance().setScreen(new FriendlyKombuchaScreen(kombucha));
    }
}
