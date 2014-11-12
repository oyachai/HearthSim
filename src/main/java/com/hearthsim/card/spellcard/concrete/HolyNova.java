package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HolyNova extends SpellDamageAoe {

	private static final byte DAMAGE_AMOUNT = 2;

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public HolyNova(boolean hasBeenUsed) {
		super((byte)5, DAMAGE_AMOUNT, hasBeenUsed);
		this.targetOpponent = true;
		this.targetOpponentMinions = true;
	}

	/**
	 * Constructor
	 * Defaults to hasBeenUsed = false
	 */
	public HolyNova() {
		this(false);
	}

	/**
	 * Use the card on the given target
	 * Deal 2 damage to all enemy characters and heal all friendly characters by 2
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

		if(toRet != null) {
			toRet = toRet.data_.getCurrentPlayerHero().takeHeal((byte)2, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0,
					deckPlayer1);
			for(Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				toRet = minion.takeHeal((byte)2, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
			}
		}
		return toRet;
	}
}
