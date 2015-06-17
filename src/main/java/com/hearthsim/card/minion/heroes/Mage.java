package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Mage extends Hero {

    //Assume that it is never a good idea to fireblast yourself
    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return super.canBeUsedOn(playerSide, minion, boardModel) && !(playerSide == PlayerSide.CURRENT_PLAYER && minion instanceof Hero);
    }

    /**
     * Use the hero ability on a given target
     *
     * Mage: deals 1 damage
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
        this.hasBeenUsed = true;
        toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
        toRet = targetMinion.takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, targetPlayerSide, toRet, false, true);

        return toRet;
    }

}
