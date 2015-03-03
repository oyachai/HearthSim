package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class MurlocRaider extends Minion {

    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = (byte) 0;

    public MurlocRaider() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
    }
}
