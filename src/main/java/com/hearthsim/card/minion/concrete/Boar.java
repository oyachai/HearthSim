package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class Boar extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public Boar() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
    }
}
