package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class DrainLife extends SpellDamage {
	
	public DrainLife() {
		this(false);
	}

	public DrainLife(boolean hasBeenUsed) {
		super("Drain Life", (byte)3, (byte)2, hasBeenUsed);
	}

	@Override
	public Object deepCopy() {
		return new DrainLife(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 2 damage and heals the hero for 2.
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
		if (playerIndex == 0 && minionIndex == 0) 
			return null;
		
		HearthTreeNode toRet = super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			toRet.data_.getHero_p0().takeHeal((byte)2, 0, 0, boardState, deckPlayer0, deckPlayer1);
		}
		return toRet;
	}
}
