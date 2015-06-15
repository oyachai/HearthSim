package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.basic.weapon.WickedKnife;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Rogue extends Hero {

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return playerSide == PlayerSide.CURRENT_PLAYER && minion instanceof Hero;
    }

    /**
     * Use the hero ability on a given target
     * <p>
     * Rogue: equip a 1 attack, 2 charge weapon
     *
     * @param targetPlayerSide
     * @param boardState
     * @return
     */
    @Override
    public HearthTreeNode useHeroAbility_core(
        PlayerSide targetPlayerSide,
        Minion targetMinion,
        HearthTreeNode boardState) {
        HearthTreeNode toRet = boardState;
        if (targetMinion.isHero() && targetPlayerSide == PlayerSide.CURRENT_PLAYER) {
            this.hasBeenUsed = true;
            toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
            Hero target = (Hero) targetMinion;

            DeathrattleAction action = target.setWeapon(new WickedKnife());
            if (action != null) {
                toRet = action.performAction(null, targetPlayerSide, toRet);
            }

            return toRet;
        } else {
            return null;
        }
    }

}
