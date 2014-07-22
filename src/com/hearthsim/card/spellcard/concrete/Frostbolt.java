package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Frostbolt extends SpellDamage {

	public Frostbolt() {
		this(false);
	}

	public Frostbolt(boolean hasBeenUsed) {
		super("Frostbolt", (byte)2, (byte)3, hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new Frostbolt(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 3 damage and freezes an enemy
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
		if (playerIndex == 0) 
			return null;
		
		HearthTreeNode toRet = boardState;
		if (toRet != null) {
			if (minionIndex == 0)
				toRet.data_.getHero_p1().setFrozen(true);
			else
				toRet.data_.getMinion_p1(minionIndex - 1).setFrozen(true);
		}
		toRet = super.use_core(thisCardIndex, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);

		return toRet;
	}
}
