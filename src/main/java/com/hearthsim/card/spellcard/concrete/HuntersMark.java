package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuff;

public class HuntersMark extends SpellTargetableCard {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuff(0, 1);

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public HuntersMark(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public HuntersMark() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * This card damages all enemy minions by 1
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
        return HuntersMark.effect;
    }
}
