package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;

public class HolySmite extends SpellDamageTargetableCard {

    public HolySmite() {
        super();
    }

    @Deprecated
    public HolySmite(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
