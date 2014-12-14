package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MindControl extends SpellCard {
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public MindControl(boolean hasBeenUsed) {
		super((byte)10, hasBeenUsed);

		this.canTargetEnemyHero = false;
		this.canTargetOwnHero = false;
		this.canTargetOwnMinions = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public MindControl() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Take control of 1 enemy minion
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
			toRet.data_.removeMinion(targetMinion);
			toRet.data_.placeMinion(PlayerSide.CURRENT_PLAYER, targetMinion);
		}
		return toRet;
	}
}
