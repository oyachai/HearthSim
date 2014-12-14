package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Equality extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Equality(boolean hasBeenUsed) {
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
	public Equality() {
		this(false);
	}
	
	/**
	 * 
	 * Change the Health of ALL minions to 1
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
			for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				minion.setHealth((byte)1);
			}
			for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				minion.setHealth((byte)1);
			}
		}
		return toRet;
	}
}
