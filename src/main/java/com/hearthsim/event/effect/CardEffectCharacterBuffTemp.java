package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectCharacterBuffTemp extends CardEffectCharacter {
    private byte attackDelta;

    public CardEffectCharacterBuffTemp(int attackDelta) {
        this.attackDelta = (byte) attackDelta;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetCharacter.addExtraAttackUntilTurnEnd(this.attackDelta);
        return boardState;
    }
}