package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class LightningBolt extends SpellDamage {

    public LightningBolt() {
        super();
    }

    @Deprecated
    public LightningBolt(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
