package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class WildGrowth extends SpellCard {
	

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public WildGrowth(boolean hasBeenUsed) {
		super("Wild Growth", (byte)2, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public WildGrowth() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new WildGrowth(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gain an empty mana crystal (i.e., it increases maxMana by 1).  If maxMana is already 10, then it places
	 * the ExcessMana card in your hand.
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
		if (playerIndex == 1 || minionIndex > 0) {
			return null;
		}
		
		if (boardState.data_.getMaxMana_p0() >= 10) {
			boardState.data_.placeCard_hand_p0(new ExcessMana());
		} else {
			if (boardState.data_.getMaxMana_p0() < 10)
				boardState.data_.addMaxMana_p0(1);			
		}
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
	
	

}
