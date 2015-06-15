package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hunter extends Hero {

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return playerSide == PlayerSide.WAITING_PLAYER && minion instanceof Hero;
    }

    /**
     * Use the hero ability on a given target
     *
     * Hunter: Deals 2 damage to enemy hero
     *
     *
     *
     * @param targetPlayerSide
     * @param targetMinion The target minion
     * @param boardState
     *
     * @return
     */
    @Override
    public HearthTreeNode useHeroAbility_core(
            PlayerSide targetPlayerSide,
            Minion targetMinion,
            HearthTreeNode boardState) {
        HearthTreeNode toRet = boardState;
        if (targetMinion.isHero() && targetPlayerSide == PlayerSide.WAITING_PLAYER) {
            this.hasBeenUsed = true;
            toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
            toRet = targetMinion.takeDamageAndNotify((byte) 2, PlayerSide.CURRENT_PLAYER, targetPlayerSide, toRet, false, false);
            return toRet;
        } else {
            return null;
        }
    }

}
