package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class EffectHandManaCost implements SimpleEffectHand {
    byte manaEffect;

    public EffectHandManaCost(int manaEffect) {
        this.manaEffect = (byte) manaEffect;
    }

    @Override
    public void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Card targetCard, BoardModel board) {
        targetCard.setManaDelta((byte) (targetCard.getManaDelta() + this.manaEffect));
    }
}
