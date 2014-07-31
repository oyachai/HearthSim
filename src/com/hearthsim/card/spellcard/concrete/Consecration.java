package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
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
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		if (targetPlayerIndex > 0 || !(targetMinion instanceof Hero)) 
			return null;
		
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			toRet = toRet.data_.getHero_p1().takeDamage(DAMAGE_AMOUNT, 0, 0, toRet, deckPlayer0, deckPlayer1, true);
			for (Minion minion : toRet.data_.getMinions_p1()) {
				toRet = minion.takeDamage(DAMAGE_AMOUNT, 0, 1, toRet, deckPlayer0, deckPlayer1, true);
			}
		}
		return toRet;
	}
}
