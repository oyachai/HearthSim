package com.hearthsim.card;

import java.util.ArrayList;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardAction {

	private Card card;
	
	public CardAction(Card card) {
		this.card = card;
	}
		
	
	public Card getCard() {
		return card;
	}
	
	public final HearthTreeNode useOn(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		return this.useOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, false);
	}  
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 *
     *
     *
     * @param side
     * @param targetMinion The target minion (can be a Hero)
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck for player0
     * @param deckPlayer1 The deck for player1
     * @param singleRealizationOnly For cards with random effects, setting this to true will return only a single realization of the random event.
     *
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode useOn(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		//A generic card does nothing except for consuming mana
		HearthTreeNode toRet = this.card.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);

		//Notify all other cards/characters of the card's use
		if (toRet != null) {
			ArrayList<Minion> tmpList = new ArrayList<Minion>(7);
            for (Card card : toRet.data_.getCurrentPlayerHand()) {
                toRet = card.getCardAction().otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = toRet.data_.getCurrentPlayerHero().getCardAction().otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
			{
                for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
                    tmpList.add(minion);
                }
				for (Minion minion : tmpList) {
					if (!minion.isSilenced())
						toRet = minion.getCardAction().otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
				}
			}
            for (Card card : toRet.data_.getWaitingPlayerHand()) {
                toRet = card.getCardAction().otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = toRet.data_.getWaitingPlayerHero().getCardAction().otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
			{
				tmpList.clear();
                for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
                    tmpList.add(minion);
                }
				for (Minion minion : tmpList) {
					if (!minion.isSilenced())
						toRet = minion.getCardAction().otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
				}
			}

			//check for and remove dead minions
			toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);			
		}
		
		
		return toRet;
	}  
    
	/**
	 * 
	 * Called whenever another card is used
	 *  @param thisCardPlayerSide The player index of the card receiving the event
	 * @param cardUserPlayerSide
     * @param usedCard The card that was used
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @param deckPlayer1 The deck of player1
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode otherCardUsedEvent(
			PlayerSide thisCardPlayerSide,
			PlayerSide cardUserPlayerSide,
			Card usedCard,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		return boardState;
	}

	
}
