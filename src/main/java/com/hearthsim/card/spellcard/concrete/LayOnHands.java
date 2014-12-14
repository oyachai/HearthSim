package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class LayOnHands extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public LayOnHands(boolean hasBeenUsed) {
		super((byte)8, hasBeenUsed);
		
		//Let's assume that it is never beneficial to heal an opponent... though this may not strictly be true under some very corner cases (e.g., with a Northshire Cleric)
		this.canTargetEnemyHero = false; // TODO card as printed allows this
		this.canTargetEnemyMinions = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public LayOnHands() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Restore 8 Health and draw 3 cards
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		toRet = targetMinion.takeHeal((byte)8, side, boardState, deckPlayer0, deckPlayer1);
		if (toRet instanceof CardDrawNode)
			((CardDrawNode) toRet).addNumCardsToDraw(3);
		else
			toRet = new CardDrawNode(toRet, 3); //draw three cards

		return toRet;
	}
}
