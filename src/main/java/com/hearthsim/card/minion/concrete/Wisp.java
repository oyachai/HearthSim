package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class Wisp extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public Wisp() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

    }
}
