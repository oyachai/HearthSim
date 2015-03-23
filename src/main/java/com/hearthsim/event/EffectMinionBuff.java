package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectMinionBuff<T extends Card> extends EffectMinionAction<T> {
    private byte attackDelta;
    private byte healthDelta;

    public EffectMinionBuff(int attackDelta, int healthDelta) {
        this.attackDelta = (byte) attackDelta;
        this.healthDelta = (byte) healthDelta;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetCharacter.addAttack(this.attackDelta);
        targetCharacter.addHealth(this.healthDelta);
        targetCharacter.addMaxHealth(this.healthDelta);
        return boardState;
    }
}
