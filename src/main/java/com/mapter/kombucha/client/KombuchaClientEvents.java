package com.mapter.kombucha.client;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.client.screen.FriendlyKombuchaScreen;
import com.mapter.kombucha.entity.FriendlyKombuchaMonster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
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
                || event.getItemStack().is(Items.SUGAR)
                || isKombuchaMushroom(event.getItemStack())
                || isRegenerationMushroom(event.getItemStack())
                || !kombucha.isTame()
                || kombucha.isBaby()) {
            return;
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
        Minecraft.getInstance().gui.setScreen(new FriendlyKombuchaScreen(kombucha));
    }

    private static boolean isKombuchaMushroom(ItemStack stack) {
        return stack.is(Kombucha.KOMBUCHA_SHROOM.get())
                || stack.is(Kombucha.GOLDEN_KOMBUCHA_SHROOM.get())
                || stack.is(Kombucha.NETHER_KOMBUCHA_SHROOM.get())
                || stack.is(Kombucha.ENDER_KOMBUCHA_SHROOM.get())
                || stack.is(Kombucha.LIVING_KOMBUCHA_SHROOM.get());
    }

    private static boolean isRegenerationMushroom(ItemStack stack) {
        return stack.is(Kombucha.ENDER_KOMBUCHA_SHROOM.get());
    }
}
