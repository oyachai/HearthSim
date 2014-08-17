package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Assassinate extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Assassinate(boolean hasBeenUsed) {
		super("Assassinate", (byte)5, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Assassinate() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Assassinate(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This card destroys an enemy minion.
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
		throws HSException
	{
		if (targetPlayerIndex == 0 || targetMinion instanceof Hero) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			targetMinion.setHealth((byte)-99);
		}
		return toRet;
	}
}
