package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;

public class LightningBolt extends SpellDamageTargetableCard {

    public LightningBolt() {
        super();
    }

    @Deprecated
    public LightningBolt(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
