package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.classic.minion.common.Imp;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ImpMaster extends Minion {

    public ImpMaster() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel);
        if (isWaitingPlayer(thisMinionPlayerIndex))
            return tmpState;

        tmpState = this.takeDamageAndNotify((byte) 1, thisMinionPlayerIndex, thisMinionPlayerIndex, tmpState, false, false);
        if (!tmpState.data_.getCurrentPlayer().isBoardFull()) {
            tmpState = new Imp().summonMinion(thisMinionPlayerIndex, this, tmpState, false);
        }

        return tmpState;
    }
}
