package com.hearthsim.card;

import java.util.ArrayList;

public class Deck {

	Card[] cards_;
	
	/**
	 * Constructor
	 * @param cards An array of cards from which to make the deck
	 */
	public Deck(Card[] cards) {
		cards_ = cards;
	}

	/**
	 * Constructor
	 * @param cards An list of cards from which to make the deck
	 */
	public Deck(ArrayList<Card> cards) {
		cards_ = new Card[cards.size()];
		cards.toArray(cards_);
	}

	/**
	 * Shuffles the deck
	 */
	public void shuffle() {
		for(int i = cards_.length - 1; i > 0; --i) {
			int j = (int)(Math.random() * (i + 1));
			Card ci = cards_[i];
			cards_[i] = cards_[j];
			cards_[j] = ci;
		}
	}
	
	/**
	 * Draw a specific card from the deck
	 * 
	 * @param index The index of the card to draw
	 * @return
	 */
	public Card drawCard(int index) {
		if (index >= cards_.length)
			return null;
		return cards_[index];
	}
	

}
