package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class Lightspawn extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public Lightspawn() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
    }

    @Override
    public byte getTotalAttack() {
        return this.getTotalHealth();
    }
}
