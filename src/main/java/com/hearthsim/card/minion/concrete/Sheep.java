package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class Sheep extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public Sheep() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
    }
}
