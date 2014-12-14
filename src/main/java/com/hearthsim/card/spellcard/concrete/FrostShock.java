package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostShock extends SpellDamage {
	
	public FrostShock() {
		this(false);
	}

	public FrostShock(boolean hasBeenUsed) {
		super((byte)1, (byte)1, hasBeenUsed);
		
		this.canTargetOwnHero = false;
		this.canTargetOwnMinions = false;
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 1 damage and freezes an enemy
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
			targetMinion.setFrozen(true);
		}

		return toRet;
	}
}
