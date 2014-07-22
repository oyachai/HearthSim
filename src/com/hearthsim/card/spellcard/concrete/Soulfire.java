package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Soulfire extends SpellDamage {

	public Soulfire() {
		this(false);
	}

	public Soulfire(boolean hasBeenUsed) {
		super("Soulfire", (byte)0, (byte)4, hasBeenUsed);
	}

	@Override
	public Object deepCopy() {
		return new 	Soulfire(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 2 damage and heals the hero for 2.
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
		HearthTreeNode toRet = super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);
		
		int numCards = boardState.data_.getNumCards_hand_p0();
		if (numCards <= 0)
			return null;
		int cardToDiscardIndex = (int)(Math.random() * numCards);
		boardState.data_.removeCard_hand(cardToDiscardIndex);
		return boardState;
	}
}
