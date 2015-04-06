package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Onyxia extends Minion implements MinionUntargetableBattlecry  {

    public Onyxia() {
        super();
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = toRet.data_.getCurrentPlayer();
        while (!currentPlayer.isBoardFull()) {
            Minion placementTarget;
            if (currentPlayer.getNumMinions() % 2 == 0) {
                placementTarget = toRet.data_.getCurrentPlayer().getCharacter(currentPlayer.getIndexForCharacter(this) - 1);
            } else {
                placementTarget = this;
            }
            toRet = new Whelp().summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, toRet, false, singleRealizationOnly);
        }
        return toRet;
    }
}
