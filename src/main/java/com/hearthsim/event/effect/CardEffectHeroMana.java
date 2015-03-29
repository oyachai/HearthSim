package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectHeroMana extends CardEffectHero {
    private byte manaDelta;
    private byte maxManaDelta;

    public CardEffectHeroMana(int manaDelta) {
        this(manaDelta, 0);
    }

    public CardEffectHeroMana(int manaDelta, int maxManaDelta) {
        this.manaDelta = (byte) manaDelta;
        this.maxManaDelta = (byte) maxManaDelta;
    }

    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, HearthTreeNode boardState) {
        if (this.manaDelta > 0) {
            byte current = boardState.data_.modelForSide(targetSide).getMana();
            boardState.data_.modelForSide(targetSide).setMana((byte)Math.min(current + this.manaDelta, 10));
        }

        if (this.maxManaDelta > 0) {
            byte current = boardState.data_.modelForSide(targetSide).getMaxMana();
            boardState.data_.modelForSide(targetSide).setMaxMana((byte) Math.min(current + this.manaDelta, 10));
        }
        return boardState;
    }
}
