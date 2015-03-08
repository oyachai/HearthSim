package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class YoungPriestess extends Minion {

    public YoungPriestess() {
        super();
    }

    /**
     * At the end of your turn, give another random friendly minion +1 Health
     */
    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode toRet = super.endTurn(thisMinionPlayerIndex, boardModel, deckPlayer0, deckPlayer1);
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        if (toRet != null && thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            int numFriendlyMinions = currentPlayer.getNumMinions();
            if (numFriendlyMinions > 1) {
                int minionToBuffIndex = (int)(Math.random() * numFriendlyMinions);
                while (currentPlayer.getMinions().get(minionToBuffIndex) == this) {
                    minionToBuffIndex = (int)(Math.random() * numFriendlyMinions);
                }
                currentPlayer.getMinions().get(minionToBuffIndex).addHealth((byte)1);
                currentPlayer.getMinions().get(minionToBuffIndex).addMaxHealth((byte)1);
            }
        }
        return toRet;
    }
}
