package com.hearthsim.event.attack;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Actions (events) triggered by attacking
 *
 */
@Deprecated
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
     */
    protected HearthTreeNode performAction(
        int attackingPlayerIndex,
        Minion attackingMinion,
        int attackedPlayerIndex,
        Minion attackedMinion,
        HearthTreeNode boardState) {
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
        Deck deckPlayer1) {
        return this.performAction(attackingPlayerIndex, attackingMinion, attackedPlayerIndex, attackedMinion, boardState);
    }
}
