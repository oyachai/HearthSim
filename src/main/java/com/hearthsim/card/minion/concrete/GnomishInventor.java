package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class GnomishInventor extends Minion implements MinionUntargetableBattlecry {

    public GnomishInventor() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        if (toRet instanceof CardDrawNode)
            ((CardDrawNode) toRet).addNumCardsToDraw(1);
        else
            toRet = new CardDrawNode(toRet, 1); //draw one card
        return toRet;
    }

}
