package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdlightOracle extends Minion implements MinionUntargetableBattlecry {

    public ColdlightOracle() {
        super();
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        if (toRet instanceof CardDrawNode)
            ((CardDrawNode) toRet).addNumCardsToDraw(2);
        else
            toRet = new CardDrawNode(toRet, 2); //draw two cards

        toRet.data_.drawCardFromWaitingPlayerDeck(2);
        return toRet;
    }

}
