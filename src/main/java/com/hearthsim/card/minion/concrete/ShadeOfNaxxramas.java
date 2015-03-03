package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShadeOfNaxxramas extends Minion {

    public ShadeOfNaxxramas() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            this.addAttack((byte)1);
            this.addHealth((byte)1);
            this.addMaxHealth((byte)1);
        }
        return super.startTurn(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
    }
}
