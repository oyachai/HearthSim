package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class Charge extends SpellTargetableCard {

    public static final EffectCharacter effect = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
        targetCharacter.addAttack(2);
        targetCharacter.setCharge(true);
        return boardState;
    };

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Charge(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Charge() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.FRIENDLY_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * This card gives the target +2 attack and Charge.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        return Charge.effect;
    }
}
