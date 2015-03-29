package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;

public class ArcaneExplosion extends SpellDamage implements CardEffectAoeInterface {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public ArcaneExplosion(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     * Defaults to hasBeenUsed = false
     */
    public ArcaneExplosion() {
        super();
        this.characterFilter = CharacterFilterTargetedSpell.OPPONENT;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ENEMY_MINIONS;
    }
}
