package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class LavaBurst extends SpellDamage {

    public LavaBurst() {
        this(false);
    }

    public LavaBurst(boolean hasBeenUsed) {
        super((byte)3, (byte)5, hasBeenUsed);
    }

    @Override
    public SpellDamage deepCopy() {
        return new LavaBurst(this.hasBeenUsed);
    }

}
