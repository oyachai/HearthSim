package com.hearthsim.event.attack;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Actions (events) triggered by attacking
 *
 */
public abstract class AttackAction {

    /**
     * Perform the action
     *
     * @param attackingPlayerIndex
     * @param attackingMinion
     * @param attackedPlayerIndex
     * @param attackedMinion
     * @param boardState
     * @param deckPlayer0
     * @param deckPlayer1
     * @return
     * @throws HSInvalidPlayerIndexException
     */
    public HearthTreeNode performAction(
            int attackingPlayerIndex,
            Minion attackingMinion,
            int attackedPlayerIndex,
            Minion attackedMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1)
    throws HSInvalidPlayerIndexException {
        return boardState;
    }

}
