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
 * living kombucha shroom. The experience bar is deliberately not part of it -
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
        FriendlyKombuchaStateData stateData,
        FriendlyKombuchaPerkData perkData) {

    public static final LivingShroomData DEFAULT = new LivingShroomData(
            Optional.empty(), Optional.empty(), 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            FriendlyKombuchaStateData.DEFAULT, FriendlyKombuchaPerkData.DEFAULT);

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
            FriendlyKombuchaStateData.CODEC.optionalFieldOf("state_data", FriendlyKombuchaStateData.DEFAULT)
                    .forGetter(LivingShroomData::stateData),
            FriendlyKombuchaPerkData.CODEC.optionalFieldOf("perk_data", FriendlyKombuchaPerkData.DEFAULT)
                    .forGetter(LivingShroomData::perkData)
    ).apply(instance, LivingShroomData::new));

    public boolean sitting() {
        return stateData.sitting();
    }

    public int movementMode() {
        return stateData.movementMode();
    }

    public int combatMode() {
        return stateData.combatMode();
    }

    public int attackMode() {
        return stateData.attackMode();
    }

    /** The shroom item as it drops on death - carries the whole monster. */
    public ItemStack toItemStack() {
        ItemStack stack = new ItemStack(Kombucha.LIVING_KOMBUCHA_SHROOM.get());
        stack.set(ModDataComponents.LIVING_SHROOM_DATA, this);
        customName.ifPresent(name -> stack.set(DataComponents.CUSTOM_NAME, name));
        return stack;
    }
}
