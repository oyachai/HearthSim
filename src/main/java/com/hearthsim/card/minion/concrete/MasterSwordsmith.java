package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MasterSwordsmith extends Minion {

    public MasterSwordsmith() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        PlayerModel currentPlayer = boardModel.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        int numMinions = currentPlayer.getNumMinions();
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER && numMinions > 1) {
            Minion buffTargetMinion = currentPlayer.getMinions().get((int)(Math.random() * numMinions));
            while (buffTargetMinion == this) {
                buffTargetMinion = currentPlayer.getMinions().get((int)(Math.random() * numMinions));
            }
            buffTargetMinion.addAttack((byte)1);
        }
        return super.endTurn(thisMinionPlayerIndex, boardModel);
    }
}
