package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectCharacterHeal<T extends Card> implements CardEffectCharacter<T> {
    private final byte amount;

    public CardEffectCharacterHeal(int amount) {
        this.amount = (byte) amount;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return targetMinion.takeHealAndNotify(this.amount, targetSide, boardState);
    }
}
