package com.hearthsim.card.classic.minion.common;

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
