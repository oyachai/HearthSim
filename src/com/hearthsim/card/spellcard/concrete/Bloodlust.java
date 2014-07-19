package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Bloodlust extends SpellCard {

	public Bloodlust() {
		this(false);
	}

	public Bloodlust(boolean hasBeenUsed) {
		super("Bloodlust", (byte)5, hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new Bloodlust(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Give your minions +3 attack for this turn
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
		
		for (Minion minion : boardState.data_.getMinions_p0()) {
			minion.setExtraAttackUntilTurnEnd((byte)3);
		}

		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
