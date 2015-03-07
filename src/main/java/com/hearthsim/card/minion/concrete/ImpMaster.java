package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ImpMaster extends Minion {

    public ImpMaster() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel, deckPlayer0, deckPlayer1);
        if (isWaitingPlayer(thisMinionPlayerIndex))
            return tmpState;

        tmpState = this.takeDamage((byte)1, thisMinionPlayerIndex, thisMinionPlayerIndex, tmpState, deckPlayer0, deckPlayer1, false, false);
        if (tmpState.data_.getCurrentPlayer().getMinions().size() < 7) {
            tmpState = new Imp().summonMinion(thisMinionPlayerIndex, this, tmpState, deckPlayer0, deckPlayer1, false, true);
        }

        return tmpState;
    }
}
