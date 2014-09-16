package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hellfire extends SpellCard {

	private static final byte DAMAGE_AMOUNT = 3;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Hellfire(boolean hasBeenUsed) {
		super("Hellfire", (byte)4, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Hellfire() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Hellfire(this.hasBeenUsed);
	}

	/**
	 * 
	 * Hellfire
	 * 
	 * Deals 3 damage to all characters
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
		if (PlayerSide.WAITING_PLAYER == side || !(targetMinion instanceof Hero))
			return null;
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			toRet = toRet.data_.getCurrentPlayerHero().takeDamage(DAMAGE_AMOUNT, toRet.data_.getCurrentPlayer(), toRet.data_.getCurrentPlayer(), toRet, deckPlayer0, deckPlayer1, true, false);
			for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
				toRet = minion.takeDamage(DAMAGE_AMOUNT, toRet.data_.getCurrentPlayer(), toRet.data_.getCurrentPlayer(), toRet, deckPlayer0, deckPlayer1, true, false);
			}
	
			toRet = toRet.data_.getWaitingPlayerHero().takeDamage(DAMAGE_AMOUNT, toRet.data_.getCurrentPlayer(), toRet.data_.getWaitingPlayer(), toRet, deckPlayer0, deckPlayer1, true, false);
			for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
				toRet = minion.takeDamage(DAMAGE_AMOUNT, toRet.data_.getCurrentPlayer(), toRet.data_.getWaitingPlayer(), toRet, deckPlayer0, deckPlayer1, true, false);
			}
		}		
		return toRet;
	}
}
