package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class Warlock extends Hero {
	
	public Warlock() {
		this("Warlock", (byte)30);
	}

	public Warlock(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Warlock(
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
		return new Warlock(
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
	 * Warlock: draw a card and take 2 damage
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
		if (targetPlayerIndex != 0 || !(targetMinion instanceof Hero))
			return null;
		HearthTreeNode toRet = targetMinion.takeDamage((byte)2, 0, 0, boardState, deckPlayer0, deckPlayer1, false, false);
		if (toRet != null) {
			if (toRet instanceof CardDrawNode) {
				((CardDrawNode)toRet).addNumCardsToDraw(1);
			} else {
				toRet = new CardDrawNode(toRet, 1);
			}
		}
		return toRet;
	}
}
