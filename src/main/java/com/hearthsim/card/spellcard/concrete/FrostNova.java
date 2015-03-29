package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;

public class FrostNova extends SpellCard implements CardEffectAoeInterface {
    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public FrostNova(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public FrostNova() {
        super();

        this.characterFilter = CharacterFilterTargetedSpell.OPPONENT;
    }

    /**
     *
     * Use the card on the given target
     *
     * This freeze all enemy minions
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
        return CardEffectCharacter.FREEZE;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ENEMY_MINIONS;
    }
}
