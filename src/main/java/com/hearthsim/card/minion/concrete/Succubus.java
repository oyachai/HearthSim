package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Demon;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class Succubus extends Demon {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public Succubus() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	/**
	 * 
	 * Override for battlecry
	 * 
	 * Battlecry: Discard a random card
	 * 
	 * INCOMPLETE IMPLEMENTATION!!!!!!!!!!!!!!
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	
	public HearthTreeNode use_core(
			PlayerSide side,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{

		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		
		int numCards = toRet.data_.getNumCardsHandCurrentPlayer();
		if (numCards <= 0)
			return null;
		int cardToDiscardIndex = (int)(Math.random() * numCards);
		toRet.data_.removeCardFromHand(PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().get(cardToDiscardIndex), PlayerSide.CURRENT_PLAYER);
							
		return boardState;
	}
}
