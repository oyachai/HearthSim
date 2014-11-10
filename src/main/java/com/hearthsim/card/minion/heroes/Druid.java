package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Druid extends Hero {

	/**
	 * Use the hero ability on a given target
	 * 
	 * Druid: +1 attack this turn and +1 armor
	 * 
	 *
     *
     * @param targetPlayerSide
     * @param targetMinion The target minion
     * @param boardState
     * @param deckPlayer0
     * @param deckPlayer1
     *
     * @return
	 */
	@Override
	public HearthTreeNode useHeroAbility_core(
			PlayerSide targetPlayerSide,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (isHero(targetMinion) && targetPlayerSide == PlayerSide.CURRENT_PLAYER) {
			this.hasBeenUsed = true;
			boardState.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
			Hero target = boardState.data_.getCurrentPlayerHero();
			target.setExtraAttackUntilTurnEnd((byte)1);
			target.setArmor((byte)1);
			return boardState;
		} else {
			return null;
		}
	}
}
