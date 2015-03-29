package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AcidicSwampOoze extends Minion implements MinionUntargetableBattlecry {

    public AcidicSwampOoze() {
        super();
    }

    /**
     * Battlecry: Destroy your opponent's weapon
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        int minionPlacementIndex,
        HearthTreeNode boardState,
        boolean singleRealizationOnly
    ) {

        DeathrattleAction action = boardState.data_.getWaitingPlayer().getHero().destroyWeapon();
        if (action != null) {
            boardState = action.performAction(null, PlayerSide.WAITING_PLAYER, boardState, singleRealizationOnly);
        }
        return boardState;
    }
}
