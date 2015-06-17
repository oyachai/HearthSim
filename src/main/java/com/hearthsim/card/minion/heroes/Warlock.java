package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class Warlock extends Hero {

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return playerSide == PlayerSide.CURRENT_PLAYER && minion instanceof Hero;
    }

    /**
     * Use the hero ability on a given target
     *
     * Warlock: draw a card and take 2 damage
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
        if (targetPlayerSide == PlayerSide.WAITING_PLAYER || !targetMinion.isHero())
            return null;
        HearthTreeNode toRet = targetMinion.takeDamageAndNotify((byte) 2, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, false, false);
        if (toRet != null) {
            this.hasBeenUsed = true;
            toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
            if (toRet instanceof CardDrawNode) {
                ((CardDrawNode)toRet).addNumCardsToDraw(1);
            } else {
                toRet = new CardDrawNode(toRet, 1);
            }
        }
        return toRet;
    }
}
