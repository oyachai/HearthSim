package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class WaterElemental extends Minion {

    public WaterElemental() {
        super();
    }

    /**
     *
     * Attack with the minion
     *
     * Any minion attacked by this minion is frozen.
     *
     *
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode attack_core(
            PlayerSide targetMinionPlayerSide,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealization)
        throws HSException {
        HearthTreeNode toRet = super.attack_core(targetMinionPlayerSide, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealization);
        if (!silenced_ && toRet != null) {
            targetMinion.setFrozen(true);
        }
        return toRet;
    }
}
