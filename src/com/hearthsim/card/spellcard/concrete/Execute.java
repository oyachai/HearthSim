package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class Execute extends SpellCard {
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Execute(boolean hasBeenUsed) {
		super("Execute", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Execute() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Execute(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Destroy a damaged minion.
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
		if (minionIndex == 0 || playerIndex == 0) {
			//cant't use it on the heroes or friendly minion
			return null;
		}
		
		Minion targetMinion = boardState.data_.getMinion_p1(minionIndex-1);
		if (targetMinion.getHealth() == targetMinion.getMaxHealth())
			return null;
		
		targetMinion.destroyed(playerIndex, minionIndex, boardState, deck);
		boardState.data_.removeMinion_p1(minionIndex-1);
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
