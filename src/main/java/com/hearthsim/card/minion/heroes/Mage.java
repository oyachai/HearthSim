package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Mage extends Hero {

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
		HearthTreeNode toRet = boardState;
		this.hasBeenUsed = true;
		toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
		toRet = targetMinion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, targetPlayerSide, toRet, deckPlayer0, deckPlayer1, false, true);
		
		return toRet;
	}

}
