package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.HearthTreeNode;

public class Paladin extends Hero {

	public Paladin() {
		this("Paladin", (byte)30);
	}

	public Paladin(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Paladin(
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
		return new Paladin(
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
	 * Paladin: summon a 1/1 Silver Hand Recruit
	 * 
	 *
     * @param targetPlayerModel
     * @param boardState
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
		if (boardState.data_.getCurrentPlayer().getNumMinions() >= 7)
			return null;

		HearthTreeNode toRet = boardState;

		if (targetMinion instanceof Hero && targetPlayerModel == boardState.data_.getCurrentPlayer()) {
			this.hasBeenUsed_ = true;
			toRet.data_.setMana_p0(toRet.data_.getMana_p0() - HERO_ABILITY_COST);
			Minion theRecruit = new SilverHandRecruit();
			Minion targetLocation = toRet.data_.getCharacter(toRet.data_.getCurrentPlayer(), toRet.data_.getCurrentPlayer().getNumMinions());
			toRet = theRecruit.summonMinion(targetPlayerModel, targetLocation, toRet, deckPlayer0, deckPlayer1, false);
		} else {
			return null;
		}
	
		return toRet;
	}
}
