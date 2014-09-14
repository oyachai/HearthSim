package com.hearthsim.model;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import org.json.JSONObject;

public class PlayerModel {

	String name;
	public final Hero hero;
	final Deck deck;
	int mana;

	public PlayerModel(String name, Hero hero, Deck deck) {
		this.name = name;
		this.hero = hero;
		this.deck = deck;
	}
	
	public Card drawFromDeck(int index) {
		Card card = deck.drawCard(index);
		if (card == null) {
			return null;
		}
		card.isInHand(true);
		return card;
	}


	public Deck getDeck() {
		return deck;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hero getHero() {
        return hero;
    }

    public Deck getDeck_() {
        return deck;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public String toString() {
        return new JSONObject(this).toString();
	}
}
