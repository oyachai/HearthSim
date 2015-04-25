package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@FunctionalInterface
public interface SimpleEffectInterface<T extends Card> extends EffectInterface<T> {
    public void applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetIndex, BoardModel board);

    @Override
    public default HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetIndex, HearthTreeNode boardState) {
        this.applyEffect(originSide, origin, targetSide, targetIndex, boardState.data_);
        return boardState;
    }
}
