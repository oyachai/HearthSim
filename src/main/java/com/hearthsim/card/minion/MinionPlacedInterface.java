package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionPlacedInterface {
    /**
     * Called whenever another minion comes on board
     *
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     */
    @Deprecated
    public HearthTreeNode minionPlacedEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide,
            Minion summonedMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1);

    public HearthTreeNode minionPlacedEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide,
                                            Minion summonedMinion, HearthTreeNode boardState);
}
