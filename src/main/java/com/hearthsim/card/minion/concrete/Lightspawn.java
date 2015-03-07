package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class Lightspawn extends Minion {

    public Lightspawn() {
        super();
    }

    @Override
    public byte getTotalAttack() {
        return this.getTotalHealth();
    }
}
