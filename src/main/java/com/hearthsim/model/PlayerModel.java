package com.hearthsim.model;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.IdentityLinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayerModel implements DeepCopyable<PlayerModel> {

    private final String name;
    private final byte playerId; // used for identifying player 0 vs player 1
    private final Hero hero;
    private final Deck deck;

    private byte mana;
    private byte maxMana;

    private byte deckPos;
    private byte fatigueDamage;

    private IdentityLinkedList<Minion> minions;
    private IdentityLinkedList<Card> hand;
    byte overload;

    private byte numCardsUsed;

    public PlayerModel(byte playerId, String name, Hero hero, Deck deck) {
        this.playerId = playerId;
        this.name = name;
        this.hero = hero;
        this.deck = deck;
        this.minions = new IdentityLinkedList<>();
        this.hand = new IdentityLinkedList<>();
        this.numCardsUsed = 0;
        this.deckPos = 0;
        this.fatigueDamage = 1;
    }

    public Card drawFromDeck(int index) {
        Card card = deck.drawCard(index);
        if (card == null) {
            return null;
        }
        card.isInHand(true);
        return card;
    }

    public Minion getCharacter(int index) {
        return index == 0 ? this.getHero() : this.getMinions().get(index - 1);
    }

    public int getNumCharacters() {
        return this.getNumMinions() + 1;
    }

    public int getNumMinions() {
        return minions.size();
    }

    public Deck getDeck() {
        return deck;
    }

    public String getName() {
        return name;
    }

    public Hero getHero() {
        return hero;
    }

    @Deprecated
    public Deck getDeck_() {
        return deck;
    }

    public byte getMana() {
        return mana;
    }

    public void setMana(byte mana) {
        this.mana = mana;
    }

    public void addMana(byte value) {
        this.mana += value;
    }

    public void subtractMana(byte value) {
        this.mana -= value;
    }

    public byte getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(byte maxMana) {
        this.maxMana = maxMana;
    }

    public void addMaxMana(byte value) {
        this.maxMana += value;
    }

    public void subtractMaxMana(byte value) {
        this.maxMana -= value;
    }

    public IdentityLinkedList<Minion> getMinions() {
        return minions;
    }

    public void setMinions(IdentityLinkedList<Minion> minions) {
        this.minions = minions;
    }

    public byte getSpellDamage() {
        byte spellDamage = 0;
        for (Minion minion : minions)
            spellDamage += minion.getSpellDamage();
        return spellDamage;
    }

    public IdentityLinkedList<Card> getHand() {
        return hand;
    }

    public void setHand(IdentityLinkedList<Card> hand) {
        this.hand = hand;
    }

    public byte getOverload() {
        return overload;
    }

    public void setOverload(byte overload) {
        this.overload = overload;
    }

    public void addOverload(byte overload) {
        this.overload += overload;
    }

    public byte getDeckPos() {
        return deckPos;
    }

    public void setDeckPos(byte value) {
        deckPos = value;
    }

    public void addDeckPos(byte value) {
        deckPos += value;
    }

    public byte getFatigueDamage() {
        return fatigueDamage;
    }

    public void setFatigueDamage(byte value) {
        fatigueDamage = value;
    }

    public void addFatigueDamage(byte value) {
        fatigueDamage += value;
    }

    public byte getNumCardsUsed() {
        return numCardsUsed;
    }

    public void setNumCardsUsed(byte numCardsUsed) {
        this.numCardsUsed = numCardsUsed;
    }

    public void addNumCardsUsed(byte value) {
        this.numCardsUsed += value;
    }

    public boolean isComboEnabled() {
        //don't count the card that was just used
        return numCardsUsed > 1;
    }

    public boolean isBoardFull() {
        return this.getNumMinions() >= 7;
    }

    public int getIndexForCharacter(Minion character) {
        if (character.isHero()) {
            return 0;
        } else {
            return this.getMinions().indexOf(character) + 1;
        }
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

    @Override
    public PlayerModel deepCopy() {
        PlayerModel copiedPlayerModel = new PlayerModel(
                this.playerId,
                this.name,
                this.hero.deepCopy(),
                this.deck //TODO should be a deep copy, we're just using the index in boardmodel right now to compensate..
                //oyachai: the use of the deck position index is actually an attempt to reduce memory usage.
        );

        copiedPlayerModel.setMana(mana);
        copiedPlayerModel.setMaxMana(maxMana);
        copiedPlayerModel.setOverload(overload);
        copiedPlayerModel.deckPos = deckPos;
        copiedPlayerModel.fatigueDamage = fatigueDamage;
        copiedPlayerModel.numCardsUsed = numCardsUsed;

        for (Minion minion : minions) {
            copiedPlayerModel.getMinions().add((Minion) (minion).deepCopy());
        }

        for (final Card card: hand) {
            Card tc = card.deepCopy();
            copiedPlayerModel.placeCardHand(tc);
        }

        return copiedPlayerModel;
    }

    public void placeCardHand(Card card) {
        card.isInHand(true);
        hand.add(card);
    }

    public void placeCardHand(int cardIndex) {
        Card card = drawFromDeck(cardIndex);
        card.isInHand(true);
        hand.add(card);
    }

    public void placeCardDeck(Card card) {
        deck.addCard(card);
    }

    public void drawNextCardFromDeck() {
        Card card = drawFromDeck(deckPos);
        if (card == null) {
            //no more card left in deck, take fatigue damage
            hero.takeDamage((byte)fatigueDamage);
            ++fatigueDamage;
        } else {
            card.isInHand(true);
            hand.add(card);
            ++deckPos;
        }
    }

    public ArrayList<Minion> getMinionsAdjacentToCharacter(int characterIndex) {
        ArrayList<Minion> adjMinions = new ArrayList<Minion>();

        if(characterIndex >= this.getNumCharacters()) {
            return adjMinions;
        }

        if (characterIndex > 1) {
            adjMinions.add(this.getCharacter(characterIndex - 1));
        }
        if (characterIndex < (this.getNumCharacters() - 1)) {
            adjMinions.add(this.getCharacter(characterIndex + 1));
        }
        return adjMinions;
    }

    public byte getPlayerId() {
        return playerId;
    }

    public void resetMana() {
        mana = maxMana;
        mana -= overload;
        overload = 0;
        resetNumCardsUsed();
    }

    public void resetNumCardsUsed() {
        numCardsUsed = 0;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + (null == name ? 0 : name.hashCode());
        hash = hash * 31 + playerId;
        hash = hash * 31 + (null == hero ? 0 : hero.hashCode());
        hash = hash * 31 + (null == deck ? 0 : deck.hashCode());
        hash = hash * 31 + mana;
        hash = hash * 31 + maxMana;
        hash = hash * 31 + deckPos;
        hash = hash * 31 + fatigueDamage;
        hash = hash * 31 + (null == minions ? 0 : minions.hashCode());
        hash = hash * 31 + (null == hand ? 0 : hand.hashCode());
        hash = hash * 31 + overload;
        hash = hash * 31 + numCardsUsed;
        return hash;
    }

    @Override
    public boolean equals(Object other) {

        if (other == null)
            return false;

        if (this.getClass() != other.getClass())
            return false;

        PlayerModel otherPlayer = (PlayerModel)other;

        if (playerId != otherPlayer.playerId) return false;
        if (mana != otherPlayer.mana) return false;
        if (maxMana != otherPlayer.maxMana) return false;
        if (overload != otherPlayer.overload) return false;
        if (deckPos != otherPlayer.deckPos) return false;
        if (fatigueDamage != otherPlayer.fatigueDamage) return false;

        if (!name.equals(otherPlayer.name)) return false;
        if (!hero.equals(otherPlayer.hero)) return false;
        if (deck != null && !deck.equals(otherPlayer.deck)) return false;
        if (!minions.equals(otherPlayer.minions)) return false;
        if (!hand.equals(otherPlayer.hand)) return false;
        if (numCardsUsed != otherPlayer.numCardsUsed) return false;
        return true;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("playerId", playerId);
        json.put("hero", hero.toJSON());
        if (mana != maxMana) json.put("mana", mana);
        if (maxMana > 0) json.put("maxMana", maxMana);
        json.put("deckPos", deckPos);
        if (overload > 0) json.put("overload", overload);
        if (fatigueDamage > 0) json.put("fatigueDamage", fatigueDamage);

        if (minions.size() > 0) {
            JSONArray array = new JSONArray();
            for (Minion minion: minions) {
                array.put(minion.toJSON());
            }
            json.put("minions", array);
        }

        if (hand.size() > 0) {
            JSONArray array = new JSONArray();
            for (Card card: hand) {
                array.put(card.toJSON());
            }
            json.put("hand", array);
        }

        if (numCardsUsed > 0) {
            json.put("numCardsUsed", numCardsUsed);
        }

        return json;
    }
}
