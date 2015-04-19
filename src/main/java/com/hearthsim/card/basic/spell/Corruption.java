package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class Corruption extends SpellTargetableCard {

    private static final EffectCharacter effect = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
        targetCharacter.setDestroyOnTurnStart(true);
        return boardState;
    };

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Corruption(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Corruption() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ENEMY_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Choose an enemy minion.  At the start of your turn, destroy it.
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
        return Corruption.effect;
    }
}
