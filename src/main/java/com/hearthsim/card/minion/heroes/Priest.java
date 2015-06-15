package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Priest extends Hero {

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return super.canBeUsedOn(playerSide, minion, boardModel) && minion.getTotalHealth() < minion.getTotalMaxHealth();
    }

    /**
     * Use the hero ability on a given target
     *
     * Priest: Heals a target for 2
     *
     *
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
        this.hasBeenUsed = true;
        toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
        toRet = targetMinion.takeHealAndNotify((byte) 2, targetPlayerSide, toRet);

        return toRet;
    }

}
