package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.HealingTotem;
import com.hearthsim.card.minion.concrete.SearingTotem;
import com.hearthsim.card.minion.concrete.StoneclawTotem;
import com.hearthsim.card.minion.concrete.WrathOfAirTotem;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class Shaman extends Hero {

	/**
	 * Use the hero ability on a given target
	 * 
	 * Warlock: place random totem on the board
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
		if (targetPlayerSide != PlayerSide.CURRENT_PLAYER || isNotHero(targetMinion))
			return null;

		int numMinions = targetPlayerSide.getPlayer(boardState).getNumMinions();
		if (numMinions >= 7) {
			return null;
		}
		
		if (singleRealizationOnly) {
			HearthTreeNode toRet = boardState;
			Minion minionToSummon = null;
			Minion[] allTotems = {new SearingTotem(), new StoneclawTotem(), new HealingTotem(), new WrathOfAirTotem()};
			for(int i = allTotems.length - 1; i > 0; --i) {
				int j = (int)(Math.random() * (i + 1));
				Minion ci = allTotems[i];
				allTotems[i] = allTotems[j];
				allTotems[j] = ci;
			}
			for (int index = 0; index < 4; ++index) {
				boolean totemAlreadySummoned = false;
				for (Minion minion : toRet.data_.getMinions(targetPlayerSide)) {
					if (minion.getClass().equals(allTotems[index].getClass())) {
						totemAlreadySummoned = true;
					}
				}
				if (!totemAlreadySummoned) {
					minionToSummon = allTotems[index];
					break;
				}
			}
			if (minionToSummon == null)
				return null;
			this.hasBeenUsed = true;
			toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
			Minion summonTarget = toRet.data_.getCharacter(targetPlayerSide, numMinions);
			toRet = minionToSummon.summonMinion(targetPlayerSide, summonTarget, toRet, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
			return toRet;
		}
		
		HearthTreeNode toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.HERO_ABILITY, PlayerSide.CURRENT_PLAYER, 0, targetPlayerSide, 0));
		if (toRet != null) {
			Minion[] totems = {new SearingTotem(), new StoneclawTotem(), new HealingTotem(), new WrathOfAirTotem()};
			boolean allTotemsNotSummonable = true;
			for (Minion totemToSummon : totems) {
				boolean totemAlreadySummoned = false;
				for (Minion minion : toRet.data_.getMinions(targetPlayerSide)) {
					if (minion.getClass().equals(totemToSummon.getClass())) {
						totemAlreadySummoned = true;
						break;
					}
				}
				if (!totemAlreadySummoned) {
					allTotemsNotSummonable = false;

					HearthTreeNode newState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));

					Minion summonTarget = newState.data_.getCharacter(targetPlayerSide, numMinions);
					newState.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
					newState.data_.getCurrentPlayerHero().hasBeenUsed(true);

					newState = totemToSummon.summonMinion(targetPlayerSide, summonTarget, newState, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
				}
			}
			if (allTotemsNotSummonable) 
				return null;
		}
		return toRet;
	}
}
