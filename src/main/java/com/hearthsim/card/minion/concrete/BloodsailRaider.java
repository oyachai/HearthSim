package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodsailRaider extends Minion implements MinionUntargetableBattlecry {

    public BloodsailRaider() {
        super();
    }

    /**
     * Battlecry: Gain Attack equal to the Attack of your weapon
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        Minion minionPlacementTarget,
        HearthTreeNode boardState,
        boolean singleRealizationOnly
    ) throws HSException {
        PlayerModel currentPlayer = boardState.data_.getCurrentPlayer();
        if (currentPlayer.getHero().getWeapon() != null) {
            this.addAttack(currentPlayer.getHero().getWeapon().getWeaponDamage());
        }
        return boardState;
    }
}
