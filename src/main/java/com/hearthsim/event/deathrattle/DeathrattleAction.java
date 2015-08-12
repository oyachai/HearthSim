package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Actions triggered by a deathrattle
 */
public abstract class DeathrattleAction<T extends Card> {

    /**
     * Perform the action
     *
     * @param origin      The minion that is performing the action (aka, the dying minion)
     * @param playerSide
     * @param boardState
     */
    public HearthTreeNode performAction(CharacterIndex originIndex,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState) {
        return boardState;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof DeathrattleAction))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
