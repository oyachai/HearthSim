package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;

public class ArcaneShot extends SpellDamageTargetableCard {

    public ArcaneShot() {
        super();
    }

    @Deprecated
    public ArcaneShot(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
