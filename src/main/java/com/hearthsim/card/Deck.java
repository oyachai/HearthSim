package com.hearthsim.card;

import java.util.ArrayList;

import com.hearthsim.util.DeepCopyable;

public class Deck implements DeepCopyable {

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
	
	/**
	 * Returns the total number of cards in the deck
	 * 
	 * @return
	 */
	public int getNumCards() {
		return cards_.length;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		for (Card card : cards_) {
			hash = hash * 31 + (card != null ? card.hashCode() : 0);
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
        if (other == null)
            return false;

        if (this.getClass() != other.getClass())
            return false;
        
        Deck oD = (Deck)other;
        for (int indx = 0; indx < cards_.length; ++indx) {
    		if (!cards_[indx].equals(oD.cards_[indx])) return false;
        }
        
        return true;
	}

	@Override
	public Object deepCopy() {
		Card[] copiedCards = new Card[cards_.length];
		int indx = 0;
		for (Card card : cards_) {
			copiedCards[indx++] = (Card)card.deepCopy();
		}
		Deck copied = new Deck(copiedCards);
		return copied;
	}
}
