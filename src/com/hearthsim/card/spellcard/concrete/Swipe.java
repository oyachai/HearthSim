package com.hearthsim.card.spellcard.concrete;

import java.util.Iterator;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Swipe extends SpellCard {

	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Swipe(boolean hasBeenUsed) {
		super("Swipe", (byte)4, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Swipe() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new Swipe(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 4 damage to an enemy and 1 damage to all other enemies
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
		
		HearthTreeNode toRet = boardState;
		if (minionIndex == 0)
			toRet = toRet.data_.getHero_p1().takeDamage((byte)4, 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1, true);
		else
			toRet = toRet.data_.getHero_p1().takeDamage((byte)1, 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1, true);
		
		for (int indx = 0; indx < toRet.data_.getNumMinions_p1(); ++indx) {
			if (indx + 1 == minionIndex) {
				toRet = toRet.data_.getMinion_p1(indx).takeDamage((byte)4, 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1, true);
			} else {
				toRet = toRet.data_.getMinion_p1(indx).takeDamage((byte)1, 0, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1, true);				
			}
		}
		
		Iterator<Minion> iter = toRet.data_.getMinions_p1().iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0) {
				iter.remove();
			}
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
	}
}
