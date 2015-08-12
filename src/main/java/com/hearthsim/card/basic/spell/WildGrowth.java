package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerModel;

public class WildGrowth extends SpellTargetableCard {


    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public WildGrowth(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public WildGrowth() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * Gain an empty mana crystal (i.e., it increases maxMana by 1).  If maxMana is already 10, then it places
     * the ExcessMana card in your hand.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                PlayerModel player = boardState.data_.modelForSide(targetSide);
                if (player.getMaxMana() >= 10) {
                    player.placeCardHand(new ExcessMana());
                } else {
                    player.addMaxMana((byte) 1);
                }
                return boardState;
            };
        }
        return this.effect;
    }
}
