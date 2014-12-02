package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class DivineFavor extends SpellCard {
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public DivineFavor(boolean hasBeenUsed) {
		super((byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public DivineFavor() {
		this(false);
	}

	
	public Object deepCopy() {
		return new DivineFavor(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Draw cards until you have as many in hand as your opponent
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
		if (isHero(targetMinion) || !isCurrentPlayer(side)) {
			return null;
		}
		int numCardsToDraw = PlayerSide.WAITING_PLAYER.getPlayer(boardState).getHand().size() - PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getHand().size() + 1;
		if (numCardsToDraw < 1)
			return null;

		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			if (toRet instanceof CardDrawNode)
				((CardDrawNode) toRet).addNumCardsToDraw(numCardsToDraw);
			else
				toRet = new CardDrawNode(toRet, numCardsToDraw); //draw two cards
		}
		return toRet;
	}
}
