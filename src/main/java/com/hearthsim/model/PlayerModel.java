package com.hearthsim.model;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import org.json.JSONObject;

public class PlayerModel {

	String name_;
	public final Hero hero_;
	final Deck deck_;
	int mana_;

	public PlayerModel(String name, Hero hero, Deck deck) {
		name_ = name;
		hero_ = hero;
		deck_ = deck;
	}
	
	public Card drawFromDeck(int index) {
		Card card = deck_.drawCard(index);
		if (card == null) {
			return null;
		}
		card.isInHand(true);
		return card;
	}
	
	public String getName() {
		return name_;
	}
	
	public Deck getDeck() {
		return deck_;
	}
	
	public int getMana() {
		return mana_;
	}
	
	public void setMana(int mana) {
		mana_ = mana;
	}

	public String toString() {
        return new JSONObject(this).toString();
	}
}
