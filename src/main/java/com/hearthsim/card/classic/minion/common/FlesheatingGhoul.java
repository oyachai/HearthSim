package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FlesheatingGhoul extends Minion implements MinionDeadInterface {

    public FlesheatingGhoul() {
        super();
    }

    /**
     *
     * Whenever a minion dies, gain +1 Attack
     *
     *
     * @param thisMinionPlayerSide
     * @param deadMinionPlayerSide
     * @param deadMinion The dead minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     * */
    @Override
    public HearthTreeNode minionDeadEvent(PlayerSide thisMinionPlayerSide, PlayerSide deadMinionPlayerSide, Minion deadMinion, HearthTreeNode boardState) {
        if (this.setInHand()) {
            return boardState;
        }

        this.attack_ += 1;
        return boardState;
    }
}
