package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Actions triggered by a deathrattle
 */
public abstract class DeathrattleAction {

    /**
     * Perform the action
     *
     * @param origin      The minion that is performing the action (aka, the dying minion)
     * @param playerSide
     * @param boardState
     * @param deckPlayer0
     * @param deckPlayer1 @return
     * @throws HSException
     */
    public HearthTreeNode performAction(Card origin,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState,
                                        Deck deckPlayer0,
                                        Deck deckPlayer1,
                                        boolean singleRealizationOnly) throws HSException {
        return boardState;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof DeathrattleAction))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
