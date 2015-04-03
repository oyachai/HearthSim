package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;

public class LavaBurst extends SpellDamageTargetableCard {

    public LavaBurst() {
        super();
    }

    @Deprecated
    public LavaBurst(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
