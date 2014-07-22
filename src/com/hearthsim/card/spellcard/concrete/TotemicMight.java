package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Totem;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class TotemicMight extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public TotemicMight(boolean hasBeenUsed) {
		super("Totemic Might", (byte)0, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public TotemicMight() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new TotemicMight(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gives all friendly totems +2 health
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
		if (minionIndex > 0 || playerIndex == 1) {
			return null;
		}
		
		for (Minion minion : boardState.data_.getMinions_p0()) {
			if (minion instanceof Totem) {
				minion.setHealth((byte)(2 + minion.getHealth()));
				minion.setMaxHealth((byte)(2 + minion.getMaxHealth()));
			}
		}
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);
	}
}
