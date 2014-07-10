package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class Corruption extends SpellCard {
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Corruption(boolean hasBeenUsed) {
		super("Corruption", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Corruption() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Corruption(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Choose an enemy minion.  At the start of your turn, destroy it.
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode<BoardState> use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		if (playerIndex == 0 || minionIndex == 0) {
			return null;
		}
		
		boardState.data_.getMinion_p1(minionIndex - 1).setDestroyOnTurnStart(true);
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
