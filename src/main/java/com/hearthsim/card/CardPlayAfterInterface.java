package com.hearthsim.card;

import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface CardPlayAfterInterface {
    /**
     *
     * Called whenever another card is played, after it is actually played
     *
     * @param thisCardPlayerSide The player index of the card receiving the event
     * @param cardUserPlayerSide
     * @param usedCard The card that was used
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    public HearthTreeNode onCardPlayResolve(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState);
}
