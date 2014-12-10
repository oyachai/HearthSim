package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Swipe extends SpellDamage {

	private static final byte DAMAGE_AMOUNT = 4;

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Swipe(boolean hasBeenUsed) {
		super((byte)4, DAMAGE_AMOUNT, hasBeenUsed);

		this.canTargetOwnHero = false;
		this.canTargetOwnMinions = false;
	}

	/**
	 * Constructor
	 * Defaults to hasBeenUsed = false
	 */
	public Swipe() {
		this(false);
	}

	/**
	 * Use the card on the given target
	 * Deals 4 damage to an enemy and 1 damage to all other enemies
	 * 
	 * @param side
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException {

		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1,
				singleRealizationOnly);

		if(isNotHero(targetMinion)) {
			toRet = toRet.data_.getWaitingPlayerHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, side, boardState,
					deckPlayer0, deckPlayer1, true, false);
		}

		for(Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if(minion != targetMinion) {
				toRet = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, side, toRet, deckPlayer0, deckPlayer1,
						true, false);
			}
		}

		return toRet;
	}
}
