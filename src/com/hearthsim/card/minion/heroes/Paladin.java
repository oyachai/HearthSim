package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.HearthTreeNode;

public class Paladin extends Hero {

	public Paladin() {
		this("", (byte)30);
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
	 * @param thisPlayerIndex The player index of the hero
	 * @param targetPlayerIndex The player index of the target character
	 * @param targetMinionIndex The minion index of the target character
	 * @param boardState
	 * @param deck
	 * @return
	 */
	@Override
	public HearthTreeNode useHeroAbility(
			int thisPlayerIndex,
			int targetPlayerIndex,
			int targetMinionIndex,
			HearthTreeNode boardState,
			Deck deck)
		throws HSException
	{
		if (boardState.data_.getNumMinions_p0() >= 7)
			return null;

		HearthTreeNode toRet = super.useHeroAbility(thisPlayerIndex, targetPlayerIndex, targetMinionIndex, boardState, deck);

		if (toRet != null) {
			if (targetMinionIndex == 0 && targetPlayerIndex == 0) {
				Minion theRecruit = new SilverHandRecruit();
				boardState.data_.placeCard_hand(thisPlayerIndex, theRecruit);
				//HORRID HACK
				boardState.data_.setMana_p0(boardState.data_.getMana_p0() + 1);
				theRecruit.useOn(boardState.data_.getNumCards_hand_p0() - 1, 0, boardState.data_.getNumMinions_p0() + 1, boardState, deck);
			} else {
				return null;
			}
		}
		return toRet;
	}
}
