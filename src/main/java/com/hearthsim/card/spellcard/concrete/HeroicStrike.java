package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacterBuffTemp;

public class HeroicStrike extends SpellCard {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffTemp(4);

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public HeroicStrike(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public HeroicStrike() {
        super();

        this.characterFilter = CharacterFilterTargetedSpell.SELF;
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
    protected CardEffectCharacter getEffect() {
        return HeroicStrike.effect;
    }
}
