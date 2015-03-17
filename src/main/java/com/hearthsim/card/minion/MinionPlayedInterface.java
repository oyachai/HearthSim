package com.hearthsim.card.minion;

import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionPlayedInterface {
     /**
     * Called whenever a minion is played (i.e., if a minion card was used to directly place a minion on the board)
     *
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     *
     * */
    public HearthTreeNode minionPlayedEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState);
}
