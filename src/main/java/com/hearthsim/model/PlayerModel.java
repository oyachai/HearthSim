package com.hearthsim.model;

import org.json.JSONObject;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.MinionList;

public class PlayerModel implements DeepCopyable {

    private final String name;
    private final int playerId; // used for identifying player 0 vs player 1
    private final Hero hero;
    private final Deck deck;
    
    private int mana;
    private int maxMana;
    
    private MinionList minions;
    private byte spellDamage;
    private IdentityLinkedList<Card> hand;
    byte overload;

    public PlayerModel(int playerId, String name, Hero hero, Deck deck) {
    	this.playerId = playerId;
        this.name = name;
        this.hero = hero;
        this.deck = deck;
        this.minions = new MinionList();
        this.hand = new IdentityLinkedList<>();
    }

    public Card drawFromDeck(int index) {
        Card card = deck.drawCard(index);
        if (card == null) {
            return null;
        }
        card.isInHand(true);
        return card;
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

    public Deck getDeck_() {
        return deck;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void addMana(int value) {
        this.mana += value;
    }

    public void subtractMana(int value) {
        this.mana -= value;
    }

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}
	
	public void addMaxMana(int value) {
		this.maxMana += value;
	}
	
	public void subtractMaxMana(int value) {
		this.maxMana -= value;
	}
	
    public MinionList getMinions() {
        return minions;
    }

    public void setMinions(MinionList minions) {
        this.minions = minions;
    }

    public byte getSpellDamage() {
        return spellDamage;
    }

    public void setSpellDamage(byte spellDamage) {
        this.spellDamage = spellDamage;
    }

    public void addSpellDamage(byte spellDamage) {
        this.spellDamage += spellDamage;
    }

    public void subtractSpellDamage(byte spellDamage) {
        this.spellDamage -= spellDamage;
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

    public String toString() {
        return new JSONObject(this).toString();
    }

    @Override
    public Object deepCopy() {
        PlayerModel copiedPlayerModel = new PlayerModel(
        		this.playerId,
                this.name,
                (Hero) this.hero.deepCopy(),
                this.deck //todo should be a deep copy, we're just using the index in boardmodel right now to compensate..
                //oyachai: the use of the deck position index is actually an attempt to reduce memory usage.
        );

        copiedPlayerModel.setMana(mana);
        copiedPlayerModel.setMaxMana(maxMana);
        copiedPlayerModel.setOverload(overload);

        for (BaseEntity minion : minions) {
            copiedPlayerModel.getMinions().add((BaseEntity) (minion).deepCopy());
        }

        copiedPlayerModel.setSpellDamage(spellDamage);

        for (final Card card: hand) {
            Card tc = (Card)card.deepCopy();
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

    public int getPlayerId() {
        return playerId;
    }

    public void resetMana() {
    	mana = maxMana;
    	mana -= overload;
    	overload = 0;
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
    	hash = hash * 31 + (null == minions ? 0 : minions.hashCode());
    	hash = hash * 31 + spellDamage;
    	hash = hash * 31 + (null == hand ? 0 : hand.hashCode());
    	hash = hash * 31 + overload;
    	        
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
        if (spellDamage != otherPlayer.spellDamage) return false;
        if (overload != otherPlayer.overload) return false;

        if (!name.equals(otherPlayer.name)) return false;
        if (!hero.equals(otherPlayer.hero)) return false;
        if (deck != null && !deck.equals(otherPlayer.deck)) return false;
        if (!minions.equals(otherPlayer.minions)) return false;
        if (!hand.equals(otherPlayer.hand)) return false;
        
        return true;
    }


}
