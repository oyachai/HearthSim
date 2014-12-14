package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Corruption extends SpellCard {
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Corruption(boolean hasBeenUsed) {
		super((byte)1, hasBeenUsed);

		this.canTargetEnemyHero = false;
		this.canTargetOwnHero = false;
		this.canTargetOwnMinions = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Corruption() {
		this(false);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Choose an enemy minion.  At the start of your turn, destroy it.
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
		if(toRet != null)
			targetMinion.setDestroyOnTurnStart(true);
		return toRet;
	}
}
