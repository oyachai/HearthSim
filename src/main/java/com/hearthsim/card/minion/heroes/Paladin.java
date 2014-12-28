package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Paladin extends Hero {

	@Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		return playerSide == PlayerSide.CURRENT_PLAYER && minion instanceof Hero;
    }
	
	/**
	 * Use the hero ability on a given target
	 * 
	 * Paladin: summon a 1/1 Silver Hand Recruit
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
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (currentPlayerBoardFull(boardState))
			return null;

		HearthTreeNode toRet = boardState;

		if (isHero(targetMinion) && targetPlayerSide == PlayerSide.CURRENT_PLAYER) {
			this.hasBeenUsed = true;
			toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
			Minion theRecruit = new SilverHandRecruit();
			Minion targetLocation = toRet.data_.getCharacter(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions());
			toRet = theRecruit.summonMinion(targetPlayerSide, targetLocation, toRet, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
		} else {
			return null;
		}
	
		return toRet;
	}
}
