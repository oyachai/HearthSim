package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffTemp;

public class HeroicStrike extends SpellCard {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffTemp(4);

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public HeroicStrike() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.SELF;
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
    public CardEffectCharacter getTargetableEffect() {
        return HeroicStrike.effect;
    }
}
