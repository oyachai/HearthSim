package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Whirlwind extends SpellCard {

	private static final byte DAMAGE_AMOUNT = 1;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Whirlwind(boolean hasBeenUsed) {
		super("Whirlwind", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Whirlwind() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Whirlwind(this.hasBeenUsed);
	}

	/**
	 * 
	 * Hellfire
	 * 
	 * Deals 3 damage to all characters
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
		if (PlayerSide.CURRENT_PLAYER == side || !(targetMinion instanceof Hero))
			return null;
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		
		if (toRet != null) {
			for (Minion minion : PlayerSide.WAITING_PLAYER.getMinions()) {
				toRet = minion.takeDamage(DAMAGE_AMOUNT, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1, true, false);
			}
	
			for (Minion minion : PlayerSide.CURRENT_PLAYER.getMinions()) {
				toRet = minion.takeDamage(DAMAGE_AMOUNT, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1, true, false);
			}
		}

		return toRet;
	}
}
