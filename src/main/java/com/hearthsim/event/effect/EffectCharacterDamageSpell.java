package com.hearthsim.event.effect;

import com.hearthsim.card.Card;

public class EffectCharacterDamageSpell<T extends Card> extends EffectCharacterDamage<T> {
    public EffectCharacterDamageSpell(int damage) {
        super(damage, true);
    }
}
