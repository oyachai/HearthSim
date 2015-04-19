package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffTemp;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class HeroicStrike extends SpellTargetableCard {

    private final static EffectCharacter effect = new EffectCharacterBuffTemp(4);

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public HeroicStrike() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    /**
     * Heroic Strike
     *
     * Gives the hero +4 attack this turn
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        return HeroicStrike.effect;
    }
}
