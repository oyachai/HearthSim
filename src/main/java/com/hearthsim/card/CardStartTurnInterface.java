package com.hearthsim.card;

import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface CardStartTurnInterface {
    /**
     * Called at the start of the turn
     * This function is called at the start of the turn. Any derived class must override it to implement whatever "start of the turn" effect the card has.
     */
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException;
}
