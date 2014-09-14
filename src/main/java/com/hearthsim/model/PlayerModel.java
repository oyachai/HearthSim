package com.hearthsim.model;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.MinionList;
import org.json.JSONObject;

public class PlayerModel implements DeepCopyable {

    private String name;
    private boolean isFirstPlayer; // used for identifying player 1 vs player 2
    private Hero hero;
    private Deck deck;
    private int mana;
    private MinionList minions;
    private byte spellDamage;
    private IdentityLinkedList<Card> hand;
    byte overload;

    public PlayerModel(String name, Hero hero, Deck deck) {
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

    public MinionList getMinions() {
        return minions;
    }

    public void setMinions(MinionList minions) {
        this.minions = minions;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public byte getSpellDamage() {
        return spellDamage;
    }

    public void setSpellDamage(byte spellDamage) {
        this.spellDamage = spellDamage;
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
                this.name,
                (Hero) this.hero.deepCopy(),
                this.deck //todo should be a deep copy, we're just using the index in boardmodel right now to compensate..
        );

        copiedPlayerModel.setMana(mana);
        copiedPlayerModel.setOverload(overload);
        copiedPlayerModel.setFirstPlayer(isFirstPlayer);

        for (Minion minion : minions) {
            copiedPlayerModel.getMinions().add((Minion) (minion).deepCopy());
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

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }
}
