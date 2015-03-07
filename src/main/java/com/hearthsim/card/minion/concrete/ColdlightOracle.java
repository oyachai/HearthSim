package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdlightOracle extends Minion implements MinionUntargetableBattlecry {

    public ColdlightOracle() {
        super();
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        if (toRet instanceof CardDrawNode)
            ((CardDrawNode) toRet).addNumCardsToDraw(2);
        else
            toRet = new CardDrawNode(toRet, 2); //draw two cards

        toRet.data_.drawCardFromWaitingPlayerDeck(2);
        return toRet;
    }

}
