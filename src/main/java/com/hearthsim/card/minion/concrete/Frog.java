package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class Frog extends Beast {

    private static final boolean HERO_TARGETABLE = true;
    private static final boolean TRANSFORMED = true;
    private static final byte SPELL_DAMAGE = 0;

    public Frog() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        transformed_ = TRANSFORMED;
    }

}
