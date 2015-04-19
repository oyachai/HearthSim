package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ManaTideTotem extends Minion {

    public ManaTideTotem() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel);
        if (isWaitingPlayer(thisMinionPlayerIndex))
            return tmpState;

        tmpState.data_.drawCardFromCurrentPlayerDeck(1);
        return tmpState;
    }

}
