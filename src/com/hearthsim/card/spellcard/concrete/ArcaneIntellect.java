package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.util.BoardState;

public class ArcaneIntellect extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public ArcaneIntellect(boolean hasBeenUsed) {
		super("Arcane Intellect", (byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public ArcaneIntellect() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This card draws 2 cards from the deck.
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public BoardState useOn(int thisCardIndex, int playerIndex, int minionIndex, BoardState boardState, Deck deck) {
		if (playerIndex == 1 || minionIndex > 0) {
			return null;
		}
		
		for (int index = 0; index < 2; ++index) {
			Card card = deck.drawCard(boardState.getDeckPos());
			if (card == null) {
				byte fatigueDamage = boardState.getFatigueDamage_p0();
				boardState.setFatigueDamage_p0((byte)(fatigueDamage + 1));
				boardState.getHero_p0().setHealth((byte)(boardState.getHero_p0().getHealth() - fatigueDamage));
			} else {
				boardState.placeCard_hand(card);
				boardState.setDeckPos(boardState.getDeckPos() + 1);
			}
		}

		return super.useOn(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
