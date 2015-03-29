package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.util.tree.HearthTreeNode;

public class CaptainGreenskin extends Minion implements MinionUntargetableBattlecry {

    public CaptainGreenskin() {
        super();
    }

    /**
     * Battlecry: Add +1/+1 to your weapon.
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        int minionPlacementIndex,
        HearthTreeNode boardState,
        boolean singleRealizationOnly
    ) {
        WeaponCard weapon = boardState.data_.getCurrentPlayer().getHero().getWeapon();
        if (weapon != null) {
            weapon.setWeaponDamage((byte)(weapon.getWeaponDamage() + 1));
            weapon.setWeaponCharge((byte)(weapon.getWeaponCharge() + 1));
        }
        return boardState;
    }
}
