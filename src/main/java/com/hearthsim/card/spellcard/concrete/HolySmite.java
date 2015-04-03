package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class HolySmite extends SpellDamage {

    public HolySmite() {
        super();
    }

    @Deprecated
    public HolySmite(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
