package com.mapter.kombucha.effect;

import com.mapter.kombucha.Kombucha;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Kombucha.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> FALL_IMMUNITY =
            EFFECTS.register("fall_immunity", FallImmunityMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> KOMBUCHA_FRIEND =
            EFFECTS.register("kombucha_friend", KombuchaFriendMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> KOMBUCHA_IDOL =
            EFFECTS.register("kombucha_idol", KombuchaIdolMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> VAMPIRISM =
            EFFECTS.register("vampirism", VampirismMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> HALF_SIZE =
            EFFECTS.register("half_size", HalfSizeMobEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> FLIGHT =
            EFFECTS.register("flight", FlightMobEffect::new);

    private ModEffects() {
    }
}
