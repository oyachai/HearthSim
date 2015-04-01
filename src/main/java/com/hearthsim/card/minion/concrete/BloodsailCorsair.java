package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodsailCorsair extends Minion implements MinionUntargetableBattlecry {

    public BloodsailCorsair() {
        super();
    }

    /**
     * Battlecry: Remove 1 Durability from your opponent's weapon.
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        int minionPlacementIndex,
        HearthTreeNode boardState,
        boolean singleRealizationOnly
    ) {
        PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        boolean hasWeapon = waitingPlayer.getHero().getWeapon() != null;
        if (hasWeapon) {
            waitingPlayer.getHero().getWeapon().useWeaponCharge();
            DeathrattleAction action = waitingPlayer.getHero().checkForWeaponDeath();
            if (action != null) {
                boardState = action.performAction(null, PlayerSide.WAITING_PLAYER, boardState, singleRealizationOnly);
            }
        }
        return boardState;
    }
}
