package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.event.effect.EffectCharacter;

public class DivineSpirit extends SpellTargetableCard {

    public static final EffectCharacter effect = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
        byte healthDiff = targetCharacter.getHealth();
        targetCharacter.setHealth((byte)(targetCharacter.getHealth() * 2));
        targetCharacter.setMaxHealth((byte)(targetCharacter.getMaxHealth() + healthDiff));
        return boardState;
    };

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public DivineSpirit(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public DivineSpirit() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * This card heals the target minion up to full health and gives it taunt.  Cannot be used on heroes.
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
        return DivineSpirit.effect;
    }
}
