package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public class WildGrowth extends SpellCard {
	

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public WildGrowth(boolean hasBeenUsed) {
		super("Wild Growth", (byte)2, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public WildGrowth() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new WildGrowth(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gain an empty mana crystal (i.e., it increases maxMana by 1).  If maxMana is already 10, then it places
	 * the ExcessMana card in your hand.
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
		if (targetPlayerIndex == 1 || !(targetMinion instanceof Hero)) {
			return null;
		}

		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			if (toRet.data_.getMaxMana_p0() >= 10) {
				toRet.data_.placeCard_hand_p0(new ExcessMana());
			} else {
				if (toRet.data_.getMaxMana_p0() < 10)
					toRet.data_.addMaxMana_p0(1);			
			}
		}
		return toRet;
	}
	
	

}
