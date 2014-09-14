package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class Shiv extends SpellDamage {


	public Shiv() {
		this(false);
	}

	public Shiv(boolean hasBeenUsed) {
		super("Shiv", (byte)2, (byte)1, hasBeenUsed);
	}

	@Override
	public Object deepCopy() {
		return new Shiv(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 1 damage and draws a card
	 * 
	 *
     * @param playerModel
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerModel playerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{

		if (boardState.data_.getCurrentPlayer() == playerModel && targetMinion instanceof Hero)
			return null;
		
		HearthTreeNode toRet = super.use_core(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet instanceof CardDrawNode) {
			((CardDrawNode) toRet).addNumCardsToDraw(1);
		} else {
			toRet = new CardDrawNode(toRet, 1); //draw two cards
		}
		return toRet;
	}
}
