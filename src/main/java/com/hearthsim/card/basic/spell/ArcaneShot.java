package com.hearthsim.card.basic.spell;

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
