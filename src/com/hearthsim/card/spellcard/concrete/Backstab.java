package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Backstab extends SpellDamage {
	
	public Backstab() {
		this(false);
	}

	public Backstab(boolean hasBeenUsed) {
		super("Arcane Shot", (byte)0, (byte)2, hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new Backstab(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Using this card damages a minion by 2 if the minion is not already damaged.
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		if (targetMinion instanceof Hero)
			return null;
		if (targetMinion.getHealth() == targetMinion.getMaxHealth()) {
			return super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		} else {
			return null;
		}
	}
	
}
