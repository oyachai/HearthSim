package com.hearthsim.card.basic.minion;

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
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode attack_core(
            PlayerSide targetMinionPlayerSide,
            Minion targetMinion,
            HearthTreeNode boardState)
        throws HSException {
        HearthTreeNode toRet = super.attack_core(targetMinionPlayerSide, targetMinion, boardState);
        if (!silenced_ && toRet != null) {
            targetMinion.setFrozen(true);
        }
        return toRet;
    }
}
