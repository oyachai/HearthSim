package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Huffer;
import com.hearthsim.card.minion.concrete.Leokk;
import com.hearthsim.card.minion.concrete.Misha;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AnimalCompanion extends SpellCard {

	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public AnimalCompanion(boolean hasBeenUsed) {
		super("Animal Companion", (byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public AnimalCompanion() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new AnimalCompanion(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Summons either Huffer, Leokk, or Misha
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
		if (!(targetMinion instanceof Hero) || PlayerSide.WAITING_PLAYER == side) {
			return null;
		}
		
		int numMinions = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions();
		if (numMinions >= 7)
			return null;
		
		double rnd = Math.random();
		Minion minion = null;
		if (rnd < 0.333333333333333333333333333) {
			minion = new Huffer();
		} else if (rnd > 0.66666666666666666666666666666) {
			minion = new Leokk();
		} else {
			minion = new Misha();
		}
		boardState.data_.setMana_p0(boardState.data_.getMana_p0() + 3);
		boardState.data_.placeCardHandCurrentPlayer(minion);
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) 
			toRet = minion.useOn(side, PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(numMinions - 1), toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
		return toRet;
	}

}
