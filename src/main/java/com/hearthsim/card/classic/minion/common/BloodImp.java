package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.CharacterIndex;
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
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            PlayerModel player = boardModel.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            if (player.getNumMinions() > 1) {
                Minion buffTargetMinion = this;
                while (buffTargetMinion == this) {
                    buffTargetMinion = player.getCharacter(CharacterIndex.fromInteger((int) (Math.random() * player.getNumMinions()) + 1));
                }
                buffTargetMinion.addMaxHealth((byte)1);
                buffTargetMinion.addHealth((byte)1);
            }
        }
        return super.endTurn(thisMinionPlayerIndex, boardModel);
    }
}
