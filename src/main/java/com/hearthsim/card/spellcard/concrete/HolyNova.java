package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class HolyNova extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public HolyNova(boolean hasBeenUsed) {
		super("Holy Nova", (byte)5, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public HolyNova() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new HolyNova(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deal 2 damage to all enemy characters and heal all friendly characters by 2
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
		
		if (!(targetMinion instanceof Hero)) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			toRet = toRet.data_.getCurrentPlayerHero().takeHeal((byte)2, toRet.data_.getCurrentPlayer(), toRet, deckPlayer0, deckPlayer1);
			for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
				toRet = minion.takeHeal((byte)2, toRet.data_.getCurrentPlayer(), toRet, deckPlayer0, deckPlayer1);
			}
			
			toRet = toRet.data_.getWaitingPlayerHero().takeDamage((byte)2, toRet.data_.getCurrentPlayer(), toRet.data_.getWaitingPlayer(), toRet, deckPlayer0, deckPlayer1, true, false);
			for (Minion minion : toRet.data_.getWaitingPlayer().getMinions()) {
				toRet = minion.takeDamage((byte)2, toRet.data_.getCurrentPlayer(), toRet.data_.getWaitingPlayer(), toRet, deckPlayer0, deckPlayer1, true, false);
			}
		}
		return toRet;
	}
}
