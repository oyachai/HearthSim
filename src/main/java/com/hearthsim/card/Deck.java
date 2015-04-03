package com.hearthsim.card;

import com.hearthsim.util.DeepCopyable;

import java.util.ArrayList;
import java.util.Collections;

public class Deck implements DeepCopyable<Deck> {

    private final ArrayList<Card> cards;


    /**
     * Constructor
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Constructor
     * @param cards An array of cards from which to make the deck
     */
    public Deck(Card[] cards) {
        this.cards = new ArrayList<>();
        Collections.addAll(this.cards, cards);
    }

    /**
     * Constructor
     * @param cards An list of cards from which to make the deck
     */
    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        for (int i = cards.size() - 1; i > 0; --i) {
            int j = (int)(Math.random() * (i + 1));
            Card ci = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, ci);
        }
    }

    /**
     * Draw a specific card from the deck
     *
     * @param index The index of the card to draw
     * @return
     */
    public Card drawCard(int index) {
        if (index >= cards.size())
            return null;
        return cards.get(index);
    }

    /**
     * Returns the total number of cards in the deck
     *
     * @return
     */
    public int getNumCards() {
        return cards.size();
    }

    /**
     * Add a card to the end of the deck
     * @param card
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    public String toString() {
        String[] names = new String[this.cards.size()];
        for (int i = 0; i < this.cards.size(); i++) {
            names[i] = this.cards.get(i).getName();
        }
        return "[\"" + String.join("\",\"", names) + "\"]";
    }

    @Override
    public int hashCode() {
        int hash = 1;
        for (Card card : cards) {
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
        for (int indx = 0; indx < cards.size(); ++indx) {
                if (!cards.get(indx).equals(oD.cards.get(indx))) return false;
        }

        return true;
    }

    @Override
    public Deck deepCopy() {
        ArrayList<Card> copiedCards = new ArrayList<>();
        for (Card card : cards) {
            copiedCards.add(card.deepCopy());
        }
        return new Deck(copiedCards);
    }
}
