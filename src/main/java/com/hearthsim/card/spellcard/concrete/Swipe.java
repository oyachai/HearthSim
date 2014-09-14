package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class Swipe extends SpellCard {

	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Swipe(boolean hasBeenUsed) {
		super("Swipe", (byte)4, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Swipe() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new Swipe(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 4 damage to an enemy and 1 damage to all other enemies
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
		if (boardState.data_.getCurrentPlayer() == playerModel) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		toRet = targetMinion.takeDamage((byte)4, toRet.data_.getCurrentPlayer(), playerModel, toRet, deckPlayer0, deckPlayer1, true, false);
		if (!(targetMinion instanceof Hero))
			toRet = toRet.data_.getWaitingPlayerHero().takeDamage((byte)1, boardState.data_.getCurrentPlayer(), playerModel, boardState, deckPlayer0, deckPlayer1, true, false);
		for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
			if (minion != targetMinion)
				toRet = minion.takeDamage((byte)1, toRet.data_.getCurrentPlayer(), playerModel, toRet, deckPlayer0, deckPlayer1, true, false);
		}
				
		return toRet;
	}
}
