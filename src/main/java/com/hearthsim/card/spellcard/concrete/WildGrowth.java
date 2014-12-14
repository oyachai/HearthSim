package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class WildGrowth extends SpellCard {
	

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public WildGrowth(boolean hasBeenUsed) {
		super((byte)2, hasBeenUsed);

		this.canTargetEnemyHero = false;
		this.canTargetEnemyMinions = false;
		this.canTargetOwnMinions = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public WildGrowth() {
		this(false);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gain an empty mana crystal (i.e., it increases maxMana by 1).  If maxMana is already 10, then it places
	 * the ExcessMana card in your hand.
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
			if (toRet.data_.getCurrentPlayer().getMaxMana() >= 10) {
				toRet.data_.placeCardHandCurrentPlayer(new ExcessMana());
			} else {
				toRet.data_.getCurrentPlayer().addMaxMana((byte)1);
			}
		}
		return toRet;
	}
	
	

}
