package com.hearthsim.card;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.VioletApprentice;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardActionVioletTeacher extends CardAction{

	public CardActionVioletTeacher(Card card) {
		super(card);
	}
	
	/**
	 * 
	 * Called whenever another card is used
	 * 
	 * When you cast a spell, summon a 1/1 Violet Apprentice
	 *  @param thisCardPlayerSide The player index of the card receiving the event
	 * @param cardUserPlayerSide
     * @param usedCard The card that was used
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @param deckPlayer1 The deck of player1
     * @return The boardState is manipulated and returned
	 * @throws HSException 
	 */
	@Override
	public HearthTreeNode otherCardUsedEvent(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = super.otherCardUsedEvent(thisCardPlayerSide, cardUserPlayerSide, usedCard, boardState, deckPlayer0, deckPlayer1);
		if (thisCardPlayerSide != PlayerSide.CURRENT_PLAYER)
			return toRet;
		if (getCard().isInHand_)
			return toRet;
        if (usedCard instanceof SpellCard && PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions() < 7) {
            Minion newMinion = new VioletApprentice();
            toRet = newMinion.summonMinion(thisCardPlayerSide, (Minion)this.getCard(), toRet, deckPlayer0, deckPlayer1, false);
        }
        return toRet;
	}
}
