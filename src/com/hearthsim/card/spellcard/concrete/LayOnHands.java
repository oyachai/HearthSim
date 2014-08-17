package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class LayOnHands extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public LayOnHands(boolean hasBeenUsed) {
		super("Lay on Hands", (byte)8, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public LayOnHands() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new LayOnHands(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Restore 8 Health and draw 3 cards
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
		//Let's assume that it is never beneficial to heal an opponent... though this may not strictly be true under some very corner cases (e.g., with a Northshire Cleric)
		if (targetPlayerIndex == 1) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		toRet = targetMinion.takeHeal((byte)8, targetPlayerIndex, boardState, deckPlayer0, deckPlayer1);
		if (toRet instanceof CardDrawNode)
			((CardDrawNode) toRet).addNumCardsToDraw(3);
		else
			toRet = new CardDrawNode(toRet, 3); //draw three cards

		return toRet;
	}
}
