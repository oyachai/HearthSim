package com.hearthsim.card.thegrandtournament.spell.common;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;

/**
 * Created by oyachai on 8/14/15.
 */
public class FlashHeal extends SpellTargetableCard {

    private static final byte HEAL_AMOUNT = 5;

    public FlashHeal() {
        super();
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new EffectCharacterHeal(FlashHeal.HEAL_AMOUNT);
        }
        return this.effect;
    }
}
