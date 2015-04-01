package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.card.weapon.concrete.BattleAxe;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ArathiWeaponsmith extends Minion implements MinionUntargetableBattlecry {

    public ArathiWeaponsmith() {
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
        HearthTreeNode toRet = boardState;
        Hero theHero = toRet.data_.getCurrentPlayer().getHero();

        WeaponCard newWeapon = new BattleAxe();
        newWeapon.hasBeenUsed(true);

        DeathrattleAction action = theHero.setWeapon(newWeapon);
        if (action != null) {
            toRet = action.performAction(null, PlayerSide.CURRENT_PLAYER, toRet, singleRealizationOnly);
        }

        return toRet;
    }

}
