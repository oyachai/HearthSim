package com.hearthsim.model;

import com.hearthsim.card.Card;
import com.hearthsim.util.IdentityLinkedList;

/**
 * Created by oyachai on 6/24/15.
 */
public class HandModel extends IdentityLinkedList<Card> {

    private final byte maxCardsInHand;

    public HandModel() {
        this((byte)10);
    }

    public HandModel(byte maxCardsInHand) {
        this.maxCardsInHand = maxCardsInHand;
    }

    public boolean isFull() {
        return this.size() >= maxCardsInHand;
    }
}
