package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class CircleOfHealing extends SpellCard {

	private static final byte HEAL_AMOUNT = 4;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public CircleOfHealing(boolean hasBeenUsed) {
		super("Circle of Healing", (byte)0, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public CircleOfHealing() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new CircleOfHealing(this.hasBeenUsed_);
	}

	
	/**
	 * 
	 * Circle of Healing
	 * 
	 * Heals all minions for 4
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
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		if (playerIndex > 0 || minionIndex > 0) 
			return null;
		
		HearthTreeNode toRet = super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);
		for (int indx = 0; indx < boardState.data_.getNumMinions_p0(); ++indx) {
			Minion targetMinion = boardState.data_.getMinion_p0(indx);
			toRet = targetMinion.takeHeal(HEAL_AMOUNT, 0, indx + 1, toRet, deckPlayer0, deckPlayer1);
		}

		for (int indx = 0; indx < boardState.data_.getNumMinions_p1(); ++indx) {
			Minion targetMinion = boardState.data_.getMinion_p1(indx);
			toRet = targetMinion.takeHeal(HEAL_AMOUNT, 1, indx + 1, toRet, deckPlayer0, deckPlayer1);
		}

		return toRet;
	}
}
