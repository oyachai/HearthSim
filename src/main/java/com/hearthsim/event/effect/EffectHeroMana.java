package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectHeroMana<T extends Card> implements EffectHero<T> {
    private final byte manaDelta;
    private final byte maxManaDelta;

    public EffectHeroMana(int manaDelta) {
        this(manaDelta, 0);
    }

    public EffectHeroMana(int manaDelta, int maxManaDelta) {
        this.manaDelta = (byte) manaDelta;
        this.maxManaDelta = (byte) maxManaDelta;
    }

    public HearthTreeNode applyEffect(PlayerSide targetSide, HearthTreeNode boardState) {
        if (this.manaDelta != 0) {
            byte value = boardState.data_.modelForSide(targetSide).getMana();
            value = (byte)Math.min(value + this.manaDelta, 10);
            value = (byte)Math.max(value, 0);
            boardState.data_.modelForSide(targetSide).setMana(value);
        }

        if (this.maxManaDelta != 0) {
            byte value = boardState.data_.modelForSide(targetSide).getMaxMana();
            value = (byte)Math.min(value + this.maxManaDelta, 10);
            value = (byte)Math.max(value, 0);
            boardState.data_.modelForSide(targetSide).setMaxMana(value);
        }
        return boardState;
    }
}
