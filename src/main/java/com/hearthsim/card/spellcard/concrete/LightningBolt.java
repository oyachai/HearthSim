package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class LightningBolt extends SpellDamage {

    public LightningBolt() {
        this(false);
    }

    public LightningBolt(boolean hasBeenUsed) {
        super((byte)1, (byte)3, hasBeenUsed);
    }

    @Override
    public SpellDamage deepCopy() {
        return new LightningBolt(this.hasBeenUsed);
    }

}
