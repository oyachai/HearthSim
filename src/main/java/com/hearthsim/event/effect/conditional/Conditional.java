package com.hearthsim.event.effect.conditional;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface Conditional {

    default boolean isSatisfied(PlayerSide side, HearthTreeNode boardState) {
        return isSatisfied(side, boardState.data_);
    }

    boolean isSatisfied(PlayerSide side, BoardModel boardModel);

    Conditional HOLDING_DRAGON = (side, boardModel) -> {
        for (Card card : boardModel.modelForSide(side).getHand()) {
            if (card instanceof Minion && ((Minion)card).getTribe() == Minion.MinionTribe.DRAGON) {
                return true;
            }
        }
        return false;
    };
}
