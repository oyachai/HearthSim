package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CircleOfHealing extends SpellCard {

	private static final byte HEAL_AMOUNT = 4;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public CircleOfHealing(boolean hasBeenUsed) {
		super((byte)0, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public CircleOfHealing() {
		this(false);
	}
	
	
	public Object deepCopy() {
		return new CircleOfHealing(this.hasBeenUsed);
	}

	
	/**
	 * 
	 * Circle of Healing
	 * 
	 * Heals all minions for 4
	 * 
	 *
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
		if (isWaitingPlayer(side) || isNotHero(targetMinion))
			return null;
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		for (BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			toRet = minion.takeHeal(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
		}

		for (BaseEntity minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			toRet = minion.takeHeal(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1);
		}

		return toRet;
	}
}
