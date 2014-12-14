package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Frog;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hex extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Hex(boolean hasBeenUsed) {
		super((byte)3, hasBeenUsed);
		
		this.canTargetEnemyHero = false;
		this.canTargetOwnHero = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Hex() {
		this(false);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Transform a minion into 0/1 frog with Taunt
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
			Frog frog = new Frog();
			toRet = frog.placeMinion(side, targetMinion, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
			toRet.data_.removeMinion(targetMinion);
		}
		return toRet;
	}

}
