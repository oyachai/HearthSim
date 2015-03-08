package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Doomsayer extends Minion {

    public Doomsayer() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);

            for (Minion minion : currentPlayer.getMinions()) {
                minion.setHealth((byte)-99);
            }
            for (Minion minion : waitingPlayer.getMinions()) {
                minion.setHealth((byte)-99);
            }
        }
        return super.startTurn(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
    }

}
