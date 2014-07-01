package com.hearthsim.card;

public class Deck {

	Card[] cards_;
	
	public Deck(Card[] cards) {
		cards_ = cards;
	}
	
	public void shuffle() {
		for(int i = cards_.length - 1; i > 0; --i) {
			int j = (int)(Math.random() * (i + 1));
			Card ci = cards_[i];
			cards_[i] = cards_[j];
			cards_[j] = ci;
		}
	}
	
	public Card drawCard(int index) {
		if (index >= cards_.length)
			return null;
		return cards_[index];
	}
	

}
