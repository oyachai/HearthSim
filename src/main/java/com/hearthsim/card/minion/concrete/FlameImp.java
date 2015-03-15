package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FlameImp extends Minion implements MinionUntargetableBattlecry {

    public FlameImp() {
        super();
    }

    /**
     * Battlecry: Deal 3 damage to your hero
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        toRet = currentPlayer.getHero().takeDamage((byte)3, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, toRet, false, false);
        return toRet;
    }

}
