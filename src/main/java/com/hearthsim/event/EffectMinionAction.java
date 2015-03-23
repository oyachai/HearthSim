package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class EffectMinionAction<T extends Card> {
    public abstract HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException;

    public final HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, Minion targetCharacter, HearthTreeNode boardState) throws HSException {
        int index = boardState.data_.modelForSide(targetSide).getIndexForCharacter(targetCharacter);
        return this.applyEffect(originSide, origin, targetSide, index, boardState);
    }
}
