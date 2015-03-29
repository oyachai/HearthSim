package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SunfuryProtector extends Minion implements MinionUntargetableBattlecry {

    public SunfuryProtector() {
        super();
    }

    /**
     * Battlecry: Give adjacent minions Taunt
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        int thisMinionIndex = currentPlayer.getMinions().indexOf(this);
        for (Minion minion : currentPlayer.getMinionsAdjacentToCharacter(thisMinionIndex + 1)) {
            minion.setTaunt(true);
        }
        return boardState;
    }
}
