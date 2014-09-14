package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class CircleOfHealing extends SpellCard {

	private static final byte HEAL_AMOUNT = 4;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public CircleOfHealing(boolean hasBeenUsed) {
		super("Circle of Healing", (byte)0, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public CircleOfHealing() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new CircleOfHealing(this.hasBeenUsed_);
	}

	
	/**
	 * 
	 * Circle of Healing
	 * 
	 * Heals all minions for 4
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
		if (boardState.data_.getWaitingPlayer() == playerModel || !(targetMinion instanceof Hero))
			return null;
		
		HearthTreeNode toRet = super.use_core(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
			toRet = minion.takeHeal(HEAL_AMOUNT, toRet.data_.getCurrentPlayer(), toRet, deckPlayer0, deckPlayer1);
		}

		for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
			toRet = minion.takeHeal(HEAL_AMOUNT, toRet.data_.getWaitingPlayer(), toRet, deckPlayer0, deckPlayer1);
		}

		return toRet;
	}
}
