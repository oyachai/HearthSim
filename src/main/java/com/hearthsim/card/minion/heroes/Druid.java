package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Druid extends Hero {

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return playerSide == PlayerSide.CURRENT_PLAYER && minion instanceof Hero;
    }

    /**
     * Use the hero ability on a given target
     *
     * Druid: +1 attack this turn and +1 armor
     *
     * @param targetPlayerSide
     * @param targetMinion
     *            The target minion
     * @param boardState
     *
     * @return
     */
    @Override
    public HearthTreeNode useHeroAbility_core(PlayerSide targetPlayerSide, Minion targetMinion,
            HearthTreeNode boardState) {
        if (targetMinion.isHero() && targetPlayerSide == PlayerSide.CURRENT_PLAYER) {
            this.hasBeenUsed = true;
            PlayerModel currentPlayer = boardState.data_.getCurrentPlayer();
            currentPlayer.subtractMana(HERO_ABILITY_COST);

            Hero target = currentPlayer.getHero();
            target.addExtraAttackUntilTurnEnd((byte)1);
            target.addArmor((byte)1);
            return boardState;
        } else {
            return null;
        }
    }
}
