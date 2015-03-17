package com.hearthsim.card;

import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface CardEndTurnInterface {
    /**
     * End the turn and resets the card state
     * This function is called at the end of the turn. Any derived class must override it and remove any temporary buffs that it has.
     */
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException;
}
