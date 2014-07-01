package com.hearthsim.player;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.Hand;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.exception.HSException;

public class Player {

	String name_;
	Hero hero_;
	Hand hand_;
	Deck deck_;
	int mana_;
	int maxMana_;

	public Player(String name, Hero hero, Deck deck) {
		name_ = name;
		hero_ = hero;
		deck_ = deck;
		hand_ = new Hand();
	}
	
	public Card drawFromDeck(int index) {
		Card card = deck_.drawCard(index);
		if (card == null) {
			return null;
		}
		hand_.addCard(card);
		card.isInHand(true);
		return card;
	}
	
	public String getName() {
		return name_;
	}
	
	public Card playFromHand(int cardIndex) throws HSException {
		return hand_.playFromHand(cardIndex);
	}
	
	public int getNumCardsInHand() {
		return hand_.getNumCards();
	}
	
	public int getMana() {
		return mana_;
	}
	
	public void setMana(int mana) {
		mana_ = mana;
	}
	
	public void addMana(int mana) {
		mana_ += mana;
	}
	
	public void removeMana(int mana) {
		mana_ -= mana;
	}
	
	public int getMaxMana() {
		return maxMana_;
	}
	
	public void addMaxMana(int mana) {
		maxMana_ += mana;
	}
	
	public void removeMaxMana(int mana) {
		maxMana_ -= mana;
	}
	
	public String toString() {
		String toRet = "{ \"name\":" + name_ + " \"hero\": " + hero_ + ", \"hand\": " + hand_ + "}";
		return toRet;
	}
}
