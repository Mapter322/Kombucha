package com.mapter.kombucha.item;

import com.mapter.kombucha.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class EmptyKombuchaBottleItem extends Item {
    public EmptyKombuchaBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        level.playSound(player, player.blockPosition(), ModSounds.EMPTY_BOTTLE_WHISTLE.get(),
                SoundSource.PLAYERS, 1.0F, 1.3F + (float) (player.getLookAngle().y / 2F));
        return InteractionResult.SUCCESS;
    }
}
