package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class HealingTouch extends SpellCard {
	
	private static final byte HEAL_AMOUNT = 8;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public HealingTouch(boolean hasBeenUsed) {
		super("Healing Touch", (byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public HealingTouch() {
		this(false);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Heal a character for 8
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
		if (minionIndex == 0)
			boardState.data_.getHero(playerIndex).takeHeal(HEAL_AMOUNT, playerIndex, minionIndex, boardState, deck);
		else
			boardState.data_.getMinion(playerIndex, minionIndex - 1).takeHeal(HEAL_AMOUNT, playerIndex, minionIndex, boardState, deck);
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
