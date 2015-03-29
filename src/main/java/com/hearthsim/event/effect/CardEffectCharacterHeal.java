package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectCharacterHeal extends CardEffectCharacter {
    private byte amount;

    public CardEffectCharacterHeal(int amount) {
        this.amount = (byte) amount;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return targetMinion.takeHealAndNotify(this.amount, targetSide, boardState);
    }
}
