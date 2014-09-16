package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
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
		super("Circle of Healing", (byte)0, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public CircleOfHealing() {
		this(false);
	}
	
	@Override
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
		if (PlayerSide.WAITING_PLAYER == side || !(targetMinion instanceof Hero))
			return null;
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		for (Minion minion : PlayerSide.CURRENT_PLAYER.getMinions()) {
			toRet = minion.takeHeal(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
		}

		for (Minion minion : PlayerSide.WAITING_PLAYER.getMinions()) {
			toRet = minion.takeHeal(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1);
		}

		return toRet;
	}
}
