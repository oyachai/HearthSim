package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class MultiShot extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public MultiShot(boolean hasBeenUsed) {
		super("Multi-Shot", (byte)4, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public MultiShot() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new MultiShot(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
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
		if (minionIndex > 0 || playerIndex == 0) {
			return null;
		}
		
		int numMinions = boardState.data_.getNumMinions_p0();
		if (numMinions < 2)
			return null;
		
		int indx0 = (int)(numMinions * Math.random());
		int indx1 = indx0;
		while (indx1 == indx0) {
			indx1 = (int)(numMinions * Math.random());
		}
		if (indx0 > indx1) {
			int tmp = indx0;
			indx0 = indx1;
			indx1 = tmp;
		}
		
		Minion tgt0 = boardState.data_.getMinion_p1(indx0);
		Minion tgt1 = boardState.data_.getMinion_p1(indx1);
		
		tgt0.takeDamage((byte)3, 0, 1, indx0 + 1, boardState, deckPlayer0, deckPlayer1);
		tgt1.takeDamage((byte)3, 0, 1, indx1 + 1, boardState, deckPlayer0, deckPlayer1);

		if (tgt1.getHealth() <= 0) {
			boardState.data_.removeMinion_p1(indx1);
		}
		
		if (tgt0.getHealth() <= 0) {
			boardState.data_.removeMinion_p1(indx0);
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);
	}

}
