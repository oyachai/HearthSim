package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.entity.BaseEntity;
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
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Equality() {
		this(false);
	}
	
	
	public Object deepCopy() {
		return new Equality(this.hasBeenUsed);
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
	
	protected HearthTreeNode use_core(
			PlayerSide side,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (isNotHero(targetMinion) || isWaitingPlayer(side)) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			for (BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				minion.setHealth((byte)1);
			}
			for (BaseEntity minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				minion.setHealth((byte)1);
			}
		}
		return toRet;
	}
}
