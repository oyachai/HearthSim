package com.hearthsim.card;

import com.hearthsim.exception.HSException;

import java.util.ArrayList;



public class Hand {

	int numCards_;
	ArrayList<Card> cards_;
	
	public Hand() {
		numCards_ = 0;
		cards_ = new ArrayList<Card>();
	}
	
	public void addCard(Card card) {
		cards_.add(card);
		++numCards_;
	}
	
	
	public Card playFromHand(int cardIndex) throws HSException {
		if (cardIndex < 0 || cardIndex >= cards_.size()) {
			throw new HSException("invalid cardIndex");
		}
		Card toRet = cards_.get(cardIndex);
		cards_.remove(cardIndex);
		return toRet;
	}
	
	public int getNumCards() {
		return numCards_;
	}
	
	@Override
	public String toString() {
		String toRet = "{\"nc\": " + numCards_ + ", \"cards\": [";
		for (int i = 0; i < cards_.size() - 1; ++i) {
			toRet = toRet + cards_.get(i) + ", ";
		}
		toRet = toRet + cards_.get(cards_.size()-1) + "]}";
		return toRet;
	}
}