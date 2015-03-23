package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.MinionFilterSpellTargetable;

public class ShadowBolt extends SpellDamage {

    public ShadowBolt() {
        super();

        this.minionFilter = MinionFilterSpellTargetable.ALL_MINIONS;
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
