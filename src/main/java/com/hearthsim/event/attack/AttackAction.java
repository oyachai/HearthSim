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
     * @return
     * @throws HSInvalidPlayerIndexException
     */
    public HearthTreeNode performAction(
            int attackingPlayerIndex,
            Minion attackingMinion,
            int attackedPlayerIndex,
            Minion attackedMinion,
            HearthTreeNode boardState)
    throws HSInvalidPlayerIndexException {
        return boardState;
    }

    @Deprecated
    public HearthTreeNode performAction(
        int attackingPlayerIndex,
        Minion attackingMinion,
        int attackedPlayerIndex,
        Minion attackedMinion,
        HearthTreeNode boardState,
        Deck deckPlayer0,
        Deck deckPlayer1)
        throws HSInvalidPlayerIndexException {
        return this.performAction(attackingPlayerIndex, attackingMinion, attackedPlayerIndex, attackedMinion, boardState);
    }
}
