package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;

public class Fireball extends SpellDamageTargetableCard {

    public Fireball() {
        super();
    }

    @Deprecated
    public Fireball(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
