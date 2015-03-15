package com.hearthsim.card.minion;

import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionDamagedInterface {
    /**
     * Called whenever another minion is damaged
     *
     * @param thisMinionPlayerSide
     * @param damagedPlayerSide
     * @param damagedMinion The damaged minion
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     */
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide,
            Minion damagedMinion, HearthTreeNode boardState);
}
