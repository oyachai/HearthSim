package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class HolyLight extends SpellCard {
	
	private static final byte HEAL_AMOUNT = 6;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public HolyLight(boolean hasBeenUsed) {
		super("Holy Light", (byte)2, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public HolyLight() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new HolyLight(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Heal a character for 6
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
		HearthTreeNode toRet = super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
		if (minionIndex == 0)
			toRet = toRet.data_.getHero(playerIndex).takeHeal(HEAL_AMOUNT, playerIndex, minionIndex, toRet, deck);
		else
			toRet = toRet.data_.getMinion(playerIndex, minionIndex - 1).takeHeal(HEAL_AMOUNT, playerIndex, minionIndex, toRet, deck);
		return toRet;
	}
}
