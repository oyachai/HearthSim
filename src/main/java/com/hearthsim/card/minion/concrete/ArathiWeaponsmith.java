package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.card.weapon.concrete.BattleAxe;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
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
        Minion minionPlacementTarget,
        HearthTreeNode boardState,
        Deck deckPlayer0,
        Deck deckPlayer1,
        boolean singleRealizationOnly
    ) throws HSException {
        HearthTreeNode toRet = boardState;
        Hero theHero = toRet.data_.getCurrentPlayerHero();

        WeaponCard newWeapon = new BattleAxe();
        newWeapon.hasBeenUsed(true);

        DeathrattleAction action = theHero.setWeapon(newWeapon);
        if (action != null) {
            toRet = action.performAction(null, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
        }

        return toRet;
    }

}
