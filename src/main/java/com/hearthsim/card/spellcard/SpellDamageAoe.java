package com.hearthsim.card.spellcard;

import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.event.effect.CardEffectCharacter;

@Deprecated
public class SpellDamageAoe extends SpellDamage implements CardEffectAoeInterface {

    private CharacterFilter hitsFilter;

    public SpellDamageAoe() {
        super();
        this.hitsFilter = CharacterFilter.ENEMY_MINIONS;
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.OPPONENT;
    }

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public SpellDamageAoe(byte mana, byte damage, boolean hasBeenUsed) {
        super(mana, damage, hasBeenUsed);
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getTargetableEffect(); }

    @Override
    public CharacterFilter getAoeFilter() {
        return this.hitsFilter;
    }
}
