package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.HealingTotem;
import com.hearthsim.card.minion.concrete.SearingTotem;
import com.hearthsim.card.minion.concrete.StoneclawTotem;
import com.hearthsim.card.minion.concrete.WrathOfAirTotem;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class Shaman extends Hero {

	public Shaman() {
		this("Shaman", (byte)30);
	}

	public Shaman(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Shaman(
			String name,
			byte attack,
			byte extraAttackUntilTurnEnd,
			byte health,
			byte armor,
			byte weaponCharge,
			boolean windFury,
			boolean hasAttacked,
			boolean hasWindFuryAttacked,
			boolean frozen,
			boolean hasBeenUsed) {
	
		super(name, attack, extraAttackUntilTurnEnd, health, armor, weaponCharge, windFury, hasAttacked, hasWindFuryAttacked, frozen, hasBeenUsed);
	}
	
	@Override
	public DeepCopyable deepCopy() {
		return new Shaman(
				this.name_, 
				this.attack_,
				this.extraAttackUntilTurnEnd_,
				this.health_,
				this.armor_,
				this.weaponCharge_,
				this.windFury_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.hasBeenUsed_
				);
	}
	
	/**
	 * Use the hero ability on a given target
	 * 
	 * Warlock: place random totem on the board
	 * 
	 *
     * @param targetPlayerModel
     * @param targetMinion The target minion
     * @param boardState
     * @param deckPlayer0
     * @param deckPlayer1
     *
     * @return
	 */
	@Override
	public HearthTreeNode useHeroAbility_core(
			PlayerModel targetPlayerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (targetPlayerModel != boardState.data_.getCurrentPlayer() || !(targetMinion instanceof Hero))
			return null;

		int numMinions = targetPlayerModel.getNumMinions();
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
				for (Minion minion : toRet.data_.getMinions(targetPlayerModel)) {
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
			this.hasBeenUsed_ = true;
			toRet.data_.setMana_p0(toRet.data_.getMana_p0() - HERO_ABILITY_COST);
			Minion summonTarget = toRet.data_.getCharacter(targetPlayerModel, numMinions);
			toRet = minionToSummon.summonMinion(targetPlayerModel, summonTarget, toRet, deckPlayer0, deckPlayer1, false);
			return toRet;
		}
		
		HearthTreeNode toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.HERO_ABILITY, boardState.data_.getCurrentPlayer(), 0, targetPlayerModel, 0));
		if (toRet != null) {
			Minion[] totems = {new SearingTotem(), new StoneclawTotem(), new HealingTotem(), new WrathOfAirTotem()};
			boolean allTotemsNotSummonable = true;
			for (Minion totemToSummon : totems) {
				boolean totemAlreadySummoned = false;
				for (Minion minion : toRet.data_.getMinions(targetPlayerModel)) {
					if (minion.getClass().equals(totemToSummon.getClass())) {
						totemAlreadySummoned = true;
						break;
					}
				}
				if (!totemAlreadySummoned) {
					allTotemsNotSummonable = false;

					HearthTreeNode newState = toRet.addChild(new HearthTreeNode((BoardModel)toRet.data_.deepCopy()));
                    PlayerModel newTargetPlayerModel;
                    if (toRet.data_.getCurrentPlayer().equals(targetPlayerModel)){
                        newTargetPlayerModel = newState.data_.getCurrentPlayer();
                    }else{
                        newTargetPlayerModel = newState.data_.getWaitingPlayer();
                    }

					Minion summonTarget = newState.data_.getCharacter(newTargetPlayerModel, numMinions);
					newState.data_.setMana_p0(newState.data_.getMana_p0() - HERO_ABILITY_COST);
					newState.data_.getCurrentPlayerHero().hasBeenUsed(true);



					newState = totemToSummon.summonMinion(newTargetPlayerModel, summonTarget, newState, deckPlayer0, deckPlayer1, false);
				}
			}
			if (allTotemsNotSummonable) 
				return null;
		}
		return toRet;
	}
}
