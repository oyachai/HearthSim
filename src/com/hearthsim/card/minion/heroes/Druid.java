package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.HearthTreeNode;

public class Druid extends Hero {

	public Druid() {
		this("Druid", (byte)30);
	}

	public Druid(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Druid(
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
		return new Druid(
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
	 * Druid: +1 attack this turn and +1 armor
	 * 
	 * @param targetPlayerIndex The player index of the target character
	 * @param targetMinion The target minion
	 * @param boardState
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * 
	 * @return
	 */
	@Override
	public HearthTreeNode useHeroAbility_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		if ((targetMinion instanceof Hero)  && targetPlayerIndex == 0) {
			Hero target = boardState.data_.getHero_p0();
			target.setExtraAttackUntilTurnEnd((byte)1);
			target.setArmor((byte)1);
			return boardState;
		} else {
			return null;
		}
	}

}
