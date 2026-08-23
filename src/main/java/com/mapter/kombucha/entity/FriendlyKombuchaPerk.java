package com.mapter.kombucha.entity;

public enum FriendlyKombuchaPerk {
    INCREASED_JUMP("screen.kombucha.perk.increased_jump", 3),
    FALL_IMMUNITY("screen.kombucha.perk.fall_immunity", 1),
    REGENERATION("screen.kombucha.perk.regeneration", 3),
    VAMPIRISM("screen.kombucha.perk.vampirism", 3);

    private final String displayNameKey;
    private final int maxLevel;

    FriendlyKombuchaPerk(String displayNameKey, int maxLevel) {
        this.displayNameKey = displayNameKey;
        this.maxLevel = maxLevel;
    }

    public String getDisplayNameKey() {
        return displayNameKey;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
