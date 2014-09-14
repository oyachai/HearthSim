package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
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
		if (toRet != null) {
			toRet = toRet.data_.getWaitingPlayerHero().takeDamage(DAMAGE_AMOUNT, toRet.data_.getCurrentPlayer(), toRet.data_.getCurrentPlayer(), toRet, deckPlayer0, deckPlayer1, true, false);
			for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
				toRet = minion.takeDamage(DAMAGE_AMOUNT, toRet.data_.getCurrentPlayer(), toRet.data_.getWaitingPlayer(), toRet, deckPlayer0, deckPlayer1, true, false);
			}
		}
		return toRet;
	}
}
