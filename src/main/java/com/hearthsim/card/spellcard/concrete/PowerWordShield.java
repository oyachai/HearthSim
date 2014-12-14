package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class PowerWordShield extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public PowerWordShield(boolean hasBeenUsed) {
		super((byte)1, hasBeenUsed);

		this.canTargetEnemyHero = false;
		this.canTargetOwnHero = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public PowerWordShield() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gives a minion +2 health and draw a card
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
		if (toRet != null) {
			targetMinion.setHealth((byte)(targetMinion.getHealth() + 2));
			targetMinion.setMaxHealth((byte)(targetMinion.getMaxHealth() + 2));
		}
		if (toRet instanceof CardDrawNode) {
			((CardDrawNode) toRet).addNumCardsToDraw(1);
		} else {
			toRet = new CardDrawNode(toRet, 1); //draw two cards
		}
		return toRet;
	}
}
