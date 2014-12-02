package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HolyNova extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public HolyNova(boolean hasBeenUsed) {
		super((byte)5, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public HolyNova() {
		this(false);
	}

	
	public Object deepCopy() {
		return new HolyNova(this.hasBeenUsed);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deal 2 damage to all enemy characters and heal all friendly characters by 2
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
		if (isCurrentPlayer(side)) {
			return null;
		}
		
		if (isNotHero(targetMinion)) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			toRet = toRet.data_.getCurrentPlayerHero().takeHeal((byte)2, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
			for (BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				toRet = minion.takeHeal((byte)2, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
			}
			
			toRet = toRet.data_.getWaitingPlayerHero().takeDamage((byte)2, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1, true, false);
			for (BaseEntity minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				toRet = minion.takeDamage((byte)2, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1, true, false);
			}
		}
		return toRet;
	}
}
