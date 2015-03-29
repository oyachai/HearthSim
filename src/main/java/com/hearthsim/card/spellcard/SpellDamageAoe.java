package com.hearthsim.card.spellcard;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

@Deprecated
public class SpellDamageAoe extends SpellDamage implements CardEffectAoeInterface {

    protected CharacterFilter hitsFilter;

    public SpellDamageAoe() {
        super();
        this.hitsFilter = CharacterFilter.ENEMY_MINIONS;
        this.characterFilter = CharacterFilter.OPPONENT;
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
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public CharacterFilter getAoeFilter() {
        return this.hitsFilter;
    }
}
