package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class ArcaneExplosion extends SpellCard {
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public ArcaneExplosion(boolean hasBeenUsed) {
		super("Arcane Explosion", (byte)2, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public ArcaneExplosion() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new ArcaneExplosion(this.hasBeenUsed_);
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
			Deck deckPlayer0, Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		if (playerIndex == 0) {
			return null;
		}
		
		if (minionIndex > 0) {
			return null;
		}
		
		for (int indx = 0; indx < boardState.data_.getNumMinions_p1(); ++indx) {
			Minion targetMinion = boardState.data_.getMinion_p1(indx);
			targetMinion.takeDamage((byte)1, 0, 1, indx + 1, boardState, deckPlayer0, deckPlayer1, true);
		}

		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);
	}
}
