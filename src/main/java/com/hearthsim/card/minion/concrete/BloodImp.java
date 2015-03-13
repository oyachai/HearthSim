package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodImp extends Minion {

    public BloodImp() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            PlayerModel player = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            if (player.getNumMinions() > 1) {
                Minion buffTargetMinion = this;
                while (buffTargetMinion == this) {
                    buffTargetMinion = player.getMinions().get((int)(Math.random() * player.getNumMinions()));
                }
                buffTargetMinion.addMaxHealth((byte)1);
                buffTargetMinion.addHealth((byte)1);
            }
        }
        return super.endTurn(thisMinionPlayerIndex, toRet);
    }
}
