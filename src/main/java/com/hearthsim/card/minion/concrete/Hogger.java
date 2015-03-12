package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hogger extends Minion {

    public Hogger() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode toRet = boardModel;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER && currentPlayer.getNumMinions() < 7) {
            Minion minion = new Gnoll();
            Minion placementTarget = currentPlayer.getMinions().getLast();
            minion.summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, toRet, deckPlayer0, deckPlayer1, false, false);
        }
        return super.endTurn(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
    }

}
