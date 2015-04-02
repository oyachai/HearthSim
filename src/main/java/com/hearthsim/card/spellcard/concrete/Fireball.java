package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class Fireball extends SpellDamage {

    public Fireball() {
        super();
    }

    @Deprecated
    public Fireball(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
