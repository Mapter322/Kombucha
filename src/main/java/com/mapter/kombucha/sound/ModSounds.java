package com.mapter.kombucha.sound;

import com.mapter.kombucha.Kombucha;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Kombucha.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> EMPTY_BOTTLE_WHISTLE = SOUNDS.register(
            "item.empty_kombucha_bottle.whistle",
            () -> SoundEvent.createVariableRangeEvent(
                    Identifier.fromNamespaceAndPath(Kombucha.MODID, "item.empty_kombucha_bottle.whistle")));

    private ModSounds() {
    }
}
