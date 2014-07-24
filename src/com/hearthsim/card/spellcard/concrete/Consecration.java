package com.hearthsim.card.spellcard.concrete;

import java.util.Iterator;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Consecration extends SpellCard {

	private static final byte DAMAGE_AMOUNT = 2;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Consecration(boolean hasBeenUsed) {
		super("Consecration", (byte)4, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Consecration() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Consecration(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Consecration
	 * 
	 * Deals 2 damage to all enemy characters
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
		if (playerIndex > 0 || minionIndex > 0) 
			return null;
		
		HearthTreeNode toRet = boardState;
		toRet = toRet.data_.getHero_p1().takeDamage(DAMAGE_AMOUNT, 0, 0, 0, toRet, deckPlayer0, deckPlayer1, true);
		for (int indx = 0; indx < toRet.data_.getNumMinions_p1(); ++indx) {
			Minion targetMinion = toRet.data_.getMinion_p1(indx);
			toRet = targetMinion.takeDamage(DAMAGE_AMOUNT, 0, 1, indx + 1, toRet, deckPlayer0, deckPlayer1, true);
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
