package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilterTargetedSpell;

public class ShadowBolt extends SpellDamage {

    public ShadowBolt() {
        super();

        this.characterFilter = CharacterFilterTargetedSpell.ALL_MINIONS;
    }

    @Deprecated
    public ShadowBolt(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public SpellDamage deepCopy() {
        return new ShadowBolt(this.hasBeenUsed);
    }
}
