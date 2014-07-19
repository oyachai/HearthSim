package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class MindVision extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public MindVision(boolean hasBeenUsed) {
		super("Mind Vision", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public MindVision() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new MindVision(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This card damages all enemy minions by 1
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
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		if (minionIndex > 0 || playerIndex == 0) {
			return null;
		}
		
		int numCards = boardState.data_.getNumCards_hand_p1();
		
		if (numCards == 0)
			return null;
		
		Card cardToSteal = (Card)boardState.data_.getCard_hand_p1((int)(numCards * Math.random())).deepCopy();
		boardState.data_.placeCard_hand_p0(cardToSteal);
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
