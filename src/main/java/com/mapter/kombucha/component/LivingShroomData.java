package com.mapter.kombucha.component;

import com.mapter.kombucha.Kombucha;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.ItemStack;

/**
 * Everything that makes a friendly kombucha monster who it is, carried by a
 * living combucha shroom. The experience bar is deliberately not part of it -
 * it resets when the monster comes back to life.
 */
public record LivingShroomData(
        Optional<UUID> ownerUuid,
        Optional<Component> customName,
        int level,
        int upgradePoints,
        int healthUpgrades,
        int speedUpgrades,
        int meleeDamageUpgrades,
        int rangedDamageUpgrades,
        int meleeSpeedUpgrades,
        int rangedSpeedUpgrades,
        int projectileSpeedUpgrades,
        int feedCooldown,
        boolean sitting,
        int movementMode,
        int combatMode,
        int attackMode) {

    public static final LivingShroomData DEFAULT = new LivingShroomData(
            Optional.empty(), Optional.empty(), 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0, 0);

    public static final Codec<LivingShroomData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.optionalFieldOf("owner_uuid").forGetter(LivingShroomData::ownerUuid),
            ComponentSerialization.CODEC.optionalFieldOf("custom_name").forGetter(LivingShroomData::customName),
            Codec.INT.optionalFieldOf("level", 1).forGetter(LivingShroomData::level),
            Codec.INT.optionalFieldOf("upgrade_points", 0).forGetter(LivingShroomData::upgradePoints),
            Codec.INT.optionalFieldOf("health_upgrades", 0).forGetter(LivingShroomData::healthUpgrades),
            Codec.INT.optionalFieldOf("speed_upgrades", 0).forGetter(LivingShroomData::speedUpgrades),
            Codec.INT.optionalFieldOf("melee_damage_upgrades", 0).forGetter(LivingShroomData::meleeDamageUpgrades),
            Codec.INT.optionalFieldOf("ranged_damage_upgrades", 0).forGetter(LivingShroomData::rangedDamageUpgrades),
            Codec.INT.optionalFieldOf("melee_speed_upgrades", 0).forGetter(LivingShroomData::meleeSpeedUpgrades),
            Codec.INT.optionalFieldOf("ranged_speed_upgrades", 0).forGetter(LivingShroomData::rangedSpeedUpgrades),
            Codec.INT.optionalFieldOf("projectile_speed_upgrades", 0).forGetter(LivingShroomData::projectileSpeedUpgrades),
            Codec.INT.optionalFieldOf("feed_cooldown", 0).forGetter(LivingShroomData::feedCooldown),
            Codec.BOOL.optionalFieldOf("sitting", false).forGetter(LivingShroomData::sitting),
            Codec.INT.optionalFieldOf("movement_mode", 0).forGetter(LivingShroomData::movementMode),
            Codec.INT.optionalFieldOf("combat_mode", 0).forGetter(LivingShroomData::combatMode),
            Codec.INT.optionalFieldOf("attack_mode", 0).forGetter(LivingShroomData::attackMode)
    ).apply(instance, LivingShroomData::new));

    /** The shroom item as it drops on death - carries the whole monster. */
    public ItemStack toItemStack() {
        ItemStack stack = new ItemStack(Kombucha.LIVING_COMBUCHA_SHROOM.get());
        stack.set(ModDataComponents.LIVING_SHROOM_DATA, this);
        customName.ifPresent(name -> stack.set(DataComponents.CUSTOM_NAME, name));
        return stack;
    }
}
