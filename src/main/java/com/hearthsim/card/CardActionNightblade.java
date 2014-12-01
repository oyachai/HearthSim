package com.hearthsim.card;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardActionNightblade extends CardAction{
	public CardActionNightblade(Card card) {
		super(card);
	}
	
	/**
	 * 
	 * Override for battlecry
	 * 
	 * Battlecry: Heals friendly characters for 2
	 * 
	 *
     *
     *
     * @param side
     * @param targetMinion The target minion (can be a Hero).  If it is a Hero, then the minion is placed on the last (right most) spot on the board.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode useOn(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = super.getCard().use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);

		if (toRet != null) 
			toRet = toRet.data_.getWaitingPlayerHero().takeDamage((byte)3, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
		
		return toRet;
	}
}
